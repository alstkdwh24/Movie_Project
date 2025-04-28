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
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String html = "<div class=\"ant_three\">" +
                "<div class=\"contents_img\" id=\"contents_img\">" +
                "</div>" +
                "<div class=\"contents_ant\">" +
                "<div class=\"ant_big_title\">" +
                gifticonVO.getGifticon_name() +
                "</div>" +
                "<div class=\"ant_big_title\">" +
                gifticonVO.getGifticon_name() +
                "</div>" + "</div>" +
                "</div>";

        System.out.println(gifticonVO.getGifticon_name()+"gifticonVO.getGifticon_name()");

                return html;
    }


    @GetMapping("/movie_gifticon_list/file/{filePath}/{Gifticon_filename}")
    public ResponseEntity<Resource> gifticon_img(@PathVariable String filePath, @PathVariable String Gifticon_filename) throws IOException {

        String path = "classpath:/static/css/uploadImage/files/" + filePath + "/" + Gifticon_filename;

//            Path path = Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/" + filePath_two + "/" + gifticon_filename);
        Resource resource= resourceLoader.getResource(path);
        String fileName = resource.getFilename();

        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        MediaType mediaType=MediaType.parseMediaType(mimeType !=null? mimeType:"application/octet-stream");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; Gifticon_filename=\""+ resource.getFilename()+"\"")
                .body(resource);

    }

    @GetMapping("/gifticonVO_Responses_two_list")
    public ResponseEntity<ArrayList<Movie_gifticonVO_Responses>> gifticonVO_Responses_two(Movie_gifticonVO vo, Model model){
        ArrayList<Movie_gifticonVO> Movie_gifticon_list=movie_Image_Service.Movie_gifticon_list(vo);
        ArrayList<Movie_gifticonVO_Responses> Movie_gifticonVO_Responses_list = new ArrayList<>();
        for(Movie_gifticonVO movie_gifticon : Movie_gifticon_list ){
            Movie_gifticonVO_Responses movie_gifticonVO_responses=new Movie_gifticonVO_Responses();
            movie_gifticonVO_responses.setFilePath(movie_gifticon.getMovie_filepath());
            movie_gifticonVO_responses.setGifticon_filename(movie_gifticon.getGifticon_filename());
            movie_gifticonVO_responses.setGifticon_name(movie_gifticon.getGifticon_name());
            movie_gifticonVO_responses.setImageUrl(movie_gifticon.getUploadPaths());

            String GifticonHtml=Gifticon_createElement(movie_gifticon);
            movie_gifticonVO_responses.setImageUrl(GifticonHtml);

            model.addAttribute("GifticonHtml",GifticonHtml);

            Movie_gifticonVO_Responses_list.add(movie_gifticonVO_responses);
        }

        return ResponseEntity.ok(Movie_gifticonVO_Responses_list);
    }
        private String Gifticon_createElement(Movie_gifticonVO movie_gifticonVO){
            String GifticonHtml = " <div class=\"body_gift\">" +
                    "<div class=\"gift_img\">" +
                    "</div>" +
                    "<div class=\"gift_title\">" +
                    "<div class=\"gift_title_big\">" +
                    movie_gifticonVO.getGifticon_name() +
                    "</div>" +
                    "<div class=\"gift_title_small\">" +
                    movie_gifticonVO.getResist_textarea() +
                    "</div>" +
                    "</div>";

            return GifticonHtml;
        }

        @GetMapping("/gifticonVO_Responses_two_list/files/{filePath_two}/{gifticon_filename}")
        public ResponseEntity<Resource> gifticonVO_Resource(@PathVariable String filePath_two, @PathVariable String gifticon_filename) throws IOException {
            String path = "classpath:/static/css/uploadImage/files/" + filePath_two + "/" + gifticon_filename;

//            Path path = Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/" + filePath_two + "/" + gifticon_filename);
            Resource resource= resourceLoader.getResource(path);
            // 파일 이름 가져오기
            String fileName = resource.getFilename();

            // 확장자 기반으로 MIME 타입 설정
            String mimeType = URLConnection.guessContentTypeFromName(fileName);
//            MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; delicious_filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        }

}
