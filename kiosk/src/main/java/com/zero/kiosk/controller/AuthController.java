package com.zero.kiosk.controller;

import com.zero.kiosk.model.Auth;
import com.zero.kiosk.security.TokenProvider;
import com.zero.kiosk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request){
        var result = memberService.register(request);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request){
        var member = this.memberService.authenticate(request);
        var token = this.tokenProvider.generateToken(member.getLoginId(), member.getRoles());
        return ResponseEntity.ok(token);
    }
}
