package com.kiosk.persist;


import com.kiosk.persist.entity.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long>, JpaSpecificationExecutor<StoreEntity> {
//    Page<StoreEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable);

    List<StoreEntity> findAllByUserIdAndOwnerName(String userId, String ownerName);
    Optional<StoreEntity> findByStoreName(String name);
    Page<StoreEntity> findAll(Specification<StoreEntity> spec, Pageable pageable);

    boolean existsByStoreName(String storeName);

    boolean existsByStoreNameAndOwnerName(String storeName, String ownerName);

}
