package com.uydev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, updatable = false)
    public LocalDateTime insertDateTime;

    @Column(nullable = false, updatable = false)
    public Long insertUserId;

    @Column(nullable = false)
    public LocalDateTime lastUpdateDateTime;

    @Column(nullable = false)
    public Long lastUpdateUserId;

    public Boolean isDeleted = false;
}