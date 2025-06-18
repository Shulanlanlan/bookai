package com.zsl.bookai.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "book")
public class BookEntity {
    @Id
    public Long id;

    @Column(value = "title")
    public String title;

    @Column(value = "author")
    public String author;

    @Column(value = "description")
    public String description;

    @Column(value = "tags")
    public List<String> tags;
}