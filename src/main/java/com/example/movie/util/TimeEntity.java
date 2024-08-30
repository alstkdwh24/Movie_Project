package com.example.movie.util;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeEntity {

    @CreatedDate
    private LocalDate write_time;


    @LastModifiedDate
    private LocalDateTime modified_time;
}
