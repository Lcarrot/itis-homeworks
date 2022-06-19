package ru.itis.tyshenko.service;

import org.springframework.stereotype.Service;
import ru.itis.tyshenko.entity.Book;
import java.util.List;

@Service
public interface BookService {

    List<Book> getAllBooks();
    Book saveBook(Book book);
}
