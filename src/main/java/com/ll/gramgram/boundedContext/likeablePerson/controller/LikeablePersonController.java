package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/likeablePerson")
@RequiredArgsConstructor
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @GetMapping("/add")
    public String showAdd() {
        return "usr/likeablePerson/add";
    }

    @AllArgsConstructor
    @Getter
    public static class AddForm {
        private final String username;
        private final int attractiveTypeCode;
    }

    @PostMapping("/add")
    public String add(@Valid AddForm addForm) {

        RsData<LikeablePerson> canLikeRsData = likeablePersonService.canLike(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        if (canLikeRsData.isFail()) {
            return rq.historyBack(canLikeRsData);
        }

        RsData rsData;

        if(canLikeRsData.getResultCode().equals("S-2")){
            // 이미 등록된 호감대상의 호감 사유만 바꾸는 경우
            rsData = likeablePersonService.modifyAttractive(canLikeRsData.getData(), addForm.attractiveTypeCode);
        }else{
            // 호감표시에 문제가 없고 호감 대상 추가하는 경우
            rsData = likeablePersonService.like(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());
        }

        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/likeablePerson/list", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id){
        LikeablePerson likeablePeople = likeablePersonService.findById(id).orElse(null);

        RsData canActorDelete = likeablePersonService.canActorDelete(rq.getMember().getInstaMember(), likeablePeople);

        if(canActorDelete.isFail()) return rq.historyBack(canActorDelete);

        RsData deleteRs = likeablePersonService.delete(likeablePeople);

        if(deleteRs.isFail()) return rq.historyBack(deleteRs);

        return rq.redirectWithMsg("/likeablePerson/list", deleteRs);
    }

    @GetMapping("/list")
    public String showList(Model model) {
        InstaMember instaMember = rq.getMember().getInstaMember();

        // 인스타인증을 했는지 체크
        if (instaMember != null) {
            List<LikeablePerson> likeablePeople = instaMember.getFromLikealbePeople();
            model.addAttribute("likeablePeople", likeablePeople);
        }

        return "usr/likeablePerson/list";
    }
}
