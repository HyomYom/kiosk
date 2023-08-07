package com.zero.kiosk.service;

import com.zero.kiosk.exception.impl.AlreadyExistUserException;
import com.zero.kiosk.model.Auth;
import com.zero.kiosk.persist.entity.MemberEntity;
import com.zero.kiosk.persist.MemberRepository;
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

    public MemberEntity register(Auth.SignUp member) {
        boolean exits = this.memberRepository.existsByLoginId(member.getLoginId());
        if(exits){
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());
        return result;
    }

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
