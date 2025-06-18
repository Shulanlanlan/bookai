package com.zsl.bookai.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user")
@AllArgsConstructor
public class UserEntity {
    @Id
    public Long id;

    @Column(value = "username")
    public String username;

    @Column(value = "password")
    public String password;
}