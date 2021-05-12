package com.tommy.datajpa;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 업무에서 Auditing 을 적용할때 등록, 수정 시간과 등록자, 수정자에서 사용처가 나뉠 경우가 있다.
 * BaseEntity가 BaseTimeEntity를 상속하게 하고 사용하면 특정 케이스의 요구사항을 만족할 수 있다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
