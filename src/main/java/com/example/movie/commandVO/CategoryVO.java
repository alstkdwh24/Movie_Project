package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryVO {

    private Integer movie_category_lv;
    private String movie_category_nm;
    private Integer movie_detail_lv;
    private String movie_detail_title;
    private Integer movie_category_parents_lv;
    private Integer movie_category_detail_parent_lv;
private String movie_write_category;
    private String group_id;
    private Integer movie_category_number;
    private String id;


}
