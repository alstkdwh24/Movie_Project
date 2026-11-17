package com.example.movie.ShopService;

import com.example.movie.dto.mains.DeliciousDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shopService")
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;

    @Override
    public int delicious_Resist(DeliciousDTO dto) {
        return shopMapper.delicious_Resist(dto.toEntity());
    }
}
