package com.zero.kiosk.persist.entity;

import com.zero.kiosk.model.Book;
import com.zero.kiosk.persist.utility.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity(name = "review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String title;
    private String contents;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    private BookEntity book;


    @ManyToOne
    @JoinColumn(name = "storeId")
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    public void updateStore(StoreEntity store) {
        this.store = store;
        store.getReviews().add(this);
    }

    public void updateMember(MemberEntity member) {
        this.member = member;
        member.getReviews().add(this);

    }
    public void updateBook(BookEntity book){
        this.book = book;
        book.setReview(this);
    }

}
