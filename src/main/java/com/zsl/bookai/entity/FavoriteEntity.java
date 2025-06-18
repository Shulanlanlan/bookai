package com.zsl.bookai.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "favorite")
public class FavoriteEntity {
    @Id
    public Long id;

    @Column(value = "user_id")
    public Long userId;

    @Column(value = "book_id")
    public Long bookId;
}