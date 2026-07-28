package com.vml.biblioteca.service;

import com.vml.biblioteca.dto.StatsDTO;
import com.vml.biblioteca.repository.BookRepository;
import com.vml.biblioteca.repository.LoanRepository;
import com.vml.biblioteca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    /**
     * Get complete dashboard summary statistics.
     */
    public StatsDTO getSummary() {
        return StatsDTO.builder()
                .totalBooks(bookRepository.count())
                .totalUsers(userRepository.count())
                .activeLoans(loanRepository.countByStatus("ACTIVE"))
                .overdueLoans(loanRepository.countOverdueLoans(LocalDate.now()))
                .availableBooks(bookRepository.countByAvailable(true))
                .build();
    }

    /**
     * Get top 5 most loaned books.
     */
    public List<StatsDTO.TopBookDTO> getTopBooks() {
        return loanRepository.findTopBooks().stream()
                .limit(5)
                .map(row -> StatsDTO.TopBookDTO.builder()
                        .bookId((Long) row[0])
                        .title((String) row[1])
                        .author((String) row[2])
                        .loanCount((Long) row[3])
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get loans count by month for the last 6 months.
     */
    public List<StatsDTO.MonthlyLoanDTO> getLoansByMonth() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        return loanRepository.findLoansByMonth(sixMonthsAgo).stream()
                .map(row -> {
                    int monthNum = ((Number) row[0]).intValue();
                    int year = ((Number) row[1]).intValue();
                    String monthName = Month.of(monthNum)
                            .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es"));
                    return StatsDTO.MonthlyLoanDTO.builder()
                            .month(monthName + " " + year)
                            .count((Long) row[2])
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Get book distribution by genre with percentages.
     */
    public List<StatsDTO.GenreDistributionDTO> getGenreDistribution() {
        List<Object[]> genreCounts = bookRepository.countByGenre();
        long totalBooks = bookRepository.count();

        return genreCounts.stream()
                .map(row -> StatsDTO.GenreDistributionDTO.builder()
                        .genre((String) row[0])
                        .count((Long) row[1])
                        .percentage(totalBooks > 0 ? Math.round(((Long) row[1]) * 10000.0 / totalBooks) / 100.0 : 0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get top 5 most active users by loan count.
     */
    public List<StatsDTO.ActiveUserDTO> getMostActiveUsers() {
        return loanRepository.findMostActiveUsers().stream()
                .limit(5)
                .map(row -> StatsDTO.ActiveUserDTO.builder()
                        .userId((Long) row[0])
                        .name((String) row[1])
                        .email((String) row[2])
                        .loanCount((Long) row[3])
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get all statistics combined in a single response.
     */
    public StatsDTO getAllStats() {
        StatsDTO summary = getSummary();
        summary.setTopBooks(getTopBooks());
        summary.setLoansByMonth(getLoansByMonth());
        summary.setGenreDistribution(getGenreDistribution());
        summary.setActiveUsers(getMostActiveUsers());
        return summary;
    }
}
