package com.kiosk.model;

import com.kiosk.persist.entity.ReviewEntity;
import lombok.Data;

@Data
public class Review {
    private Long bookId;
    private String userId;
    private String storeName;
    private String title;
    private String contents;



    public ReviewEntity toEntity(){
        return ReviewEntity.builder()
                .userId(this.userId)
                .title(this.title)
                .contents(this.contents)
                .build();
    }
}
