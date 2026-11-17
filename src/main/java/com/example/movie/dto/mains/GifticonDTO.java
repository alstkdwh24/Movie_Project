package com.example.movie.dto.mains;

import com.example.movie.entity.mains.GifticonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GifticonDTO {
    private Integer Gifticon_number;
    private String Gifticon_name;
    private String Gifticon_filename;
    private String uuid;
    private String movie_filepath;
    private LocalDateTime reg_date;
    private String uploadPaths;
    private long size;
    private String movie_resist_filePath;
    private String resist_textarea;
    private String GifticonHtml;

    public GifticonEntity toEntity() {
        return GifticonEntity.builder()
                .Gifticon_number(this.Gifticon_number)
                .Gifticon_name(this.Gifticon_name)
                .Gifticon_filename(this.Gifticon_filename)
                .uuid(this.uuid)
                .movie_filepath(this.movie_filepath)
                .reg_date(this.reg_date)
                .uploadPaths(this.uploadPaths)
                .size(this.size)
                .movie_resist_filePath(this.movie_resist_filePath)
                .resist_textarea(this.resist_textarea)
                .GifticonHtml(this.GifticonHtml)
                .build();
    }

    public static GifticonDTO fromEntity(GifticonEntity entity) {
        if (entity == null) return null;
        return GifticonDTO.builder()
                .Gifticon_number(entity.getGifticon_number())
                .Gifticon_name(entity.getGifticon_name())
                .Gifticon_filename(entity.getGifticon_filename())
                .uuid(entity.getUuid())
                .movie_filepath(entity.getMovie_filepath())
                .reg_date(entity.getReg_date())
                .uploadPaths(entity.getUploadPaths())
                .size(entity.getSize())
                .movie_resist_filePath(entity.getMovie_resist_filePath())
                .resist_textarea(entity.getResist_textarea())
                .GifticonHtml(entity.getGifticonHtml())
                .build();
    }
}
