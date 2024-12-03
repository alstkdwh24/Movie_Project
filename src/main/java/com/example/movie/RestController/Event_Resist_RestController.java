package com.example.movie.RestController;

import com.example.movie.ResponseVO.ResponseVO;
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

import javax.servlet.http.HttpSession;
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

    public String UploadPaths;

    public String imageUrl;

    public String filePath;
    public String filenames;
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
    public ResponseEntity<ArrayList<ResponseVO>> event_list(EventVO_Board vo, Model model, HttpSession session) {

        ArrayList<EventVO_Board> get_event_list = movie_Image_Service.get_event_list(vo);
        ArrayList<ResponseVO> response=new ArrayList<>();
        model.addAttribute("get_event_list", get_event_list);
        for (EventVO_Board event:get_event_list) {
//            String firstFile = get_event_list.get(i).getFilePath() + "/" + get_event_list.get(i).getMovie_filename();
            String imageUrl=event.getUploadPaths()+"/"+event.getFilePath() + "/" + event.getMovie_filename();;
          String htmlContent=generateHtml(event);

            response.add(new ResponseVO(imageUrl, htmlContent));
        }
        session.setAttribute("get_event_list", get_event_list);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    private String generateHtml(EventVO_Board event) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"movie_event_notice\">\n")
                .append("    <div class=\"movie_image_notice\">\n")
                .append("        <img src=\"").append(event.getUploadPaths()).append("/").append(event.getFilePath()).append("/").append(event.getMovie_filename()).append("\" alt=\"\">\n")
                .append("    </div>\n")
                .append("    <div class=\"movie_event_title\">").append(event.getEvent_name()).append("</div>\n")
                .append("</div>");
        return html.toString();
    }

}
