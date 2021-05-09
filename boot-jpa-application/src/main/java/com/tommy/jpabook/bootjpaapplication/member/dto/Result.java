package com.tommy.jpabook.bootjpaapplication.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ResponseDTO를 List 로 반환하면 JSON이 배열 형태로 나간다.
 * 배열 형태의 JSON을 유지보수하기 좋게 Result 타입으로 감싸어 Object 타입으로 변경한다.
 * @param <T>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private T data;
}
