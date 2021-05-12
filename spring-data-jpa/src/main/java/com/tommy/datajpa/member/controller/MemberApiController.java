package com.tommy.datajpa.member.controller;

import com.tommy.datajpa.member.domain.Member;
import com.tommy.datajpa.member.domain.MemberRepository;
import com.tommy.datajpa.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        MemberDto memberDto = new MemberDto(member);
        return memberDto.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * http://localhost:8080/members?page=0&size=3&sort=username,desc
     * 위의 이런 쿼리스트링으로 페이징 조건을 명시해 줄 수 있다.
     * @param pageable
     * @return
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(MemberDto::new);
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("tommy" + i, i));
        }

    }
}
