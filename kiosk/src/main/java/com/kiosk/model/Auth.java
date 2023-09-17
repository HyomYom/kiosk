package com.kiosk.model;

import com.kiosk.persist.entity.MemberEntity;
import lombok.Data;

import java.util.List;


public class Auth {
    @Data
    public static class SignUp{
        private String loginId;
        private String password;
        private String name;
        private List<String> roles;

        public MemberEntity toEntity(){
                return MemberEntity.builder()
                        .loginId(this.loginId)
                        .password(this.password)
                        .name(this.name)
                        .roles(this.roles)
                        .build();

        }


    }
    @Data
    public static class SignIn{

        private String loginId;
        private String password;

    }

}
