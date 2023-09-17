package com.kiosk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kiosk.persist.entity.StoreEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Store {
    private String storeName;
    private String address;
    private String userId;
    private String ownerName;
    private double latitude;
    private double longitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate bookDate;
//    private MemberEntity member;

    public StoreEntity toEntity(){
        return StoreEntity.builder()
                .storeName(this.storeName)
                .address(this.address)
                .ownerName(this.ownerName)
                .userId(this.userId)
                .latitude(this.latitude)
                .longitude(this.longitude)
//                .member(this.member)
                .build();
    }
}
