package com.tommy.jpa.basic.jpql;

import java.util.Objects;

public class MemberDTO {

    private final String username;
    private final int age;

    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO memberDTO = (MemberDTO) o;
        return getAge() == memberDTO.getAge() &&
                Objects.equals(getUsername(), memberDTO.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAge());
    }
}
