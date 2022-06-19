package ru.itis.tyshenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.itis.tyshenko.entity.Book;
import ru.itis.tyshenko.repository.BookRepository;

import java.util.List;

public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
}
