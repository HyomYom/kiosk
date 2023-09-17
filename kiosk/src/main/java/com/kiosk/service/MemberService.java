package com.kiosk.service;

import com.kiosk.exception.impl.AlreadyExistUserException;
import com.kiosk.model.Auth;
import com.kiosk.persist.MemberRepository;
import com.kiosk.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 1. 오류
     * 1) 동일 아이디가 이미 존재할 시 오류 발생
     *
     * 2. 암호화
     * 1) 비밀번호를 암호화 하여 저장
     * @param member(loginId, password, name, roles[])
     * @return
     */
    public MemberEntity register(Auth.SignUp member) {
        boolean exits = this.memberRepository.existsByLoginId(member.getLoginId());
        if(exits){
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());
        return result;
    }

    /**
     * 1. 오류
     * 1) 아아디와 비밀번호가 일치하지 않으면 오류 발생
     *
     * @param member(loginId, password)
     * @return
     */

    public MemberEntity authenticate(Auth.SignIn member){
        var user = this.memberRepository.findByLoginId(member.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return this.memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + loginId));
    }
}
