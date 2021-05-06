package com.tommy.jpabook.bootjpaapplication.member.service;

import com.tommy.jpabook.bootjpaapplication.member.domain.Member;
import com.tommy.jpabook.bootjpaapplication.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Long register(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * update는 변경성 메서드인데,
     * Member를 반환할 경우 조회를 하면서 커맨드와 쿼리가 공존하게 된다.
     * 그래서 update는 그냥 void로 하여 Transaction으로 끝내버리고,
     * Controller에서 새롭게 조회를 하는 방법을 권장한다. 아니면 id 정도만 반환해도 좋다.
     */
    public void update(Long id, String name) {
        Member findMember = findById(id);
        findMember.updateName(name);
    }

    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
