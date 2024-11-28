package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/movie_resist")
public class Event_Resist_RestController {

    @Value("${project.upload.path}")
    private String uploadPath;

    @Autowired
    private ResourceLoader resourceLoader;

    String filePath = "";


    @Autowired
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_Image_Service;

    public String makeFolder() {
        filePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File file = new File(uploadPath + "/" + filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    @PostMapping(value = "/event_resist", consumes = "Multipart/form-data")
    public ResponseEntity<EventVO_Board> eventVOBoardResponseEntity(@RequestParam("event_name")String event_name,
                                                                    @RequestParam("movie_filename") String movie_filename,
                                                                    @RequestParam("movie_filepath") String movie_filepath,
                                                                    @RequestParam("resist_textarea") String resist_textarea,
                                                                    @RequestParam("file") MultipartFile event_resist_file){

        long size=event_resist_file.getSize();
        String filePath=makeFolder();

        return ResponseEntity.ok(EventVO_Board.builder().build());
    }
}
