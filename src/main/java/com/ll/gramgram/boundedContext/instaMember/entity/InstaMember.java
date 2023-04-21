package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.base.BaseEntity;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@ToString(callSuper = true) // 디버그를 위한
@SuperBuilder
public class InstaMember extends BaseEntity {
    @Column(unique = true)
    private String username;
    @Setter
    private String gender;

    @OneToMany(mappedBy = "fromInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default //@Builder 가 있으면 ' = new ArrayList<>;'가 작동하지 않는다. 그렇기에 이걸 붙임
    private List<LikeablePerson> fromLikealbePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id DESC")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default //@Builder 가 있으면 ' = new ArrayList<>;'가 작동하지 않는다. 그렇기에 이걸 붙임
    private List<LikeablePerson> toLikealbePeople = new ArrayList<>();


    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikealbePeople.add(0, likeablePerson);// 최신것이 앞으로 들어가야한다.
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikealbePeople.add(0, likeablePerson);
    }
}
