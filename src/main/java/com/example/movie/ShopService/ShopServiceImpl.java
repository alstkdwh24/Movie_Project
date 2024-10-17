package com.example.movie.ShopService;

import com.example.movie.commandVO.MainsVO.DeliciousVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("shopService")
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;





    @Override
    public int delicious_Resist(DeliciousVO vo) {
        return shopMapper.delicious_Resist(vo);
    }
}
