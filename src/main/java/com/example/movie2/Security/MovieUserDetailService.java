package com.example.movie2.Security;


import com.example.movie2.LoginService.LoginMapper;
import com.example.movie2.commandVO.LoginVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MovieUserDetailService implements UserDetailsService {


    @Autowired
    private LoginMapper loginMapper;


    @Override
    public UserDetails loadUserByUsername(@Param("id") String id) throws UsernameNotFoundException {
        System.out.println("사용자가 로그인을 시도함");
        System.out.println("사용자가 입력한 이름");

        LoginVO vo = loginMapper.Logins(id);
        System.out.println(vo);

        //회원정보가 없음 ->비밀번호 비교를 하기 위해서 UserDetails타입으로 리턴
        if (vo != null) {


            return new MovieUserDetail(vo); //스프링 스큐리티가 비밀번호 비교, 롤 확인도 해서 로그인 처리

        } else {
            //시큐러티에 설정한 형식대로, 권한처리까지 처리를 해 줍니다.
            //만약 아이디가 없거나, 바밀번호가 틀리면 login?error로 기본 이동합니다.
            //시큐러티는 특별한 세션 모형으로 사용함
            //시쿠러티는 (new Authentication(new MyUserDetail())모형으로 저장시킵니다.


            return null;
        }

    }
}
