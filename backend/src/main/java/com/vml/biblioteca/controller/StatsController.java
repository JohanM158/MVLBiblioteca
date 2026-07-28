package com.vml.biblioteca.controller;

import com.vml.biblioteca.dto.StatsDTO;
import com.vml.biblioteca.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsDTO> getAllStats() {
        return ResponseEntity.ok(statsService.getAllStats());
    }

    @GetMapping("/summary")
    public ResponseEntity<StatsDTO> getSummary() {
        return ResponseEntity.ok(statsService.getSummary());
    }

    @GetMapping("/top-books")
    public ResponseEntity<List<StatsDTO.TopBookDTO>> getTopBooks() {
        return ResponseEntity.ok(statsService.getTopBooks());
    }

    @GetMapping("/loans-by-month")
    public ResponseEntity<List<StatsDTO.MonthlyLoanDTO>> getLoansByMonth() {
        return ResponseEntity.ok(statsService.getLoansByMonth());
    }

    @GetMapping("/genre-distribution")
    public ResponseEntity<List<StatsDTO.GenreDistributionDTO>> getGenreDistribution() {
        return ResponseEntity.ok(statsService.getGenreDistribution());
    }

    @GetMapping("/active-users")
    public ResponseEntity<List<StatsDTO.ActiveUserDTO>> getMostActiveUsers() {
        return ResponseEntity.ok(statsService.getMostActiveUsers());
    }
}
