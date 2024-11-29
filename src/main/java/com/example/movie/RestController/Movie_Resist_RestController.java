package com.example.movie.RestController;

import com.example.movie.ImageProperties.ImageProperties;
import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/movie_resist")
public class Movie_Resist_RestController {

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


    @PostMapping(value = "/upload_ok/movie_resist_two", consumes = "Multipart/form-data")
    public ResponseEntity<MovieVO> movie_resist_two(@RequestParam("movie_title") String movie_title,
                                                    @RequestParam("file") MultipartFile movie_resist_file
            , @RequestParam("movie_textarea") String movie_textarea,
                                                    @RequestParam("movie_filename") String movie_filename,
                                                    @RequestParam("uploadPaths") String uploadPaths) throws IOException {

        long size = movie_resist_file.getSize();
//        String uuid=UUID.randomUUID().toString();
        String filePath = makeFolder();
        uploadPaths = uploadPath;
        String savePath = uploadPath + "/" + filePath + "/" + movie_filename;
//        String name=uuid+movie_filename;
//        System.out.println("savePath_two:"+savePath_two);
        System.out.println("파일명:" + movie_filename); //원본파일명 DB저장
        System.out.println("파일 사이즈:" + size); //폴더명 DB저장
//        System.out.println("파일 구분:" + uuid); //파일 구분 DB저장
        System.out.println("업로그 할 경로:" + savePath); //업로드할 경로 저장


        File saveFile = new File(savePath);

        movie_resist_file.transferTo(saveFile);//업로드 바로
        System.out.println();
        MovieVO vo = new MovieVO();
        vo.setFilePath(filePath);
        vo.setUploadPaths(uploadPaths);
        vo.setMovie_title(movie_title);
        vo.setMovie_filename(movie_filename);
//        vo.setUuid(uuid);
        vo.setSize(size);
        vo.setMovie_resist_filePath(savePath);
        vo.setMovie_textarea(movie_textarea);
        int Movie_resist_people = movie_Image_Service.movie_resist(vo);

        return ResponseEntity.ok(MovieVO.builder().build());
    }



    @GetMapping("/files/{filePaths}/{movie_filename_two}/{movietitles}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filePaths, @PathVariable String movie_filename_two, @PathVariable String movietitles) {
        System.out.println("finalFilename_one"+ filePath);
        try {
            Path path = Path.of("C:/Users/alstk/2course/JAVA/portfolio_project/movie_resist/files/" + filePaths + "/" + movie_filename_two);
            File file = path.toFile();
            String mimeType = Files.probeContentType(path);
            MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream");

            return ResponseEntity.ok()
                    .contentType(mediaType) // 이미지 형식에 맞게 설정
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(new org.springframework.core.io.FileSystemResource(file));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().build(); // 500 Internal Server Error 반환
        }

    }
}

//    @PostMapping(value = "/event_resist", consumes = "multipart/form-data")
//    public ResponseEntity<EventVO_Board> event_resist(@RequestParam("event_name") String eventName,
//                                                      @RequestParam("resist_textarea") String resistText, @RequestParam("movie_filename") MultipartFile file) {
//        String originName = file.getOriginalFilename();
//
//        originName = originName.substring(originName.lastIndexOf("\\") + 1);
//
//        long size = file.getSize();
//        // 동일한 파일로 업로드가 되면 덮어지기 때문에, 랜덤한 이름을 생성해야 한다.
//        String uuid = UUID.randomUUID().toString();
//
//        //날짜별로 폴더 생성
//        String filepath = makeFolder();
//
//        String savePath = uploadPath + "/" + filepath + '/' + uuid + "_" + originName;
//        System.out.println("파일명:" + originName); //원본파일명 DB저장
//        System.out.println("파일 사이즈:" + size); //폴더명 DB저장
//        System.out.println("파일 구분:" + uuid); //파일 구분 DB저장
//        System.out.println("업로그 할 경로:" + savePath); //업로드할 경로 저장
//
//
//        EventVO_Board vo = new EventVO_Board();
//        vo.setEvent_name(eventName);
//        vo.setResist_textarea(resistText);
//        vo.setMovie_filename(uuid + "_" + originName); // 저장된 파일 이름
//        vo.setFileSize(size); // 파일 크기
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
//    }
//
//    @PostMapping(value = "/movie_resist", consumes = "multipart/form-data")
//    public ResponseEntity<MovieVO> event_resist(@RequestParam("movie_title") String movie_title,
//                                                      @RequestParam("movie_textarea") String movie_textarea, @RequestParam("movie_filename") MultipartFile file) {
//        String originName = file.getOriginalFilename();
//
//        originName = originName.substring(originName.lastIndexOf("\\") + 1);
//
//        long size = file.getSize();
//        // 동일한 파일로 업로드가 되면 덮어지기 때문에, 랜덤한 이름을 생성해야 한다.
//        String uuid = UUID.randomUUID().toString();
//
//        //날짜별로 폴더 생성
//        String filepath = makeFolder();
//
//        String savePath = uploadPath + "/" + filepath + '/' + uuid + "_" + originName;
//        System.out.println("파일명:" + originName); //원본파일명 DB저장
//        System.out.println("파일 사이즈:" + size); //폴더명 DB저장
//        System.out.println("파일 구분:" + uuid); //파일 구분 DB저장
//        System.out.println("업로그 할 경로:" + savePath); //업로드할 경로 저장
//
//
//        MovieVO vo = new MovieVO();
//        vo.setMovie_title(movie_title);
//        vo.setMovie_textarea(movie_textarea);
//        vo.setMovie_filename(uuid + "_" + originName); // 저장된 파일 이름
//        vo.setFIleSize(size); // 파일 크기
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
//    }
//


