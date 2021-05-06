package com.tommy.jpabook.bootjpaapplication.member.controller;

import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.dto.CreateMemberRequest;
import com.tommy.jpabook.bootjpaapplication.member.dto.CreateMemberResponse;
import com.tommy.jpabook.bootjpaapplication.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping(value = "/api/v1/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long registeredId = memberService.register(member);
        return new CreateMemberResponse(registeredId);
    }

    @PostMapping(value = "/api/v2/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Long registeredId = memberService.register(request.toMember());
        return new CreateMemberResponse(registeredId);
    }
}
