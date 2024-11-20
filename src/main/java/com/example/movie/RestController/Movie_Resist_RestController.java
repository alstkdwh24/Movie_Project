package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_Image_Service;

    public String makeFolder() {
        String filepath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        File file = new File(uploadPath + '/' + filepath);
        if (!file.exists()) { //해당 파일이 있으면 true, 없으면!
            file.mkdirs();
        }
        return filepath;
    }


    @PostMapping("/upload_ok/movie_resist_two")
    public ResponseEntity<MovieVO> movie_resist_two(@RequestParam("file") MultipartFile movie_resist_file,String imageFilePath,

                                                    @RequestParam("movie_filename") String movie_filename) throws IOException {


        long size =movie_resist_file.getSize();
        String uuid=UUID.randomUUID().toString();
        String filepath = makeFolder();
        String savePath=uploadPath + "/" + filepath +"/" + uuid + "_" + movie_filename;

        System.out.println("파일명:" + movie_filename); //원본파일명 DB저장
        System.out.println("파일 사이즈:" + size); //폴더명 DB저장
        System.out.println("파일 구분:" + uuid); //파일 구분 DB저장
        System.out.println("업로그 할 경로:" + savePath); //업로드할 경로 저장



        File saveFile = new File(savePath);

        movie_resist_file.transferTo(saveFile);//업로드 바로

        return ResponseEntity.ok(MovieVO.builder().build());
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


