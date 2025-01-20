package com.example.movie.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아서 File로 전환한 후에 S3에 업로드합니다.
    public String upload(MultipartFile multipartFile, String dirName) throws IOException{
        File uploadFile = convert(multipartFile)
        .orElseThrow(() -> new IllegalArgumentException("MultipartFile에서 File전환에 실패"));
        return upload(uploadFile, dirName);
    }
    private String upload(File uploadFile, String dirName){
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    //publicRead권한으로 업로드가 되는것 같습니다.
    private String putS3(File uploadFile, String fileName){
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, fileName).toString();

    }


    private void removeNewFile(File targetFile){
        if(targetFile.delete()){
            log.info("파일이 삭제되었습니다.");

        }else{
            log.info("파일이 삭제되지 않았습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}
