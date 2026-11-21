package com.example.movie.util;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Standard Webhooks 서명 검증: "id.timestamp.본문"을 시크릿으로 HMAC-SHA256 → "v1,base64" */
public class PortOneWebhookVerifierTest {

    public static final byte[] KEY = "test-webhook-key-0123456789abcdef".getBytes(StandardCharsets.UTF_8);
    public static final String SECRET = "whsec_" + Base64.getEncoder().encodeToString(KEY);
    private static final long NOW = 1_700_000_000L;
    private static final String BODY = "{\"type\":\"Transaction.Paid\",\"data\":{\"paymentId\":\"rsv-1\"}}";

    private final PortOneWebhookVerifier verifier = new PortOneWebhookVerifier(SECRET);

    public static String sign(String id, String timestamp, String body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(KEY, "HmacSHA256"));
        return "v1," + Base64.getEncoder().encodeToString(
                mac.doFinal((id + "." + timestamp + "." + body).getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void 올바른_서명이면_통과() throws Exception {
        String ts = String.valueOf(NOW);
        assertThat(verifier.verify("msg_1", ts, sign("msg_1", ts, BODY), BODY, NOW)).isTrue();
    }

    @Test
    void 본문이나_id가_바뀌면_거부() throws Exception {
        String ts = String.valueOf(NOW);
        String signature = sign("msg_1", ts, BODY);
        assertThat(verifier.verify("msg_1", ts, signature, BODY.replace("rsv-1", "rsv-2"), NOW)).isFalse();
        assertThat(verifier.verify("msg_2", ts, signature, BODY, NOW)).isFalse();
    }

    @Test
    void 오분이_지난_요청은_서명이_맞아도_거부() throws Exception {
        String old = String.valueOf(NOW - 301);
        assertThat(verifier.verify("msg_1", old, sign("msg_1", old, BODY), BODY, NOW)).isFalse();
    }

    @Test
    void 서명이_여러_개면_하나만_맞아도_통과_시크릿_교체_중() throws Exception {
        String ts = String.valueOf(NOW);
        String header = "v1,AAAA " + sign("msg_1", ts, BODY);
        assertThat(verifier.verify("msg_1", ts, header, BODY, NOW)).isTrue();
    }

    @Test
    void 헤더가_없으면_거부하고_시크릿_설정이_없으면_예외() {
        assertThat(verifier.verify(null, null, null, BODY, NOW)).isFalse();
        assertThatThrownBy(() -> new PortOneWebhookVerifier("").verify("msg_1", "1", "v1,AAAA", BODY, NOW))
                .isInstanceOf(IllegalStateException.class);
    }
}
