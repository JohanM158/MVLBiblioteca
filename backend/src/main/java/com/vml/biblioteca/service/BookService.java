package com.vml.biblioteca.service;

import com.vml.biblioteca.dto.BookDTO;
import com.vml.biblioteca.entity.Book;
import com.vml.biblioteca.exception.BusinessException;
import com.vml.biblioteca.exception.ResourceNotFoundException;
import com.vml.biblioteca.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));
        return toDTO(book);
    }

    public List<BookDTO> search(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO create(BookDTO dto) {
        // Validate unique ISBN
        if (dto.getIsbn() != null && !dto.getIsbn().isBlank()) {
            bookRepository.findByIsbn(dto.getIsbn()).ifPresent(existing -> {
                throw new BusinessException("Ya existe un libro con el ISBN: " + dto.getIsbn());
            });
        }

        Book book = toEntity(dto);
        book.setAvailable(true);
        Book saved = bookRepository.save(book);
        return toDTO(saved);
    }

    public BookDTO update(Long id, BookDTO dto) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));

        // Validate unique ISBN (if changed)
        if (dto.getIsbn() != null && !dto.getIsbn().isBlank() && !dto.getIsbn().equals(existing.getIsbn())) {
            bookRepository.findByIsbn(dto.getIsbn()).ifPresent(other -> {
                throw new BusinessException("Ya existe un libro con el ISBN: " + dto.getIsbn());
            });
        }

        existing.setTitle(dto.getTitle());
        existing.setAuthor(dto.getAuthor());
        existing.setIsbn(dto.getIsbn());
        existing.setGenre(dto.getGenre());
        existing.setYear(dto.getYear());
        existing.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) {
            existing.setAvailable(dto.getAvailable());
        }

        Book saved = bookRepository.save(existing);
        return toDTO(saved);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro", id);
        }
        bookRepository.deleteById(id);
    }

    // ---- Mapping helpers ----

    public BookDTO toDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .year(book.getYear())
                .description(book.getDescription())
                .available(book.getAvailable())
                .createdAt(book.getCreatedAt() != null ? book.getCreatedAt().toString() : null)
                .updatedAt(book.getUpdatedAt() != null ? book.getUpdatedAt().toString() : null)
                .build();
    }

    private Book toEntity(BookDTO dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .genre(dto.getGenre())
                .year(dto.getYear())
                .description(dto.getDescription())
                .build();
    }
}
