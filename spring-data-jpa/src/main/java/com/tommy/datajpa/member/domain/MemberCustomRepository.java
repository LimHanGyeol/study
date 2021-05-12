package com.tommy.datajpa.member.domain;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findMemberCustom();
}
