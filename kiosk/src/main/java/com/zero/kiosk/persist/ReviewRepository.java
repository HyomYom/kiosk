package com.zero.kiosk.persist;


import com.zero.kiosk.persist.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findTopByUserIdOrderByCreatedDateDesc(String userId);

}
