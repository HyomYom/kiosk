package com.kiosk.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.kiosk.persist.entity.BookEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {
    private Long id;
    private String userId;
    private String storeName;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime bookDate;

    public BookEntity toEntity(){
        return BookEntity.builder()
                .userId(this.userId)
                .storeName(this.storeName)
                .bookDate(this.bookDate)
                .build();
    }
}
