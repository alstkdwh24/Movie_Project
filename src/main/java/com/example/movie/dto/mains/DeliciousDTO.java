package com.example.movie.dto.mains;

import com.example.movie.entity.mains.DeliciousEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliciousDTO {
    private Integer delicious_number;
    private String delicious_name;
    private String delicious_filename;
    private String uuid;
    private LocalDateTime reg_date;
    private String movie_filepath;
    private String resist_textarea;
    private String uploadPaths;
    private long size;
    private String movie_resist_filePath;
    private String filePath;

    public DeliciousEntity toEntity() {
        return DeliciousEntity.builder()
                .delicious_number(this.delicious_number)
                .delicious_name(this.delicious_name)
                .delicious_filename(this.delicious_filename)
                .uuid(this.uuid)
                .reg_date(this.reg_date)
                .movie_filepath(this.movie_filepath)
                .resist_textarea(this.resist_textarea)
                .uploadPaths(this.uploadPaths)
                .size(this.size)
                .movie_resist_filePath(this.movie_resist_filePath)
                .filePath(this.filePath)
                .build();
    }

    public static DeliciousDTO fromEntity(DeliciousEntity entity) {
        if (entity == null) return null;
        return DeliciousDTO.builder()
                .delicious_number(entity.getDelicious_number())
                .delicious_name(entity.getDelicious_name())
                .delicious_filename(entity.getDelicious_filename())
                .uuid(entity.getUuid())
                .reg_date(entity.getReg_date())
                .movie_filepath(entity.getMovie_filepath())
                .resist_textarea(entity.getResist_textarea())
                .uploadPaths(entity.getUploadPaths())
                .size(entity.getSize())
                .movie_resist_filePath(entity.getMovie_resist_filePath())
                .filePath(entity.getFilePath())
                .build();
    }
}
