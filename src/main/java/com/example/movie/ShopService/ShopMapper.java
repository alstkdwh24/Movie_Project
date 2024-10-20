package com.example.movie.ShopService;

import com.example.movie.commandVO.MainsVO.DeliciousVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopMapper {
    int delicious_Resist(DeliciousVO vo);

}
