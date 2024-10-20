package com.example.movie;

import com.example.movie.ShopService.ShopService;
import com.example.movie.commandVO.MainsVO.DeliciousVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest
public class MovieTest {


    @Value("${project.upload.path}")
    private String uploadPath;

    @Autowired
    @Qualifier("shopService")
    ShopService shopService;
    public void makeFolder(){
        String movie_filepath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File file=new File(uploadPath + '/' +movie_filepath);
        if (file.exists() == false) {//해당 파일이 있으면 true, 없으면 false
            file.mkdirs();

        }
    }

    @Test
    public void test01()  {
        String movie_filepath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        makeFolder();
        String delicious_filename = ("30000386316_1280.jpg");
        String uuid = UUID.randomUUID().toString();

        DeliciousVO vo = DeliciousVO.builder()
                .delicious_name("봇치 더 록!전편 보기")
                .delicious_filename(delicious_filename)
                .uuid(uuid)
                .movie_filepath(movie_filepath)
                .reg_date(LocalDateTime.now())
                .build();

        shopService.delicious_Resist(vo);


    }


}
