package com.zero.kiosk.service;

import com.zero.kiosk.exception.impl.AlreadyExistStoreException;
import com.zero.kiosk.exception.impl.InfoOmissionException;
import com.zero.kiosk.exception.impl.NotExistUser;
import com.zero.kiosk.model.Member;
import com.zero.kiosk.model.Store;
import com.zero.kiosk.persist.MemberRepository;
import com.zero.kiosk.persist.query.StoreEntitySpecification;
import com.zero.kiosk.persist.StoreRepository;
import com.zero.kiosk.persist.entity.StoreEntity;
import com.zero.kiosk.persist.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final Utility utility;

    public StoreEntity addStore(Store store) {
        var newStore = store.toEntity();
        var member = this.memberRepository.findByLoginId(store.getUserId())
                .orElseThrow(() -> new NotExistUser());
        log.info(store.getOwnerName() + " " + member.getName());
        if (store == null || store.getStoreName().equals("") ||
                !member.getName().equals(store.getOwnerName())
        ) {
            throw new InfoOmissionException();
        }

        boolean exits = storeRepository.existsByStoreNameAndOwnerName(store.getStoreName()
                , store.getOwnerName());
        if (exits) {
            throw new AlreadyExistStoreException();
        }
        newStore.updateMember(member);
        storeRepository.save(newStore);
        return this.storeRepository.findByStoreName(store.getStoreName()).orElseThrow(() -> new RuntimeException("상점이 존재하지 않습니다."));
    }

    public Page<StoreEntity> searchStoreList(String storeName, double lat, double lon,
                                             String unit, Pageable pageable) {
        Specification<StoreEntity> spec = Specification.where(StoreEntitySpecification.search(storeName));
        return storeRepository.findAll(spec, pageable).map(e -> StoreEntity.builder()
                .id(e.getId())
                .distance(utility.getDistance(e.getLatitude(), e.getLongitude(), lat, lon, unit))
                .storeName(e.getStoreName())
                .address(e.getAddress())
                .userId(e.getUserId())
                .ownerName(e.getOwnerName())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .build());
    }

    public List<StoreEntity> searchOwnerStoreList(Member member) {
        Specification<StoreEntity> spec = Specification.where(StoreEntitySpecification.equalOwnerId(member.getLoginId()))
                .and(Specification.where(StoreEntitySpecification.equalOwnerName(member.getName())));

        if (ObjectUtils.isEmpty(spec)) {
            throw new RuntimeException("가게 정보를 불러오는데 실패하였습니다");
        }
        return storeRepository.findAll(spec).stream().map(e -> StoreEntity.builder()
                .id(e.getId())
                .storeName(e.getStoreName())
                .address(e.getAddress())
                .userId(e.getUserId())
                .ownerName(e.getOwnerName())
                .member(null)
                .build()).collect(Collectors.toList());
    }
}
