package com.zero.kiosk.persist.query;

import com.zero.kiosk.persist.entity.StoreEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StoreEntitySpecification {

    public static Specification<StoreEntity> equalOwnerId(String ownerId) {
        return new Specification<StoreEntity>() {
            @Override
            public Predicate toPredicate(Root<StoreEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("userId"), ownerId);
            }
        };
    }

    public static Specification<StoreEntity> equalOwnerName(String ownerName) {
        return new Specification<StoreEntity>() {
            @Override
            public Predicate toPredicate(Root<StoreEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("ownerName"), ownerName);
            }
        };
    }

    public static Specification<StoreEntity> likeName(String storeName) {
        return new Specification<StoreEntity>() {
            @Override
            public Predicate toPredicate(Root<StoreEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("storeName"), "%" + storeName + "%");
            }
        };
    }
//    static Specification<StoreEntity> orderBy() {
//       return new Specification<StoreEntity>() {
//           @Override
//           public Predicate toPredicate(Root<StoreEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//               return criteriaBuilder.desc();
//           }
//       }
//    }

    public static Specification<StoreEntity> search(String storeName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(root.get("storeName"), "%"+storeName+"%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
