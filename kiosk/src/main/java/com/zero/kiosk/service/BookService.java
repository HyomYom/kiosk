package com.zero.kiosk.service;


import com.zero.kiosk.exception.impl.NotExistStore;
import com.zero.kiosk.exception.impl.NotExistUser;
import com.zero.kiosk.model.Book;
import com.zero.kiosk.model.Member;
import com.zero.kiosk.model.Store;
import com.zero.kiosk.persist.BookRepository;
import com.zero.kiosk.persist.MemberRepository;
import com.zero.kiosk.persist.StoreRepository;
import com.zero.kiosk.persist.entity.BookEntity;
import com.zero.kiosk.persist.query.BookEntitySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;


    /**
     * 1. 오류
     * 1) 입력된 시간 앞뒤 10분 내 예약 불가 오류 발생
     * 2) 현재 시간을 기준으로 이미 지난 시간은 예약 불가 오류 발생
     * @param book(userId, storeName, bookDate)
     * @return
     */
    public BookEntity addBook(Book book) {
        var newBook = book.toEntity();
        boolean bookExist = this.bookRepository.existsByStoreNameAndBookDateBetween(book.getStoreName(),
                book.getBookDate().minusMinutes(10), book.getBookDate().plusMinutes(10));
        boolean storeExist = this.storeRepository.existsByStoreName(book.getStoreName());
        if (bookExist || book.getBookDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("해당 날짜(시간)에 이미 예약이 존재하거나, 가능하지 않은 조건입니다.");
        } else if (!storeExist) {
            throw new NotExistStore();
        }
        var member = this.memberRepository.findByLoginId(book.getUserId())
                .orElseThrow(() -> new NotExistUser());

        newBook.updateMember(member);
//        return this.bookRepository.findByStoreNameAndUserIdAndBookDate(book.getStoreName(),
//                book.getUserId(), book.getBookDate());
        return this.bookRepository.save(newBook).setMember(null);
    }

    public List<BookEntity> bookSearchUser(Member member) {
        var memberEntity = this.memberRepository.findByLoginId(member.getLoginId())
                .orElseThrow(() -> new NotExistUser());
        return memberEntity.getBooks().stream().map(e -> BookEntity.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .storeName(e.getStoreName())
                .confirmYn(e.isConfirmYn())
                .checkYn(e.isCheckYn())
                .reviewYn(e.isReviewYn())
                .bookDate(e.getBookDate())
                .member(null)
                .build()).collect(Collectors.toList());
    }

    public List<BookEntity> bookSearchOwner(Store store) {
        Specification<BookEntity> spec = Specification.where(BookEntitySpecification.search(store));

        return bookRepository.findAll(spec).stream().map(e -> BookEntity.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .storeName(e.getStoreName())
                .confirmYn(e.isConfirmYn())
                .checkYn(e.isCheckYn())
                .reviewYn(e.isReviewYn())
                .bookDate(e.getBookDate())
                .member(null)
                .build()).collect(Collectors.toList());
    }


    public BookEntity bookCheckOwner(Book book) {
        BookEntity bookEntity = bookRepository.findById(book.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 예약입니다."));

        bookEntity.setConfirmYn(true);
        return bookRepository.save(bookEntity).setMember(null);
    }

    /**
     * 1. 오류
     * 1) 가게 오너가 수락하지 않은 예약은 사용자가 확인할 수 없다.
     * @param book(bookId)
     * @return
     */
    public BookEntity bookCheckUser(Book book) {
        BookEntity bookEntity = bookRepository.findById(book.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 예약입니다."));
        if(!bookEntity.isConfirmYn()){
            throw new RuntimeException("예약이 수락되지 않았습니다. 직원에게 문의해주세요.");
        }
        bookEntity.setCheckYn(true);
        return bookRepository.save(bookEntity).setMember(null);
    }
}
