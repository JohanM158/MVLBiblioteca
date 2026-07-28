package com.vml.biblioteca.service;

import com.vml.biblioteca.dto.BookDTO;
import com.vml.biblioteca.entity.Book;
import com.vml.biblioteca.exception.BusinessException;
import com.vml.biblioteca.exception.ResourceNotFoundException;
import com.vml.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        book1 = Book.builder()
                .id(1L)
                .title("Cien años de soledad")
                .author("Gabriel García Márquez")
                .isbn("978-0307474728")
                .genre("Realismo Mágico")
                .year(1967)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        book2 = Book.builder()
                .id(2L)
                .title("Don Quijote")
                .author("Miguel de Cervantes")
                .isbn("978-8420412146")
                .genre("Novela")
                .year(1605)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        bookDTO = BookDTO.builder()
                .title("Nuevo Libro")
                .author("Autor Nuevo")
                .isbn("978-1234567890")
                .genre("Ficción")
                .year(2024)
                .build();
    }

    @Test
    @DisplayName("findAll should return all books")
    void findAll_ShouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<BookDTO> result = bookService.findAll();

        assertEquals(2, result.size());
        assertEquals("Cien años de soledad", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should return book when exists")
    void findById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        BookDTO result = bookService.findById(1L);

        assertNotNull(result);
        assertEquals("Cien años de soledad", result.getTitle());
        assertEquals("Gabriel García Márquez", result.getAuthor());
    }

    @Test
    @DisplayName("findById should throw exception when not found")
    void findById_ShouldThrowException_WhenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.findById(99L));
    }

    @Test
    @DisplayName("create should save book successfully")
    void create_ShouldSaveBookSuccessfully() {
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(
                Book.builder()
                        .id(3L)
                        .title(bookDTO.getTitle())
                        .author(bookDTO.getAuthor())
                        .isbn(bookDTO.getIsbn())
                        .genre(bookDTO.getGenre())
                        .year(bookDTO.getYear())
                        .available(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        BookDTO result = bookService.create(bookDTO);

        assertNotNull(result);
        assertEquals("Nuevo Libro", result.getTitle());
        assertTrue(result.getAvailable());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("create should throw exception when ISBN already exists")
    void create_ShouldThrowException_WhenIsbnExists() {
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(Optional.of(book1));

        assertThrows(BusinessException.class, () -> bookService.create(bookDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("update should modify existing book")
    void update_ShouldModifyExistingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        BookDTO updateDTO = BookDTO.builder()
                .title("Título Actualizado")
                .author("Autor Actualizado")
                .isbn("978-0307474728")
                .genre("Drama")
                .year(2024)
                .build();

        BookDTO result = bookService.update(1L, updateDTO);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("delete should remove existing book")
    void delete_ShouldRemoveExistingBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        assertDoesNotThrow(() -> bookService.delete(1L));
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete should throw exception when not found")
    void delete_ShouldThrowException_WhenNotFound() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(99L));
        verify(bookRepository, never()).deleteById(any());
    }
}
