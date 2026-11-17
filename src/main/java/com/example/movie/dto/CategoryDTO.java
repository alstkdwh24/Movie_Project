package com.example.movie.dto;

import com.example.movie.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

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

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .movie_category_lv(this.movie_category_lv)
                .movie_category_nm(this.movie_category_nm)
                .movie_detail_lv(this.movie_detail_lv)
                .movie_detail_title(this.movie_detail_title)
                .movie_category_parents_lv(this.movie_category_parents_lv)
                .movie_category_detail_parent_lv(this.movie_category_detail_parent_lv)
                .movie_write_category(this.movie_write_category)
                .group_id(this.group_id)
                .movie_category_number(this.movie_category_number)
                .id(this.id)
                .build();
    }

    public static CategoryDTO fromEntity(CategoryEntity entity) {
        if (entity == null) return null;
        return CategoryDTO.builder()
                .movie_category_lv(entity.getMovie_category_lv())
                .movie_category_nm(entity.getMovie_category_nm())
                .movie_detail_lv(entity.getMovie_detail_lv())
                .movie_detail_title(entity.getMovie_detail_title())
                .movie_category_parents_lv(entity.getMovie_category_parents_lv())
                .movie_category_detail_parent_lv(entity.getMovie_category_detail_parent_lv())
                .movie_write_category(entity.getMovie_write_category())
                .group_id(entity.getGroup_id())
                .movie_category_number(entity.getMovie_category_number())
                .id(entity.getId())
                .build();
    }
}
