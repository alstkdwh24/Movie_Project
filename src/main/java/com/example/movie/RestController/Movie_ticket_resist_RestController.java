package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.Movie_gifticonVO;
import com.example.movie.commandVO.MainsVO.Movie_ticketVO;
import com.example.movie.commandVO.Response.Movie_gifticonVO_Responses;
import com.example.movie.commandVO.Response.Movie_ticketVO_Responses;
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
public class Movie_ticket_resist_RestController {
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


    @PostMapping(value = "/movie_ticket_resist",  consumes = "Multipart/form-data")
    public ResponseEntity<Movie_ticketVO> movie_ticket_resist(@RequestParam("movie_ticket_name") String movie_ticket_name,
                                                              @RequestParam("movie_ticket_filename") String movie_ticket_filename,
                                                              @RequestParam("movie_ticket_file")MultipartFile movie_ticket_file,
                                                              @RequestParam("resist_textarea") String resist_textarea,
                                                              @RequestParam("uploadPaths") String uploadPaths) throws IOException {

        long size=movie_ticket_file.getSize();
        String filePath=makeFolder();
        uploadPaths = uploadPath;
        String savePath=uploadPath+ "/" +filePath+ "/" + movie_ticket_filename;

        File saveFile=new File(savePath);
        movie_ticket_file.transferTo(saveFile);
        Movie_ticketVO vo =new Movie_ticketVO();
        vo.setResist_textarea(resist_textarea);
        vo.setSize(size);
        vo.setUploadPaths(uploadPaths);
        vo.setMovie_filepath(savePath);
        vo.setMovie_ticket_filename(movie_ticket_filename);
        vo.setMovie_ticket_name(movie_ticket_name);
        vo.setFilePath(filePath);
        int movie_ticket_resist=movie_Image_Service.movie_ticket_resist(vo);

        if (movie_ticket_resist == 1) {
            return ResponseEntity.ok(Movie_ticketVO.builder().build());
        }else{
            return null;
        }
    }

    @GetMapping("/movie_ticket_resist_list")
    public ResponseEntity<ArrayList<Movie_ticketVO_Responses>> movie_ticket_resist_list(Movie_ticketVO vo, Model model){
        ArrayList<Movie_ticketVO> Movie_ticket_list=movie_Image_Service.movie_ticket_resist_list(vo);
        ArrayList<Movie_ticketVO_Responses> movie_ticketVO_Responses_two=new ArrayList<>();

        for(Movie_ticketVO movie_ticket_list: Movie_ticket_list){
            Movie_ticketVO_Responses movie_ticketVO_responses=new Movie_ticketVO_Responses();
            movie_ticketVO_responses.setMovie_ticket_url(movie_ticket_list.getUploadPaths());
            movie_ticketVO_responses.setMovie_ticket_filename(movie_ticket_list.getMovie_ticket_filename());
            movie_ticketVO_responses.setMovie_filepath(movie_ticket_list.getMovie_filepath());
            movie_ticketVO_responses.setMovie_ticket_name(movie_ticket_list.getMovie_ticket_name());
            movie_ticketVO_responses.setFilePath(movie_ticket_list.getFilePath());
            String movie_ticket_htmlContent=create_ticket(movie_ticket_list);
            movie_ticketVO_responses.setMovie_ticket_htmlContent(movie_ticket_htmlContent);
            movie_ticketVO_Responses_two.add(movie_ticketVO_responses);

            model.addAttribute("movie_ticket_htmlContent",movie_ticket_htmlContent);


        }


        return ResponseEntity.ok(movie_ticketVO_Responses_two);
    }

    private String create_ticket(Movie_ticketVO movie_ticket_list){
        StringBuilder movie_ticket_htmlContent=new StringBuilder();

        movie_ticket_htmlContent.append("<div class=\"ant_three\">")
                .append("<div class=\"contents_img\" id=\"contents_img_two\">")
                .append("</div>")
                .append("<div class=\"contents_ant\">")
                .append("<div class=\"ant_big_title\">")
                .append(movie_ticket_list.getMovie_ticket_name())
                .append("</div>")
                .append("<div class=\"ant_big_title\">")
                .append(movie_ticket_list.getResist_textarea())
                .append("</div>")
                .append("</div>")
                .append("</div>");

    return movie_ticket_htmlContent.toString();
    }

    @GetMapping("/movie_ticket_resist_list/files/{filePath}/{movie_ticket_filename}")
    public ResponseEntity<Resource> movie_ticket_image(@PathVariable String filePath, @PathVariable String movie_ticket_filename) throws IOException {

        Path path= Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/"+ filePath + "/"+ movie_ticket_filename);
        File file=path.toFile();
        String mimeType= Files.probeContentType(path);
        MediaType mediaType=MediaType.parseMediaType(mimeType !=null? mimeType:"application/octet-stream\"");


        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getName() + "\"")
                .body(new org.springframework.core.io.FileSystemResource(file));

    }

    @GetMapping("/popcon_store/movie_ticket_resist_list_two")
    public ResponseEntity<ArrayList<Movie_ticketVO_Responses>> movie_ticket_resist_list_two(Movie_ticketVO vo, Model model){
        ArrayList<Movie_ticketVO> movie_ticketVOS=movie_Image_Service.movie_ticketVOS_list(vo);
        ArrayList<Movie_ticketVO_Responses> movie_ticketVO_responses=new ArrayList<>();
        for(Movie_ticketVO movie_ticketVO_two: movie_ticketVOS){
            Movie_ticketVO_Responses movie_ticketVO_responses1=new Movie_ticketVO_Responses();
            movie_ticketVO_responses1.setFilePath(movie_ticketVO_two.getFilePath());
            movie_ticketVO_responses1.setMovie_ticket_filename(movie_ticketVO_two.getMovie_ticket_filename());
            movie_ticketVO_responses1.setMovie_ticket_name(movie_ticketVO_two.getMovie_ticket_name());
            movie_ticketVO_responses1.setMovie_filepath(movie_ticketVO_two.getMovie_filepath());
            movie_ticketVO_responses1.setFilePath(movie_ticketVO_two.getFilePath());

            String movie_ticket_htmlContent=movie_ticket_htmlContent_createElement(movie_ticketVO_two);
            movie_ticketVO_responses1.setMovie_ticket_htmlContent(movie_ticket_htmlContent);
            movie_ticketVO_responses.add(movie_ticketVO_responses1);

            model.addAttribute("movie_ticket_htmlContent",movie_ticket_htmlContent);
        }
        return ResponseEntity.ok(movie_ticketVO_responses);
    }

    private String movie_ticket_htmlContent_createElement(Movie_ticketVO movie_ticketVO_two){
        StringBuilder movie_ticket_htmlContent=new StringBuilder();
        movie_ticket_htmlContent.append("<div class=\"body_gift\">")
                .append("<div class=\"gift_img_two\">")
                .append("</div>")
                .append("<div class=\"gift_title\">")
                .append("<div class=\"gift_title_big\">")
                .append(movie_ticketVO_two.getMovie_ticket_name())
                .append("</div>")
                .append("<div class=\"gift_title_small\">")
                .append(movie_ticketVO_two.getResist_textarea())
                .append("</div>")
                .append("</div>")
                .append("</div>");
        return movie_ticket_htmlContent.toString();
    }

    @GetMapping("/popcon_store/movie_ticket_resist_list_two/files/{filePath_ticket}/{movie_ticket_filename_array}")
    public ResponseEntity<Resource> movie_ticket_filename_array(@PathVariable String filePath_ticket, @PathVariable String movie_ticket_filename_array) throws IOException {

        Path path= Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/"+ filePath_ticket + "/"+ movie_ticket_filename_array);
        File file=path.toFile();
        String mimeType= Files.probeContentType(path);
        MediaType mediaType=MediaType.parseMediaType(mimeType !=null? mimeType:"application/octet-stream\"");


        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; movie_ticket_filename=\"" + file.getName() + "\"")
                .body(new org.springframework.core.io.FileSystemResource(file));
    }
}
