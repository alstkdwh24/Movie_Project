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

    @PostMapping(value = "/event_resist", consumes = "multipart/form-data")
    public ResponseEntity<EventVO_Board> event_resist(@RequestParam("event_name") String eventName,
                                                      @RequestParam("resist_textarea") String resistText, @RequestParam("movie_filename") MultipartFile file) {
        String originName = file.getOriginalFilename();

        originName = originName.substring(originName.lastIndexOf("\\") + 1);

        long size = file.getSize();
        // 동일한 파일로 업로드가 되면 덮어지기 때문에, 랜덤한 이름을 생성해야 한다.
        String uuid = UUID.randomUUID().toString();

        //날짜별로 폴더 생성
        String filepath = makeFolder();

        String savePath = uploadPath + "/" + filepath + '/' + uuid + "_" + originName;
        System.out.println("파일명:" + originName); //원본파일명 DB저장
        System.out.println("파일 사이즈:" + size); //폴더명 DB저장
        System.out.println("파일 구분:" + uuid); //파일 구분 DB저장
        System.out.println("업로그 할 경로:" + savePath); //업로드할 경로 저장


        EventVO_Board vo = new EventVO_Board();
        vo.setEvent_name(eventName);
        vo.setResist_textarea(resistText);
        vo.setMovie_filename(uuid + "_" + originName); // 저장된 파일 이름
        vo.setFileSize(size); // 파일 크기

        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }


}
