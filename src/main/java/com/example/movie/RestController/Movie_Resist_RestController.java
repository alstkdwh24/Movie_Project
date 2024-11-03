package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@RestController
@RequestMapping("/movie_resist")
public class Movie_Resist_RestController {

    @Value("${project.upload.path}")
    private String uploadPath;

    @Autowired
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_Image_Service;

    public String makeFolder() {
        String filepath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        File file = new File(uploadPath + '/' + filepath);
        if (!file.exists()) { //해당 파일이 있으면 true, 없으면!
            file.mkdirs();
        }
        return filepath;
    }

    @PostMapping("/event_resist")
    public ResponseEntity<MovieVO> event_resist(@RequestBody MovieVO vo) {
//        int Movie_resist=movie_Image_Service.event_resist(vo);
//        if(Movie_resist==1) {
//            return new ResponseEntity<>(vo, HttpStatus.CREATED);
//        }else{
//            return new ResponseEntity<>(vo,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return null;
    }


}
