package com.zero.kiosk.persist.query;

import com.zero.kiosk.model.Store;
import com.zero.kiosk.persist.entity.BookEntity;
import com.zero.kiosk.persist.entity.StoreEntity;
import org.hibernate.criterion.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookEntitySpecification {

    public static Specification<BookEntity> likeName(String storeName) {
        return new Specification<BookEntity>() {
            @Override
            public Predicate toPredicate(Root<BookEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("storeName"), "%" + storeName + "%");
            }
        };
    }

    public static Specification<BookEntity> onTime(LocalDateTime start, LocalDateTime end) {
        return (Specification<BookEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add((criteriaBuilder.between(root.get("bookDate"), start, end)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<BookEntity> search(Store store) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("userId"), store.getUserId()));
            predicates.add(criteriaBuilder.equal(root.get("storeName"), store.getStoreName()));
            if (store.getBookDate()!= null) {

                LocalDateTime start = store.getBookDate().atStartOfDay();
                LocalDateTime end = store.getBookDate().atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("bookDate"), start, end));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
