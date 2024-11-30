package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    public ResponseEntity<EventVO_Board> eventVOBoardResponseEntity(@RequestParam("event_name") String event_name,
                                                                    @RequestParam("movie_filename") String movie_filename,
                                                                    @RequestParam("resist_textarea") String resist_textarea,
                                                                    @RequestParam("file") MultipartFile event_resist_file,
            @RequestParam("uploadPaths") String uploadPaths) throws IOException {

        long size = event_resist_file.getSize();
        String filePath = makeFolder();
        uploadPaths = uploadPath;
        String savePath = uploadPath + "/" + filePath + "/" + movie_filename;
        System.out.println("파일명:" + movie_filename);

        File saveFile = new File(savePath);
        event_resist_file.transferTo(saveFile);
        EventVO_Board vo = new EventVO_Board();
        vo.setFilePath(filePath);
        vo.setEvent_name(event_name);
        vo.setUploadPaths(uploadPaths);
        vo.setSize(size);
        vo.setMovie_filename(movie_filename);
        vo.setResist_textarea(resist_textarea);
        vo.setMovie_resist_filePath(savePath);
        int movie_resist_people = movie_Image_Service.event_board_resist(vo);
        if (movie_resist_people == 1) {
            return ResponseEntity.ok(EventVO_Board.builder().build());

        } else {
            return ResponseEntity.internalServerError().build(); // 500 Internal Server Error 반환;
        }
    }
    @GetMapping("/event_list")
    public ResponseEntity<ArrayList<EventVO_Board>> event_list(EventVO_Board vo, Model model){

        ArrayList<EventVO_Board> get_event_list=movie_Image_Service.get_event_list(vo);
        model.addAttribute("get_event_list", get_event_list);
        return new ResponseEntity<>(get_event_list, HttpStatus.CREATED);
    }

}
