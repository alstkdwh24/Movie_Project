package com.example.movie.dto.mains;

import com.example.movie.entity.mains.TicketEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
    private Integer movie_ticket_number;
    private String movie_ticket_name;
    private String movie_ticket_filename;
    private String uuid;
    private LocalDate reg_date;
    private String movie_filepath;
    private String uploadPaths;
    private String uploadPath;
    private String filePath;
    private long size;
    private String resist_textarea;
    private String movie_ticket_html;

    public TicketEntity toEntity() {
        return TicketEntity.builder()
                .movie_ticket_number(this.movie_ticket_number)
                .movie_ticket_name(this.movie_ticket_name)
                .movie_ticket_filename(this.movie_ticket_filename)
                .uuid(this.uuid)
                .reg_date(this.reg_date)
                .movie_filepath(this.movie_filepath)
                .uploadPaths(this.uploadPaths)
                .uploadPath(this.uploadPath)
                .filePath(this.filePath)
                .size(this.size)
                .resist_textarea(this.resist_textarea)
                .movie_ticket_html(this.movie_ticket_html)
                .build();
    }

    public static TicketDTO fromEntity(TicketEntity entity) {
        if (entity == null) return null;
        return TicketDTO.builder()
                .movie_ticket_number(entity.getMovie_ticket_number())
                .movie_ticket_name(entity.getMovie_ticket_name())
                .movie_ticket_filename(entity.getMovie_ticket_filename())
                .uuid(entity.getUuid())
                .reg_date(entity.getReg_date())
                .movie_filepath(entity.getMovie_filepath())
                .uploadPaths(entity.getUploadPaths())
                .uploadPath(entity.getUploadPath())
                .filePath(entity.getFilePath())
                .size(entity.getSize())
                .resist_textarea(entity.getResist_textarea())
                .movie_ticket_html(entity.getMovie_ticket_html())
                .build();
    }
}
