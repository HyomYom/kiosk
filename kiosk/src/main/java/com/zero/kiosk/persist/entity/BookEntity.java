package com.zero.kiosk.persist.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zero.kiosk.persist.entity.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "book")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String storeName;
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean confirmYn;
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean checkYn;

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean reviewYn;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //get
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") //post
    private LocalDateTime bookDate;

    @JsonIgnore
    @OneToOne(mappedBy = "book")
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    public BookEntity setMember(MemberEntity member){
        this.member = member;
        return this;
    }

    public void updateMember(MemberEntity member) {
        this.member = member;
        member.getBooks().add(this);
    }
}
