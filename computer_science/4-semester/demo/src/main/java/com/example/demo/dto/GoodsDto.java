package com.example.demo.dto;

import com.example.demo.entity.Category;
import com.example.demo.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto {

    private String name;
    private Integer price;
    private Category[] categories;

    public Goods to() {
        return Goods.builder().name(name).categories(Arrays.asList(categories)).price(price).build();
    }
}
