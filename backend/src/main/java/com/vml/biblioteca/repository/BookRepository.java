package com.vml.biblioteca.repository;

import com.vml.biblioteca.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAvailable(Boolean available);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    @Query("SELECT DISTINCT b.genre FROM Book b WHERE b.genre IS NOT NULL ORDER BY b.genre")
    List<String> findAllGenres();

    long countByAvailable(Boolean available);

    @Query("SELECT b.genre, COUNT(b) FROM Book b WHERE b.genre IS NOT NULL GROUP BY b.genre ORDER BY COUNT(b) DESC")
    List<Object[]> countByGenre();
}
