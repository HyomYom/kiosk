package com.kiosk.service;

import com.kiosk.exception.impl.AlreadyExistStoreException;
import com.kiosk.exception.impl.InfoOmissionException;
import com.kiosk.model.Member;
import com.kiosk.persist.MemberRepository;
import com.kiosk.persist.StoreRepository;
import com.kiosk.persist.query.StoreEntitySpecification;
import com.kiosk.persist.utility.Utility;
import com.kiosk.exception.impl.NotExistUser;
import com.kiosk.model.Store;
import com.kiosk.persist.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final Utility utility;


    /**
     * 1. 오류
     * 1) 정보 누락 또는 등록하려는 가게 소유주 정보와 맴버 정보가 일치하지 않을 때
     * 2) 가게 소유주의 이름으로 동일한 가게가 존재할 때
     *
     * 2. 관계 설정
     * 1) member-store[OneToMany-ManyToOne] 설정 업데이트 후 저장
     *
     * @param store(storeName, userId, ownerName, address, longitude, latitude)
     * @return
     */
    public StoreEntity addStore(Store store) {
        var newStore = store.toEntity();
        var member = this.memberRepository.findByLoginId(store.getUserId())
                .orElseThrow(() -> new NotExistUser());
        log.info(store.getOwnerName() + " " + member.getName());
        if (store == null || store.getStoreName().equals("") ||
                !member.getName().equals(store.getOwnerName())
                        && !member.getLoginId().equals(store.getUserId())
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

    /**
     * 1. 거리 설정
     * 1) 입력되는 좌표에 따라 현재 위치부터 가게까지의 거리를 제공
     * 2) 거리는 조회 후 얻어지기 때문에 거리순으로 정렬시 정렬 후 재페이징 처리
     * @param storeName
     * @param lat
     * @param lon
     * @param unit
     * @param pageable
     * @return
     */
    public Page<StoreEntity> searchStoreList(String storeName, double lat, double lon,
                                             String unit, String sort,String dir, Pageable pageable) {
        Specification<StoreEntity> spec = Specification.where(StoreEntitySpecification.search(storeName));

       Page<StoreEntity> storePage = storeRepository.findAll(spec, pageable).map(e -> StoreEntity.builder()
                .id(e.getId())
                .distance(utility.getDistance(e.getLatitude(), e.getLongitude(), lat, lon, unit))
                .storeName(e.getStoreName())
                .address(e.getAddress())
                .userId(e.getUserId())
                .ownerName(e.getOwnerName())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .build());

        if(sort.equals("distance") && dir.equals("desc")){
            List<StoreEntity> collect = storePage.stream().sorted(Comparator.comparing(StoreEntity::getDistance).reversed()).collect(Collectors.toList());
            storePage = new PageImpl<>(collect, pageable, collect.size());
            return storePage;
        } else if(sort.equals("distance") && dir.equals("asc")){
            List<StoreEntity> collect = storePage.stream().sorted(Comparator.comparing(StoreEntity::getDistance)).collect(Collectors.toList());
            storePage = new PageImpl<>(collect,pageable,collect.size());
            return storePage;
        }
        return storePage;
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
