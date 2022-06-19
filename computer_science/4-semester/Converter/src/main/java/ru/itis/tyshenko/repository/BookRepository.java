package ru.itis.tyshenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.tyshenko.entity.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT book FROM Book book JOIN FETCH book.category")
    List<Book> findAllBooks();
}
