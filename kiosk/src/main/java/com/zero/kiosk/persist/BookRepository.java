package com.zero.kiosk.persist;


import com.zero.kiosk.model.Book;
import com.zero.kiosk.persist.entity.BookEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {
    boolean existsByStoreNameAndBookDateBetween(String storeName, LocalDateTime start, LocalDateTime end);

}
