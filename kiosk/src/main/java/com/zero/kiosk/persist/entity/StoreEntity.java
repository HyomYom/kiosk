package com.zero.kiosk.persist.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity(name = "store")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double distance;
    private String storeName;
    private String address;
    private String userId;
    private String ownerName;
    private double latitude;
    private double longitude;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store")
    private List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    public StoreEntity setMember(MemberEntity member){
        this.member = member;
        return this;
    }

    public void updateMember(MemberEntity member) {
        this.member = member;
        member.getStores().add(this);
    }


}
