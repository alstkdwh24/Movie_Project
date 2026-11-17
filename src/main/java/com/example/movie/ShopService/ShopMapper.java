package com.example.movie.ShopService;

import com.example.movie.entity.mains.DeliciousEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopMapper {
    int delicious_Resist(DeliciousEntity entity);
}
