package ru.itis.tyshenko.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomController {

    @GetMapping("getStatus")
    public String getStatus(Model model) {
        model.addAttribute("name", "leo");
        return "status/404";
    }
}
