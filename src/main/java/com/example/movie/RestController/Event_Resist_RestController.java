package com.example.movie.RestController;

import com.example.movie.dto.ResponseDTO;
import com.example.movie.dto.mains.EventBoardDTO;
import com.example.movie.movie_image_service.ImageService;
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
import java.net.URLConnection;
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
    private ImageService movie_Image_Service;

    public String makeFolder() {
        filePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File file = new File(uploadPath + "/" + filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    @PostMapping(value = "/event_resist", consumes = "Multipart/form-data")
    public ResponseEntity<EventBoardDTO> eventVOBoardResponseEntity(@RequestParam("event_name") String event_name,
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
        EventBoardDTO vo = new EventBoardDTO();
        vo.setFilePath(filePath);
        vo.setEvent_name(event_name);
        vo.setUploadPaths(uploadPaths);
        vo.setSize(size);
        vo.setMovie_filename(movie_filename);
        vo.setResist_textarea(resist_textarea);
        vo.setMovie_resist_filePath(savePath);
        int movie_resist_people = movie_Image_Service.event_board_resist(vo);
        if (movie_resist_people == 1) {
            return ResponseEntity.ok(EventBoardDTO.builder().build());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/event_list_two")
    public ResponseEntity<ArrayList<ResponseDTO>> event_list(EventBoardDTO vo, Model model) {
        ArrayList<EventBoardDTO> event = movie_Image_Service.get_event_list(vo);
        ArrayList<ResponseDTO> Response = new ArrayList<>();
        for (EventBoardDTO event_two : event) {
            ResponseDTO responseItem = new ResponseDTO();
            responseItem.setMovie_filename(event_two.getMovie_filename());
            responseItem.setFilePath(event_two.getFilePath());
            responseItem.setImageUrl(event_two.getUploadPaths());
            String htmlContent = createEventHtml(event_two);
            responseItem.setHtmlContent(htmlContent);
            Response.add(responseItem);
            model.addAttribute("htmlContent", htmlContent);
        }
        return ResponseEntity.ok(Response);
    }

    private String createEventHtml(EventBoardDTO event_two) {
        String html = "      <div class=\"movie_event_notice\">" +
                "<div class=\"movie_image_notice\" id=\"movie_image_notice\">" +
                "</div>" +
                "<div class=\"movie_event_title\">" +
                event_two.getEvent_name() +
                "</div>" +
                "</div>";

        return html;
    }

    @GetMapping("/event_list_two/files/{filePath}/{movie_filename}")
    public ResponseEntity<Resource> event_image_list_saveFile(@PathVariable String filePath,
                                                              @PathVariable String movie_filename) throws IOException {
        String path = "classpath:/static/css/uploadImage/files/" + filePath + "/" + movie_filename;
        Resource resource = resourceLoader.getResource(path);
        String fileName = resource.getFilename();

        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
