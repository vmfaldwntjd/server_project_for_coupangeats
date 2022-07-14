package com.example.demo.src.categories.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryRes {
    private int categoryId;
    private String categoryName;
    private String categoryImageUrl;
}
