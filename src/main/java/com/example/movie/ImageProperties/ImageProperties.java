package com.example.movie.ImageProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "image")
public class ImageProperties {
    private static final String DEFAULT_IMAGE_STORAGE_PATH = "C:/Users/alstk/2course/JAVA/portfolio_project/";

    private String storagePath = DEFAULT_IMAGE_STORAGE_PATH; // 기본값 설정

    public String getFullPath(String filename,String filePath) {
        // 주어진 파일 이름을 사용하여 전체 경로를 생성하는 메서드
        return storagePath + filePath+filename;
    }
}