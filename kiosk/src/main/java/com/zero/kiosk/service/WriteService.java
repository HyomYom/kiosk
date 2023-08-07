package com.zero.kiosk.service;

import com.zero.kiosk.exception.impl.NotExistStore;
import com.zero.kiosk.exception.impl.NotExistUser;
import com.zero.kiosk.model.Book;
import com.zero.kiosk.model.Review;
import com.zero.kiosk.persist.MemberRepository;
import com.zero.kiosk.persist.ReviewRepository;
import com.zero.kiosk.persist.StoreRepository;
import com.zero.kiosk.persist.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WriteService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;


    public ReviewEntity writeReview(Review review) {
        var member = this.memberRepository.findByLoginId(review.getUserId()).orElseThrow(() -> new NotExistUser());
        var store = this.storeRepository.findByStoreName(review.getStoreName()).orElseThrow(() -> new NotExistStore());
        var newReview = review.toEntity();
        newReview.updateStore(store);
        newReview.updateMember(member);
        newReview =  reviewRepository.save(newReview);
        
        return ReviewEntity.builder()
                .id(newReview.getId())
                .userId(newReview.getUserId())
                .title(newReview.getTitle())
                .contents(newReview.getContents())
                .store(newReview.getStore().setMember(null))
                .build();
    }
}
