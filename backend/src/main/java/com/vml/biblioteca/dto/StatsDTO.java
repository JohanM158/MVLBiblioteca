package com.vml.biblioteca.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDTO {

    // Summary stats
    private Long totalBooks;
    private Long totalUsers;
    private Long activeLoans;
    private Long overdueLoans;
    private Long availableBooks;

    // Top books (most loaned)
    private List<TopBookDTO> topBooks;

    // Loans by month
    private List<MonthlyLoanDTO> loansByMonth;

    // Distribution by genre
    private List<GenreDistributionDTO> genreDistribution;

    // Most active users
    private List<ActiveUserDTO> activeUsers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopBookDTO {
        private Long bookId;
        private String title;
        private String author;
        private Long loanCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyLoanDTO {
        private String month;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenreDistributionDTO {
        private String genre;
        private Long count;
        private Double percentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActiveUserDTO {
        private Long userId;
        private String name;
        private String email;
        private Long loanCount;
    }
}
