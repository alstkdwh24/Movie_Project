package com.example.movie.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

/**
 * 포트원 V2 웹훅 서명 검증(Standard Webhooks 스펙).
 * 서명 대상은 "webhook-id.webhook-timestamp.본문", 키는 웹훅 시크릿(whsec_ 뒤 base64)을 디코딩한 값, HMAC-SHA256.
 */
@Component
public class PortOneWebhookVerifier {

    private static final long TOLERANCE_SECONDS = 5 * 60; // 오래된 요청을 다시 보내는 공격 방지

    private final String webhookSecret;

    public PortOneWebhookVerifier(@Value("${portone.webhook-secret:}") String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public boolean verify(String id, String timestamp, String signatureHeader, String body) {
        return verify(id, timestamp, signatureHeader, body, Instant.now().getEpochSecond());
    }

    boolean verify(String id, String timestamp, String signatureHeader, String body, long nowEpochSeconds) {
        if (webhookSecret.isBlank()) {
            throw new IllegalStateException("portone.webhook-secret 설정이 없습니다");
        }
        if (id == null || timestamp == null || signatureHeader == null || body == null) {
            return false;
        }
        long sentAt;
        try {
            sentAt = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            return false;
        }
        if (Math.abs(nowEpochSeconds - sentAt) > TOLERANCE_SECONDS) {
            return false;
        }

        byte[] expected = sign(id + "." + timestamp + "." + body);
        // 헤더에는 "v1,서명" 이 공백으로 여러 개 올 수 있다(시크릿 교체 중)
        for (String part : signatureHeader.split(" ")) {
            int comma = part.indexOf(',');
            if (comma < 0 || !"v1".equals(part.substring(0, comma))) {
                continue;
            }
            byte[] given;
            try {
                given = Base64.getDecoder().decode(part.substring(comma + 1));
            } catch (IllegalArgumentException e) {
                continue;
            }
            if (MessageDigest.isEqual(expected, given)) {
                return true;
            }
        }
        return false;
    }

    private byte[] sign(String content) {
        String encodedKey = webhookSecret.startsWith("whsec_") ? webhookSecret.substring("whsec_".length()) : webhookSecret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(Base64.getDecoder().decode(encodedKey), "HmacSHA256"));
            return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalStateException("portone.webhook-secret 형식이 올바르지 않습니다", e);
        }
    }
}
