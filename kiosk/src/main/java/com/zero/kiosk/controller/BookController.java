package com.zero.kiosk.controller;

import com.zero.kiosk.model.Book;
import com.zero.kiosk.model.Member;
import com.zero.kiosk.model.Review;
import com.zero.kiosk.model.Store;
import com.zero.kiosk.persist.entity.BookEntity;
import com.zero.kiosk.persist.entity.ReviewEntity;
import com.zero.kiosk.service.BookService;
import com.zero.kiosk.service.WriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final WriteService writeService;

    /**
     * 예약 추가
     *
     * @param book
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> bookTheStore(@RequestBody Book book) {

        var result = bookService.addBook(book);

        return ResponseEntity.ok(result);
    }

    /**
     * 유저별 예약 리스트 조회
     *
     * @param member
     * @return
     */

    @GetMapping("/search")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> bookSearchByUser(@RequestBody Member member) {
        var result = bookService.bookSearchUser(member);
        return ResponseEntity.ok(result);
    }

    /**
     * 가게별 요일에 따른
     * 예약 리스트 조회
     *
     * @param store
     * @return
     */
    @PostMapping("/search/myStore")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> bookSearchByOwner(@RequestBody Store store) {
        List<BookEntity> bookEntities = bookService.bookSearchOwner(store);
        return ResponseEntity.ok(bookEntities);
    }

    @PostMapping("/check/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> bookCheckUser(@RequestBody Book book) {
        BookEntity bookEntity = bookService.bookCheckUser(book);
        return ResponseEntity.ok(bookEntity);
    }

    @PostMapping("/check/owner")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> bookCheckOwner(@RequestBody Book book) {
        BookEntity bookEntity = bookService.bookCheckOwner(book);
        return ResponseEntity.ok(bookEntity);
    }


    /**
     * 예약 리뷰 작성
     *
     * @param review
     * @return
     */

    @PostMapping("/review")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> writeReview(@RequestBody Review review) {
        ReviewEntity reviewEntity = writeService.writeReview(review);
        return ResponseEntity.ok(reviewEntity);
    }

}
