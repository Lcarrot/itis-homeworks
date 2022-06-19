package ru.itis.tyshenko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.itis.tyshenko.converter.CustomConverter;
import ru.itis.tyshenko.entity.Book;
import ru.itis.tyshenko.service.BookService;
import ru.itis.tyshenko.service.CategoryService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomController {

    @Autowired
    public BookService bookService;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public CustomConverter categoryConverter;

    @GetMapping(value = "/")
    public String getBooksPage() {
        return "redirect:/books";
    }

    @GetMapping(value = "/books")
    public String getBooksPage(HttpServletRequest request) {
        request.getSession().setAttribute("books", bookService.getAllBooks());
        System.out.println(bookService.getAllBooks().get(0));
        return "books";
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String getBooksAddPage(HttpServletRequest request) {
        request.getSession().setAttribute("categories", categoryService.findAllCategories());
        return "save_book";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String postBooksAddPage(@ModelAttribute Book currentBook) {
        bookService.saveBook(currentBook);
        return "save_book";
    }
}
