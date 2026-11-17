package com.example.movie.RestController;

import com.example.movie.dto.mains.TicketDTO;
import com.example.movie.dto.response.TicketResDTO;
import com.example.movie.movie_image_service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;
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
public class TicketApi {
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

    @PostMapping(value = "/movie_ticket_resist", consumes = "Multipart/form-data")
    public ResponseEntity<TicketDTO> movie_ticket_resist(@RequestParam("movie_ticket_name") String movie_ticket_name,
                                                               @RequestParam("movie_ticket_filename") String movie_ticket_filename,
                                                               @RequestParam("movie_ticket_file") MultipartFile movie_ticket_file,
                                                               @RequestParam("resist_textarea") String resist_textarea,
                                                               @RequestParam("uploadPaths") String uploadPaths) throws IOException {

        long size = movie_ticket_file.getSize();
        String filePath = makeFolder();
        uploadPaths = uploadPath;
        String savePath = uploadPath + "/" + filePath + "/" + movie_ticket_filename;

        File saveFile = new File(savePath);
        movie_ticket_file.transferTo(saveFile);
        TicketDTO vo = new TicketDTO();
        vo.setResist_textarea(resist_textarea);
        vo.setSize(size);
        vo.setUploadPaths(uploadPaths);
        vo.setMovie_filepath(savePath);
        vo.setMovie_ticket_filename(movie_ticket_filename);
        vo.setMovie_ticket_name(movie_ticket_name);
        vo.setFilePath(filePath);
        int movie_ticket_resist = movie_Image_Service.movie_ticket_resist(vo);

        if (movie_ticket_resist == 1) {
            return ResponseEntity.ok(TicketDTO.builder().build());
        } else {
            return null;
        }
    }

    @GetMapping("/movie_ticket_resist_list")
    public ResponseEntity<ArrayList<TicketResDTO>> movie_ticket_resist_list(TicketDTO vo, Model model) {
        ArrayList<TicketDTO> Movie_ticket_list = movie_Image_Service.movie_ticket_resist_list(vo);
        ArrayList<TicketResDTO> movie_ticketVO_Responses_two = new ArrayList<>();

        for (TicketDTO movie_ticket_list : Movie_ticket_list) {
            TicketResDTO movie_ticketVO_responses = new TicketResDTO();
            movie_ticketVO_responses.setMovie_ticket_url(movie_ticket_list.getUploadPaths());
            movie_ticketVO_responses.setMovie_ticket_filename(movie_ticket_list.getMovie_ticket_filename());
            movie_ticketVO_responses.setMovie_filepath(movie_ticket_list.getMovie_filepath());
            movie_ticketVO_responses.setMovie_ticket_name(movie_ticket_list.getMovie_ticket_name());
            movie_ticketVO_responses.setFilePath(movie_ticket_list.getFilePath());
            String movie_ticket_htmlContent = create_ticket(movie_ticket_list);
            movie_ticketVO_responses.setMovie_ticket_htmlContent(movie_ticket_htmlContent);
            movie_ticketVO_Responses_two.add(movie_ticketVO_responses);

            model.addAttribute("movie_ticket_htmlContent", movie_ticket_htmlContent);
        }

        return ResponseEntity.ok(movie_ticketVO_Responses_two);
    }

    private String create_ticket(TicketDTO movie_ticket_list) {
        String movie_ticket_htmlContent = "<div class=\"ant_three\">" +
                "<div class=\"contents_img\" id=\"contents_img_two\">" +
                "</div>" +
                "<div class=\"contents_ant\">" +
                "<div class=\"ant_big_title\">" +
                movie_ticket_list.getMovie_ticket_name() +
                "</div>" +
                "<div class=\"ant_big_title\">" +
                movie_ticket_list.getResist_textarea() +
                "</div>" +
                "</div>" +
                "</div>";

        return movie_ticket_htmlContent;
    }

    @GetMapping("/movie_ticket_resist_list/files/{filePath}/{movie_ticket_filename}")
    public ResponseEntity<Resource> movie_ticket_image(@PathVariable String filePath, @PathVariable String movie_ticket_filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/css/uploadImage/files/" + filePath + "/" + movie_ticket_filename);
        String fileName = resource.getFilename();
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream\"");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(resource.getFilename(), StandardCharsets.UTF_8).build().toString())
                .body(resource);
    }

    @GetMapping("/popcon_store/movie_ticket_resist_list_two")
    public ResponseEntity<ArrayList<TicketResDTO>> movie_ticket_resist_list_two(TicketDTO vo, Model model) {
        ArrayList<TicketDTO> movie_ticketVOS = movie_Image_Service.movie_ticketVOS_list(vo);
        ArrayList<TicketResDTO> movie_ticketVO_responses = new ArrayList<>();
        for (TicketDTO movie_ticketVO_two : movie_ticketVOS) {
            TicketResDTO movie_ticketVO_responses1 = new TicketResDTO();
            movie_ticketVO_responses1.setFilePath(movie_ticketVO_two.getFilePath());
            movie_ticketVO_responses1.setMovie_ticket_filename(movie_ticketVO_two.getMovie_ticket_filename());
            movie_ticketVO_responses1.setMovie_ticket_name(movie_ticketVO_two.getMovie_ticket_name());
            movie_ticketVO_responses1.setMovie_filepath(movie_ticketVO_two.getMovie_filepath());
            movie_ticketVO_responses1.setFilePath(movie_ticketVO_two.getFilePath());

            String movie_ticket_htmlContent = movie_ticket_htmlContent_createElement(movie_ticketVO_two);
            movie_ticketVO_responses1.setMovie_ticket_htmlContent(movie_ticket_htmlContent);
            movie_ticketVO_responses.add(movie_ticketVO_responses1);

            model.addAttribute("movie_ticket_htmlContent", movie_ticket_htmlContent);
        }
        return ResponseEntity.ok(movie_ticketVO_responses);
    }

    private String movie_ticket_htmlContent_createElement(TicketDTO movie_ticketVO_two) {
        String movie_ticket_htmlContent = "<div class=\"body_gift\">" +
                "<div class=\"gift_img_two\">" +
                "</div>" +
                "<div class=\"gift_title\">" +
                "<div class=\"gift_title_big\">" +
                movie_ticketVO_two.getMovie_ticket_name() +
                "</div>" +
                "<div class=\"gift_title_small\">" +
                movie_ticketVO_two.getResist_textarea() +
                "</div>" +
                "</div>" +
                "</div>";
        return movie_ticket_htmlContent;
    }

    @GetMapping("/popcon_store/movie_ticket_resist_list_two/files/{filePath_ticket}/{movie_ticket_filename_array}")
    public ResponseEntity<Resource> movie_ticket_filename_array(@PathVariable String filePath_ticket, @PathVariable String movie_ticket_filename_array) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/css/uploadImage/files/" + filePath_ticket + "/" + movie_ticket_filename_array);
        String fileName = resource.getFilename();
        String mimeType = URLConnection.guessContentTypeFromName(fileName);

        MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream\"");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(resource.getFilename(), StandardCharsets.UTF_8).build().toString())
                .body(resource);
    }
}
