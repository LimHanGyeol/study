package com.tommy.jpabook.bootjpaapplication.web;

import com.tommy.jpabook.bootjpaapplication.member.domain.Address;
import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.dto.MemberRegisterRequest;
import com.tommy.jpabook.bootjpaapplication.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberPageController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberRegisterRequest", new MemberRegisterRequest());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberRegisterRequest memberRegisterRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(memberRegisterRequest.getCity(), memberRegisterRequest.getStreet(), memberRegisterRequest.getZipcode());

        Member member = new Member(memberRegisterRequest.getName(), address);
        memberService.register(member);

        return "redirect:/";
    }
}
