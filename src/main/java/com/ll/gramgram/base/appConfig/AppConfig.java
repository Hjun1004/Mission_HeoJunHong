package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class AppConfig {
    @Getter
    private static long likeablePersonFromMax;


    @Value("${custom.likeablePerson.from.max}")
    public void setLikeablePersonFromMax(long likeablePersonFromMax) {
        AppConfig.likeablePersonFromMax = likeablePersonFromMax;
    }

    @Getter
    private static long likeablePersonModifyCoolTime;

    @Getter
    private static String likeablePersonActionModify;

    @Getter
    private static String likeablePersonActionCancel;

    @Value("${custom.likeablePerson.modifyCoolTime}")
    public void setLikeablePersonModifyCoolTime(long likeablePersonModifyCoolTime) {
        AppConfig.likeablePersonModifyCoolTime = likeablePersonModifyCoolTime;
    }

    @Value("${custom.likeablePerson.action.modify}")
    public void setLikeablePersonActionModify(String likeablePersonActionModify) {
        AppConfig.likeablePersonActionModify = likeablePersonActionModify;
    }

    @Value("${custom.likeablePerson.action.cancel}")
    public void setLikeablePersonActionCancel(String likeablePersonActionCancel) {
        AppConfig.likeablePersonActionCancel = likeablePersonActionCancel;
    }

    public static LocalDateTime genLikeablePersonModifyUnlockDate() {
        return LocalDateTime.now().plusSeconds(likeablePersonModifyCoolTime);
    }
}