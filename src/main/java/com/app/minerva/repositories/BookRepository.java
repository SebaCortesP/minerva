package com.app.minerva.repositories;

import com.app.minerva.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Consultas personalizadas opcionales
    List<Book> findByGenre(String genre);
    List<Book> findByAuthor(String author);
}
