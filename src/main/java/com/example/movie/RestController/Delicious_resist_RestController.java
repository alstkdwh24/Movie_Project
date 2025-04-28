package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.DeliciousVO;
import com.example.movie.commandVO.MainsVO.Movie_ticketVO;
import com.example.movie.commandVO.Response.DeliciousVO_Responses;
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
public class Delicious_resist_RestController {

    @Value("${project.upload.path}")
    private String uploadPath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_Image_Service;

    String filePath = "";
    private Process logger;

    public String makeFolder() {
        filePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        File file = new File(uploadPath + '/' + filePath);
        if (!file.exists()) { //해당 파일이 있으면 true, 없으면!
            file.mkdirs();
        }
        return filePath;
    }

    @PostMapping(value = "/DeliciousVO_resist", consumes = "Multipart/form-data")
    public ResponseEntity<DeliciousVO> DeliciousVO_image(@RequestParam("delicious_name") String delicious_name,
                                                         @RequestParam("delicious_filename") String delicious_filename,
                                                         @RequestParam("resist_textarea") String resist_textarea,
                                                         @RequestParam("delicious_file") MultipartFile delicious_file,
                                                         @RequestParam("uploadPaths") String uploadPaths) throws IOException {

        long size = delicious_file.getSize();
        String filePath = makeFolder();
        uploadPaths = uploadPath;
        String savePath = uploadPath + "/" + filePath + "/" + delicious_filename;
        System.out.println("파일명:" + delicious_filename);

        File saveFile = new File(savePath);
        delicious_file.transferTo(saveFile);
        DeliciousVO vo = new DeliciousVO();
        vo.setDelicious_filename(delicious_filename);
        vo.setDelicious_name(delicious_name);
        vo.setSize(size);
        vo.setUploadPaths(uploadPaths);
        vo.setResist_textarea(resist_textarea);
        vo.setMovie_resist_filePath(savePath);
        vo.setFilePath(filePath);

        int DeliciousVO_image = movie_Image_Service.DeliciousVO_image(vo);

        if (DeliciousVO_image == 1) {
            return ResponseEntity.ok(DeliciousVO.builder().build());

        } else {
            return null;
        }


    }

    @GetMapping("/DeliciousVO_list")
    public ResponseEntity<ArrayList<DeliciousVO_Responses>> DeliciousVO_Responses_two(DeliciousVO vo, Model model) {
        ArrayList<DeliciousVO> deliciousVO_list = movie_Image_Service.deliciousVO_list(vo);
        ArrayList<DeliciousVO_Responses> deliciousVO_Responses_list = new ArrayList<>();

        for (DeliciousVO deliciousVO_list_two : deliciousVO_list) {
            DeliciousVO_Responses deliciousVO_Responses = new DeliciousVO_Responses();
            deliciousVO_Responses.setFilePath(deliciousVO_list_two.getFilePath());
            deliciousVO_Responses.setDelicious_filename(deliciousVO_list_two.getDelicious_filename());
            deliciousVO_Responses.setDelicious_name(deliciousVO_list_two.getDelicious_name());

            String delicious_htmlContent = create_delicious_htmlContent(deliciousVO_list_two);

            deliciousVO_Responses.setDelicious_htmlContent(delicious_htmlContent);

            deliciousVO_Responses_list.add(deliciousVO_Responses);

            model.addAttribute("delicious_htmlContent", delicious_htmlContent);


        }


        return ResponseEntity.ok(deliciousVO_Responses_list);
    }

    private String create_delicious_htmlContent(DeliciousVO deliciousVO_list_two) {


        String delicious_htmlContent = "<div class=\"ant_three\" >" +
                "<div class=\"contents_img\" id=\"contents_img_four\">" +
                "</div>" +
                "<div class=\"contents_ant\">" +
                "<div class=\"ant_big_title\">" +
                deliciousVO_list_two.getDelicious_name() +
                "</div>" +
                "<div class=\"ant_big_title\">" +
                deliciousVO_list_two.getDelicious_name() +
                "</div>" +
                "</div>" +
                "</div>";

        return delicious_htmlContent;
    }


    @GetMapping("/DeliciousVO_list/files/{filePath}/{delicious_filename}")
    public ResponseEntity<Resource> delicious_image(@PathVariable String filePath, @PathVariable String delicious_filename) throws IOException {

        Resource resource= resourceLoader.getResource("classpath:/static/css/uploadImage/files/" + filePath + "/"+ delicious_filename);
        String fileName = resource.getFilename();
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; delicious_filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/DeliciousVO_list_two")
    public ResponseEntity<ArrayList<DeliciousVO_Responses>> DeliciousVO_Responses_list(DeliciousVO vo, Model model) {
        ArrayList<DeliciousVO> delicious_list = movie_Image_Service.deliciousVO_two_list(vo);
        ArrayList<DeliciousVO_Responses> Delicious_Responses = new ArrayList<>();

        for (DeliciousVO Delicious_list_two : delicious_list) {

            DeliciousVO_Responses deliciousVO_responses = new DeliciousVO_Responses();
            deliciousVO_responses.setDelicious_url(Delicious_list_two.getUploadPaths());
            deliciousVO_responses.setDelicious_number(Delicious_list_two.getDelicious_number());
            deliciousVO_responses.setDelicious_name(Delicious_list_two.getDelicious_name());
            deliciousVO_responses.setDelicious_filename(Delicious_list_two.getDelicious_filename());
            deliciousVO_responses.setFilePath(Delicious_list_two.getFilePath());

            String deliciousVO_response_two_htmlContent = deliciousVO_html_createElement(Delicious_list_two);
            deliciousVO_responses.setDelicious_htmlContent(deliciousVO_response_two_htmlContent);
            Delicious_Responses.add(deliciousVO_responses);

            model.addAttribute("deliciousVO_response_two_htmlContent", deliciousVO_response_two_htmlContent);
        }
        return ResponseEntity.ok(Delicious_Responses);
    }


    private String deliciousVO_html_createElement(DeliciousVO Delicious_list_two) {


        String deliciousVO_response_two_htmlContent = "<div class=\"popcorn_pakage\">" +
                "<div class=\"pakage_img\" id=\"pakage_imgs\">" +
                "</div>" +
                "<div class=\"pakage_title\">" +
                "<div class=\"pakage_title_big\">" +
                "<h2>" +
                Delicious_list_two.getDelicious_name() +
                "</h2>" +
                "</div>" +
                "<div class=\"pakage_title_small\">" +
                Delicious_list_two.getResist_textarea() +
                "<h5>" +
                "</h5></div>" +
                "</div>" +
                "</div>";


        return deliciousVO_response_two_htmlContent;
    }


    @GetMapping("DeliciousVO_list_three")
    public ResponseEntity<ArrayList<DeliciousVO_Responses>> DeliciousVO_list_three(DeliciousVO vo, Model model) {
        ArrayList<DeliciousVO> Delicious_list = movie_Image_Service.deliciousVO_two_list(vo);
        ArrayList<DeliciousVO_Responses> Delicious_Responses = new ArrayList<>();

        for (DeliciousVO Delicious_list_two : Delicious_list) {

            DeliciousVO_Responses deliciousVO_responses = new DeliciousVO_Responses();
            deliciousVO_responses.setDelicious_url(Delicious_list_two.getUploadPaths());
            deliciousVO_responses.setDelicious_number(Delicious_list_two.getDelicious_number());
            deliciousVO_responses.setDelicious_name(Delicious_list_two.getDelicious_name());
            deliciousVO_responses.setDelicious_filename(Delicious_list_two.getDelicious_filename());
            deliciousVO_responses.setFilePath(Delicious_list_two.getFilePath());

            String deliciousVO_response_two_htmlContent = deliciousVO_html_createElement(Delicious_list_two);
            deliciousVO_responses.setDelicious_htmlContent(deliciousVO_response_two_htmlContent);
            Delicious_Responses.add(deliciousVO_responses);

            model.addAttribute("deliciousVO_response_two_htmlContent", deliciousVO_response_two_htmlContent);
        }
        return ResponseEntity.ok(Delicious_Responses);


    }

    @GetMapping("/DeliciousVO_list_two/files/{filePath}/{delicious_filename}")
    public ResponseEntity<Resource> delicious_image_two(@PathVariable String filePath, @PathVariable String delicious_filename) throws IOException {

        Resource resource= resourceLoader.getResource("classpath:/static/css/uploadImage/files/" + filePath + "/"+ delicious_filename);

//            Path path = Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/" + filePath_two + "/" + gifticon_filename);
        String fileName=resource.getFilename();
        String mimeType = URLConnection.guessContentTypeFromName(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; delicious_filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/DeliciousVO_list_three/files/{filePath}/{delicious_filename}")
    public ResponseEntity<Resource> delicious_image_three(@PathVariable String filePath, @PathVariable String delicious_filename) throws IOException {

        Resource resource= resourceLoader.getResource("classpath:/static/css/uploadImage/files/" + filePath + "/"+ delicious_filename);
        // 파일 이름 가져오기
        String fileName = resource.getFilename();

        // 확장자 기반으로 MIME 타입 설정
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; delicious_filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
//코드 보관
//    @GetMapping("/DeliciousVO_list_three/files/{filePath}/{delicious_filename}")
//    public ResponseEntity<Resource> delicious_image_three(@PathVariable String filePath, @PathVariable String delicious_filename) throws IOException {
//
//        Path path = Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/" + filePath + "/" + delicious_filename);
//        File file = path.toFile();
//        String mimeType = Files.probeContentType(path);
//        MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream");
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; delicious_filename=\"" + file.getName() + "\"")
//                .body(new org.springframework.core.io.FileSystemResource(file));
//    }

}
