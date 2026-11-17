package com.example.movie.RestController;

import com.example.movie.dto.mains.MovieDTO;
import com.example.movie.movie_image_service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/movie_resist")
public class MovieApi {

    @Value("${project.upload.path}")
    private String uploadPath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    @Qualifier("movie_Image_Service")
    private ImageService movie_Image_Service;

    String filePath = "";
    private Process logger;

    public String makeFolder() {
        filePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        File file = new File(uploadPath + '/' + filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    @PostMapping(value = "/upload_ok/movie_resist_two", consumes = "Multipart/form-data")
    public ResponseEntity<MovieDTO> movie_resist_two(@RequestParam("movie_title") String movie_title,
                                                     @RequestParam("file") MultipartFile movie_resist_file,
                                                     @RequestParam("movie_textarea") String movie_textarea,
                                                     @RequestParam("movie_filename") String movie_filename,
                                                     @RequestParam("uploadPaths") String uploadPaths) throws IOException {

        long size = movie_resist_file.getSize();
        String filePath = makeFolder();
        uploadPaths = uploadPath;
        String savePath = uploadPath + "/" + filePath + "/" + movie_filename;
        System.out.println("파일명:" + movie_filename);
        System.out.println("파일 사이즈:" + size);
        System.out.println("업로그 할 경로:" + savePath);

        File saveFile = new File(savePath);
        movie_resist_file.transferTo(saveFile);

        MovieDTO vo = new MovieDTO();
        vo.setFilePath(filePath);
        vo.setUploadPaths(uploadPaths);
        vo.setMovie_title(movie_title);
        vo.setMovie_filename(movie_filename);
        vo.setSize(size);
        vo.setMovie_resist_filePath(savePath);
        vo.setMovie_textarea(movie_textarea);
        int Movie_resist_people = movie_Image_Service.movie_resist(vo);

        return ResponseEntity.ok(MovieDTO.builder().build());
    }

    @GetMapping("/files/{filePaths}/{movie_filename_two}/{movietitles}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filePaths, @PathVariable String movie_filename_two, @PathVariable String movietitles) {
        System.out.println("finalFilename_one" + filePath);
        try {
            Resource resource = resourceLoader.getResource("classpath:/static/css/uploadImage/files/" + filePaths + "/" + movie_filename_two);
            String fileName = resource.getFilename();
            String mimeType = URLConnection.guessContentTypeFromName(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
