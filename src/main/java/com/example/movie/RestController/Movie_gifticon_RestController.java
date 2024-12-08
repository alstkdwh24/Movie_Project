package com.example.movie.RestController;

import com.example.movie.commandVO.MainsVO.Movie_gifticonVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

}
