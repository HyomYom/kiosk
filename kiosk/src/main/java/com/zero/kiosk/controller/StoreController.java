package com.zero.kiosk.controller;


import com.zero.kiosk.model.Member;
import com.zero.kiosk.model.Store;
import com.zero.kiosk.persist.entity.StoreEntity;
import com.zero.kiosk.persist.utility.PageRequest;
import com.zero.kiosk.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    /**
     * 가게 등록
     *
     * @param store
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> addStore(@RequestBody Store store) {
        var save = this.storeService.addStore(store);
        return ResponseEntity.ok(save);
    }

    /**
     * 매장 검색
     * 1. 조건 설정
     * 1) 가게명 -> 입력 글자가 들어간 가게 조회
     * 2) 정렬 -> 지정 colum 기준 조회
     * 3) 차순 -> 내림차순/오름차순 조회
     *
     * 2. paging 처리
     * 1) 위 조건을 Pageble에 담아 처리
     * @param store
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> storeSearch(@RequestParam("store") String store
            , @RequestParam("lat") double lat, @RequestParam("lon") double lon,
                                         @RequestParam("unit") String unit,
                                         @RequestParam("sort") String sort,
                                         @RequestParam("dir") String dir,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size,
                                         Pageable pageable) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        if (sort != null && dir != null && !sort.equals("") && !dir.equals("")) {
            if (dir.equals("desc")) {
                pageable = pageRequest.of(Sort.Direction.DESC, sort);
            } else {

                pageable = pageRequest.of(Sort.Direction.ASC, sort);
            }
        } else {
            pageable = pageRequest.of();
        }
        Page<StoreEntity> storeEntities = storeService.searchStoreList(store, lat, lon, unit,sort, dir, pageable);
        return ResponseEntity.ok(storeEntities);
    }

    /**
     * 유저별 소유 가게 정보 리스트 조회
     *
     * @param member
     * @return
     */
    @PostMapping("/search")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> storeSearch(@RequestBody Member member) {
        List<StoreEntity> storeEntities = storeService.searchOwnerStoreList(member);
        return ResponseEntity.ok(storeEntities);
    }
}
