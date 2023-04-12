package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.exception.DataNotFoundException;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        LikeablePerson existLikeablePeople = exist(member.getInstaMember(), username);

        if(existLikeablePeople != null){
            if(existLikeablePeople.getAttractiveTypeCode() != attractiveTypeCode){
                return modify(existLikeablePeople,attractiveTypeCode);
            }
            return RsData.of("F-3", "중복으로 호감표시를 할 수 없습니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();

        int like_List_Max = (int)AppConfig.getLikeablePersonFromMax();
        // 호감 표시 대상 10명 체크
        List<LikeablePerson> fromLikeableList = fromInstaMember.getFromLikealbePeople();
        if(fromLikeableList.size() >= like_List_Max)
            return RsData.of("F-3", "호감표시는 %d명을 넘을 수 없습니다.".formatted(like_List_Max));

        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(fromInstaMember.getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장
        // 니가 좋아하는 호감표시 하나 생겼다.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public LikeablePerson exist(InstaMember instaMember, String username) {

        LikeablePerson existLikeablePerson = likeablePersonRepository.findByFromInstaMemberIdAndToInstaMember_username(instaMember.getId(),username);

        if(existLikeablePerson !=  null){
            return existLikeablePerson;
        }
        return null;
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public RsData<LikeablePerson> modify(LikeablePerson modifyLikeablePeople, int attractiveTypeCode){
        // 수정 전의 호감 사유
        String beforeAttractive = modifyLikeablePeople.getAttractiveTypeDisplayName();

        // 호감 사유 수정
        modifyLikeablePeople.setAttractiveTypeCode(attractiveTypeCode);

        return RsData.of("S-2","%s에 대한 호감사유를 %s에서 %s으로 변경합니다.".formatted(modifyLikeablePeople.getToInstaMember().getUsername()
                ,beforeAttractive,modifyLikeablePeople.getAttractiveTypeDisplayName()),modifyLikeablePeople);
    }


    public Optional<LikeablePerson> findById(Long id){
        return likeablePersonRepository.findById(id);
    }

    @Transactional
    public RsData<LikeablePerson> canActorDelete(InstaMember instaActor, LikeablePerson likeablePeople) {
        if(likeablePeople == null){
            return RsData.of("F-1", "이미 삭제되었습니다.");
        }

        if(!instaActor.getId().equals(likeablePeople.getFromInstaMember().getId())){
            return RsData.of("F-2", "삭제 권한이 없습니다.");
        }
        return RsData.of("S-2", "삭제 가능합니다.", likeablePeople);
    }


    @Transactional
    public RsData<LikeablePerson> delete(LikeablePerson likeablePeople) {
        String toInstaMemberUsername = likeablePeople.getToInstaMember().getUsername();
        likeablePersonRepository.delete(likeablePeople);
        return RsData.of("S-1","%s에 대한 호감이 삭제 되었습니다.".formatted(toInstaMemberUsername));
    }
}
