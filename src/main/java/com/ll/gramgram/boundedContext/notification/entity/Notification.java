package com.ll.gramgram.boundedContext.notification.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Notification extends BaseEntity {
    private LocalDateTime readDate;
    @ManyToOne
    @ToString.Exclude
    private InstaMember toInstaMember; // 메세지 받는 사람(호감 받는 사람)
    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember; // 메세지를 발생시킨 행위를 한 사람(호감표시한 사람)
    private String typeCode; // 호감표시=Like, 호감사유변경=ModifyAttractiveType
    private String oldGender; // 해당사항 없으면 null
    private int oldAttractiveTypeCode; // 해당사항 없으면 0
    private String newGender; // 해당사항 없으면 null
    private int newAttractiveTypeCode; // 해당사항 없으면 0

    public void updateReadDate(){
        readDate = LocalDateTime.now();
    }

    public boolean isRead(){
        return readDate!=null;
    }
    public boolean isModify(){
        return typeCode.equals("Modify");
    }
    public boolean isLike(){
        return typeCode.equals("Like");
    }

    public String passedTime(){
        long passingTime = ChronoUnit.MINUTES.between(getCreateDate(),LocalDateTime.now());

        if(passingTime <= 0) return "지금";
        else if(passingTime < 60) return "%s분 전".formatted(passingTime);
        else return "%s시간 전".formatted(passingTime / 60);
    }

    public String getAttractiveTypeDisplayName() {
        return switch (newAttractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public String getOldAttractiveTypeDisplayName() {
        return switch (oldAttractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }


    public String getGenderDisplayName() {
        return switch (getNewGender()) {
            case "W" -> "여성";
            default -> "남성";
        };
    }
}
