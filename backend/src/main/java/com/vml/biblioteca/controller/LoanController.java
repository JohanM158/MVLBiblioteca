package com.vml.biblioteca.controller;

import com.vml.biblioteca.dto.LoanDTO;
import com.vml.biblioteca.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAll() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<LoanDTO> create(@Valid @RequestBody LoanDTO dto) {
        return new ResponseEntity<>(loanService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }
}
