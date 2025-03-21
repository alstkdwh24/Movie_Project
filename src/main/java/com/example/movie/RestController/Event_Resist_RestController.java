package com.example.movie.RestController;

import com.example.movie.commandVO.ResponseVO;
import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @GetMapping("/event_list_two")
    public ResponseEntity<ArrayList<ResponseVO>> event_list(EventVO_Board vo, Model model) {
        ArrayList<EventVO_Board> event=movie_Image_Service.get_event_list(vo);
        ArrayList<ResponseVO> Response=new ArrayList<>();
        for(EventVO_Board event_two:event){
            ResponseVO responseItem = new ResponseVO();
            responseItem.setMovie_filename(event_two.getMovie_filename());
            responseItem.setFilePath(event_two.getFilePath());
            responseItem.setImageUrl(event_two.getUploadPaths());
            String htmlContent = createEventHtml(event_two); // 메서드 이름 변경
            responseItem.setHtmlContent(htmlContent);
            Response.add(responseItem);
            model.addAttribute("htmlContent",htmlContent);

        }
        return ResponseEntity.ok(Response);
    }


    private String createEventHtml(EventVO_Board event_two){
        StringBuilder html=new StringBuilder();
        html.append("      <div class=\"movie_event_notice\">")
                .append("<div class=\"movie_image_notice\" id=\"movie_image_notice\">")
                .append("</div>")
                .append("<div class=\"movie_event_title\">")
                .append(event_two.getEvent_name())
                .append("</div>")
                .append("</div>");

        return html.toString();
    }

    @GetMapping("/event_list_two/files/{filePath}/{movie_filename}")
    public ResponseEntity<Resource> event_image_list_saveFile( @PathVariable String filePath,
                                                              @PathVariable String movie_filename) throws IOException {
        Path path= Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/"+ filePath + "/"+ movie_filename);
        File file= path.toFile();
        String mimeType= Files.probeContentType(path);
        MediaType mediaType=MediaType.parseMediaType(mimeType !=null?mimeType: "application/octet-stream");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getName() + "\"")
                .body(new org.springframework.core.io.FileSystemResource(file));

    }

}
