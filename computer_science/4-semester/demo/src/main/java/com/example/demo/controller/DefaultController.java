package com.example.demo.controller;

import com.example.demo.dto.GoodsDto;
import com.example.demo.entity.Category;
import com.example.demo.service.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class DefaultController {

    @Autowired
    private DefaultService service;

    @GetMapping("/add")
    public String getPage(Model model) {
        model.addAttribute("categories", service.getCategories());
        return "add";
    }

    @PostMapping("/add")
    public String addGoods(GoodsDto goods) {
        service.addGood(goods);
        return "add";
    }

    @PostMapping("/find")
    public String findGoods(String name, Integer price, Model model) {
        model.addAttribute("goods", service.getGoods(name, price));
        return "find";
    }

    @GetMapping("/find")
    public String getGoods() {
        return "find";
    }
}
