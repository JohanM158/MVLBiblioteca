package com.vml.biblioteca.repository;

import com.vml.biblioteca.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(String status);

    List<Loan> findByUserId(Long userId);

    List<Loan> findByBookId(Long bookId);

    long countByStatus(String status);

    long countByUserIdAndStatus(Long userId, String status);

    // Find overdue loans (active loans past due date)
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate < :today")
    List<Loan> findOverdueLoans(@Param("today") LocalDate today);

    // Count overdue loans
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate < :today")
    long countOverdueLoans(@Param("today") LocalDate today);

    // Top books by loan count
    @Query("SELECT l.book.id, l.book.title, l.book.author, COUNT(l) as cnt " +
           "FROM Loan l GROUP BY l.book.id, l.book.title, l.book.author ORDER BY cnt DESC")
    List<Object[]> findTopBooks();

    // Loans by month (last 6 months)
    @Query("SELECT FUNCTION('MONTH', l.loanDate) as m, FUNCTION('YEAR', l.loanDate) as y, COUNT(l) " +
           "FROM Loan l WHERE l.loanDate >= :startDate GROUP BY y, m ORDER BY y, m")
    List<Object[]> findLoansByMonth(@Param("startDate") LocalDate startDate);

    // Most active users
    @Query("SELECT l.user.id, CONCAT(l.user.firstName, ' ', l.user.lastName), l.user.email, COUNT(l) as cnt " +
           "FROM Loan l GROUP BY l.user.id, l.user.firstName, l.user.lastName, l.user.email ORDER BY cnt DESC")
    List<Object[]> findMostActiveUsers();
}
