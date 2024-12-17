package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.DeliciousVO;
import com.example.movie.commandVO.MainsVO.Movie_gifticonVO;
import com.example.movie.commandVO.Response.Movie_gifticonVO_Responses;
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
public class Movie_gifticon_RestController {

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

    @PostMapping(value = "/movie_gift_card_resist", consumes = "Multipart/form-data")
    public ResponseEntity<Movie_gifticonVO> movie_gifticonVO(@RequestParam("Gifticon_name") String Gifticon_name,
                                                             @RequestParam("Gifticon_filename") String Gifticon_filename,
                                                             @RequestParam("resist_textarea") String resist_textarea,
                                                             @RequestParam("file") MultipartFile Gifticon_file,
                                                             @RequestParam("uploadPaths") String uploadPaths) throws IOException {


        long size = Gifticon_file.getSize();
        String filePath = makeFolder();
        uploadPaths = uploadPath;
        String savePath = uploadPath + "/" + filePath + "/" + Gifticon_filename;
        System.out.println("파일명:" + Gifticon_filename);

        File saveFile = new File(savePath);
        Gifticon_file.transferTo(saveFile);
        Movie_gifticonVO vo = new Movie_gifticonVO();
        vo.setMovie_filepath(filePath);
        vo.setGifticon_name(Gifticon_name);
        vo.setUploadPaths(uploadPaths);
        vo.setSize(size);
        vo.setGifticon_filename(Gifticon_filename);
        vo.setResist_textarea(resist_textarea);
        vo.setMovie_resist_filePath(savePath);
        int movie_gifticon_resist = movie_Image_Service.gifticon_resist(vo);
        if (movie_gifticon_resist == 1) {
            return ResponseEntity.ok(Movie_gifticonVO.builder().build());
        }else{
            return null;
        }

    }

    @GetMapping("/movie_gifticon_list")
    public ResponseEntity<ArrayList<Movie_gifticonVO_Responses>> movie_gifticon(Movie_gifticonVO vo, Movie_gifticonVO_Responses responses, Model model){
        ArrayList<Movie_gifticonVO> Gifticon_resist=movie_Image_Service.gifticon_select(vo);
        ArrayList<Movie_gifticonVO_Responses> Gifticon_Response=new ArrayList<>();

        for(Movie_gifticonVO gifticonVO:Gifticon_resist){
            Movie_gifticonVO_Responses movie_gifticonVO_responses=new Movie_gifticonVO_Responses();
            movie_gifticonVO_responses.setGifticon_filename(gifticonVO.getGifticon_filename());
            movie_gifticonVO_responses.setImageUrl(gifticonVO.getUploadPaths());
            movie_gifticonVO_responses.setGifticon_name(gifticonVO.getGifticon_name());
            movie_gifticonVO_responses.setFilePath(gifticonVO.getMovie_filepath());
            String htmlContent=createGifticon(gifticonVO);
            movie_gifticonVO_responses.setHtmlContent(htmlContent);

            Gifticon_Response.add(movie_gifticonVO_responses);
            System.out.println(htmlContent+"GifticonHtml25");
            model.addAttribute("GifticonHtml",htmlContent);
            System.out.println(gifticonVO.getGifticon_filename()+ "movie_gifticonVO_responses.setGifticon_name(gifticonVO.getGifticon_name());");
        }
        return ResponseEntity.ok(Gifticon_Response);
    }

    private String createGifticon(Movie_gifticonVO gifticonVO){
        StringBuilder html=new StringBuilder();
        html.append("<div class=\"ant_three\">")
                .append("<div class=\"contents_img\" id=\"contents_img\">")
                .append("</div>")
                .append("<div class=\"contents_ant\">")
                .append("<div class=\"ant_big_title\">")
                .append(gifticonVO.getGifticon_name())
                .append("</div>")
                .append("<div class=\"ant_big_title\">")
                .append(gifticonVO.getGifticon_name())
                .append("</div>").append("</div>").
                append("</div>");

        System.out.println(gifticonVO.getGifticon_name()+"gifticonVO.getGifticon_name()");

                return html.toString();
    }


    @GetMapping("/movie_gifticon_list/file/{filePath}/{Gifticon_filename}")
    public ResponseEntity<Resource> gifticon_img(@PathVariable String filePath, @PathVariable String Gifticon_filename) throws IOException {

        Path path= Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/"+ filePath + "/"+ Gifticon_filename);
        File file=path.toFile();
        String mimeType= Files.probeContentType(path);
        MediaType mediaType=MediaType.parseMediaType(mimeType !=null? mimeType:"application/octet-stream");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; Gifticon_filename=\""+ file.getName()+"\"")
                .body(new org.springframework.core.io.FileSystemResource(file));

    }

    @GetMapping("/gifticonVO_Responses_two_list")
    public ResponseEntity<Movie_gifticonVO_Responses> gifticonVO_Responses_two(Movie_gifticonVO vo){
        ArrayList<Movie_gifticonVO> Movie_gifticon_list=movie_Image_Service.Movie_gifticon_list(vo);
        ArrayList<Movie_gifticonVO_Responses> Movie_gifticonVO_Responses_list = new ArrayList<>();


        return null;
    }

}
