package com.example.demo.service;

import com.example.demo.dto.GoodsDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Goods;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    public void addGood(GoodsDto goodsDto) {
        Goods goods = goodsDto.to();
        goodsRepository.save(goods);
    }

    public List<Goods> getGoods(String string, Integer price) {
        return goodsRepository.findAllByNameIsStartingWithAndPriceIsLessThanEqual(string, price);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
