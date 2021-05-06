package com.tommy.jpabook.bootjpaapplication.member.controller;

import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.dto.*;
import com.tommy.jpabook.bootjpaapplication.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping(value = "/api/v1/members")
    public List<Member> findMembers() {
        return memberService.findMembers();
    }

    @GetMapping(value = "/api/v2/members")
    public Result<List<ReadMemberResponse>> findMembersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<ReadMemberResponse> memberResponses = findMembers.stream()
                .map(member -> new ReadMemberResponse(member.getName()))
                .collect(Collectors.toList());

        return new Result<>(memberResponses);
    }

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

    @PutMapping(value = "/api/v2/members/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findById(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
}
