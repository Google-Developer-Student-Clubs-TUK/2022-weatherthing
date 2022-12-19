package com.weatherthing.project.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 변경되었을 때 자동으로 기록합니다.
public abstract class BaseTimeEntity {

    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 변경 시점
    private LocalDateTime updateAt;


}
