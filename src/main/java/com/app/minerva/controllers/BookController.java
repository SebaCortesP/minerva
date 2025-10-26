package com.app.minerva.controllers;


import com.app.minerva.models.Book;
import com.app.minerva.dto.ApiResponse;
import com.app.minerva.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // GET - Listar todos los libros
    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de libros obtenida", books));
    }

    // GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok(new ApiResponse<>(true, "Libro encontrado", book)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "Libro no encontrado", null)));
    }

    // POST - Crear nuevo libro
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@RequestBody Book book) {
        Book saved = bookRepository.save(book);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(true, "Libro creado exitosamente", saved));
    }

    // PUT - Actualizar libro existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setGenre(updatedBook.getGenre());
                    book.setPublishedDate(updatedBook.getPublishedDate());
                    Book saved = bookRepository.save(book);
                    return ResponseEntity.ok(new ApiResponse<>(true, "Libro actualizado", saved));
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "Libro no encontrado", null)));
    }

    // DELETE - Eliminar libro
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Libro eliminado", null));
        } else {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(false, "Libro no encontrado", null));
        }
    }

    // GET opcional - Buscar libros por género
    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<List<Book>>> getBooksByGenre(@PathVariable String genre) {
        List<Book> books = bookRepository.findByGenre(genre);
        return ResponseEntity.ok(new ApiResponse<>(true, "Libros por género obtenidos", books));
    }

    // GET opcional - Buscar libros por autor
    @GetMapping("/author/{author}")
    public ResponseEntity<ApiResponse<List<Book>>> getBooksByAuthor(@PathVariable String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return ResponseEntity.ok(new ApiResponse<>(true, "Libros por autor obtenidos", books));
    }
}

