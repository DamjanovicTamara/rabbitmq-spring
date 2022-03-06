package com.example.rabbit_commons.domain;


import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Version
    private Long version;

    protected BaseEntity() {
        id = null;
    }
}