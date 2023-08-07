package com.zero.kiosk.model;

import com.zero.kiosk.persist.entity.MemberEntity;
import lombok.Data;

@Data
public class Member {
    private String loginId;
    private String name;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .loginId(this.loginId)
                .name(this.name)
                .build();
    }
}
