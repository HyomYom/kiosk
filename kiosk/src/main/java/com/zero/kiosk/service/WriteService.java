package com.zero.kiosk.service;

import com.zero.kiosk.exception.impl.NotExistStore;
import com.zero.kiosk.exception.impl.NotExistUser;
import com.zero.kiosk.model.Book;
import com.zero.kiosk.model.Review;
import com.zero.kiosk.persist.BookRepository;
import com.zero.kiosk.persist.MemberRepository;
import com.zero.kiosk.persist.ReviewRepository;
import com.zero.kiosk.persist.StoreRepository;
import com.zero.kiosk.persist.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WriteService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    /**
     * 1. 오류
     * 1) 예약이 수락되지 않거나, 예약이 확인되지 않거나, 이리 리뷰가 작성됐다면 오류 발생
     *
     * 2. 관계 설정
     * 1) member-review[OneToMany-ManyToOne]
     * 2) store-review[OneToMany-ManyToOne]
     * 3) book-review[OneToOne]
     * @param review(booId, userId, StoreName, title, contents)
     * @return
     */
    public ReviewEntity writeReview(Review review) {
        var member = this.memberRepository.findByLoginId(review.getUserId()).orElseThrow(() -> new NotExistUser());
        var store = this.storeRepository.findByStoreName(review.getStoreName()).orElseThrow(() -> new NotExistStore());
        var book = this.bookRepository.findById(review.getBookId()).orElseThrow(() -> new RuntimeException("에약이 존재하지 않습니다"));
        if(!book.getStoreName().equals(store.getStoreName())){
            throw new RuntimeException("예약된 가게의 정보가 일치하지 않습니다.");
        }
        if(!book.isConfirmYn()||!book.isCheckYn()&& book.isConfirmYn()){
            throw new RuntimeException("예약 정보가 없거나, 예약이 되지 않았습니다.");
        } else if(book.isReviewYn()){
            throw new RuntimeException("이미 리뷰를 작성하셨습니다.");
        }

        var newReview = review.toEntity();
        newReview.updateStore(store);
        newReview.updateMember(member);
        book.setReviewYn(true);
        newReview.updateBook(book);
        newReview =  reviewRepository.save(newReview);


        return ReviewEntity.builder()
                .id(newReview.getId())
                .userId(newReview.getUserId())
                .title(newReview.getTitle())
                .contents(newReview.getContents())
                .store(newReview.getStore().setMember(null))
                .book(newReview.getBook().setMember(null))
                .build();
    }
}
