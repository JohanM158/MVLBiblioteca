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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private static final int MAX_ACTIVE_LOANS_PER_USER = 3;
    private static final int LOAN_DAYS = 14;

    public List<LoanDTO> findAll() {
        return loanRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LoanDTO findById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo", id));
        return toDTO(loan);
    }

    public List<LoanDTO> findByUserId(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanDTO create(LoanDTO dto) {
        // 1. Find book and user
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro", dto.getBookId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getUserId()));

        // 2. Business rules validation
        if (!book.getAvailable()) {
            throw new BusinessException("El libro '" + book.getTitle() + "' no está disponible para préstamo");
        }

        if (!user.getActive()) {
            throw new BusinessException("El usuario '" + user.getFirstName() + " " + user.getLastName() + "' está inactivo");
        }

        long activeLoans = loanRepository.countByUserIdAndStatus(user.getId(), "ACTIVE");
        if (activeLoans >= MAX_ACTIVE_LOANS_PER_USER) {
            throw new BusinessException("El usuario ya tiene " + MAX_ACTIVE_LOANS_PER_USER + " préstamos activos. Debe devolver un libro antes de pedir otro.");
        }

        // 3. Create loan
        LocalDate loanDate = LocalDate.now();
        Loan loan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(loanDate)
                .dueDate(loanDate.plusDays(LOAN_DAYS))
                .status("ACTIVE")
                .notes(dto.getNotes())
                .build();

        // 4. Mark book as unavailable
        book.setAvailable(false);
        bookRepository.save(book);

        Loan saved = loanRepository.save(loan);
        return toDTO(saved);
    }

    @Transactional
    public LoanDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo", loanId));

        if (!"ACTIVE".equals(loan.getStatus()) && !"OVERDUE".equals(loan.getStatus())) {
            throw new BusinessException("Este préstamo ya ha sido devuelto");
        }

        // Mark as returned
        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");

        // Make book available again
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        Loan saved = loanRepository.save(loan);
        return toDTO(saved);
    }

    // ---- Mapping ----

    public LoanDTO toDTO(Loan loan) {
        return LoanDTO.builder()
                .id(loan.getId())
                .bookId(loan.getBook().getId())
                .userId(loan.getUser().getId())
                .bookTitle(loan.getBook().getTitle())
                .bookAuthor(loan.getBook().getAuthor())
                .userName(loan.getUser().getFirstName() + " " + loan.getUser().getLastName())
                .userEmail(loan.getUser().getEmail())
                .loanDate(loan.getLoanDate().toString())
                .dueDate(loan.getDueDate().toString())
                .returnDate(loan.getReturnDate() != null ? loan.getReturnDate().toString() : null)
                .status(loan.getStatus())
                .notes(loan.getNotes())
                .build();
    }
}
