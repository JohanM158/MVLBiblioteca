package com.vml.biblioteca.service;

import com.vml.biblioteca.dto.LoanDTO;
import com.vml.biblioteca.entity.Book;
import com.vml.biblioteca.entity.Loan;
import com.vml.biblioteca.entity.User;
import com.vml.biblioteca.exception.BusinessException;
import com.vml.biblioteca.exception.ResourceNotFoundException;
import com.vml.biblioteca.repository.BookRepository;
import com.vml.biblioteca.repository.LoanRepository;
import com.vml.biblioteca.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoanService Tests")
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private User user;
    private Loan loan;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L).title("Test Book").author("Test Author")
                .available(true).build();

        user = User.builder()
                .id(1L).firstName("María").lastName("González")
                .email("maria@test.com").active(true).build();

        loan = Loan.builder()
                .id(1L).book(book).user(user)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status("ACTIVE").build();
    }

    @Test
    @DisplayName("create loan should succeed with valid data")
    void create_ShouldSucceed_WithValidData() {
        LoanDTO dto = LoanDTO.builder().bookId(1L).userId(1L).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loanRepository.countByUserIdAndStatus(1L, "ACTIVE")).thenReturn(0L);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO result = loanService.create(dto);

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        verify(bookRepository, times(1)).save(any(Book.class)); // Book marked unavailable
    }

    @Test
    @DisplayName("create loan should fail when book not available")
    void create_ShouldFail_WhenBookNotAvailable() {
        book.setAvailable(false);
        LoanDTO dto = LoanDTO.builder().bookId(1L).userId(1L).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BusinessException ex = assertThrows(BusinessException.class, () -> loanService.create(dto));
        assertTrue(ex.getMessage().contains("no está disponible"));
    }

    @Test
    @DisplayName("create loan should fail when user is inactive")
    void create_ShouldFail_WhenUserInactive() {
        user.setActive(false);
        LoanDTO dto = LoanDTO.builder().bookId(1L).userId(1L).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BusinessException ex = assertThrows(BusinessException.class, () -> loanService.create(dto));
        assertTrue(ex.getMessage().contains("inactivo"));
    }

    @Test
    @DisplayName("create loan should fail when user has max active loans")
    void create_ShouldFail_WhenMaxLoansReached() {
        LoanDTO dto = LoanDTO.builder().bookId(1L).userId(1L).build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loanRepository.countByUserIdAndStatus(1L, "ACTIVE")).thenReturn(3L);

        BusinessException ex = assertThrows(BusinessException.class, () -> loanService.create(dto));
        assertTrue(ex.getMessage().contains("3 préstamos activos"));
    }

    @Test
    @DisplayName("create loan should fail when book not found")
    void create_ShouldFail_WhenBookNotFound() {
        LoanDTO dto = LoanDTO.builder().bookId(99L).userId(1L).build();
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.create(dto));
    }

    @Test
    @DisplayName("returnBook should mark loan as returned and make book available")
    void returnBook_ShouldSucceed() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        LoanDTO result = loanService.returnBook(1L);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class)); // Book made available
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("returnBook should fail when loan already returned")
    void returnBook_ShouldFail_WhenAlreadyReturned() {
        loan.setStatus("RETURNED");
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(BusinessException.class, () -> loanService.returnBook(1L));
    }
}
