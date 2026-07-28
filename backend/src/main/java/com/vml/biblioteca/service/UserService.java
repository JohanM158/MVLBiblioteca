package com.vml.biblioteca.service;

import com.vml.biblioteca.dto.UserDTO;
import com.vml.biblioteca.entity.User;
import com.vml.biblioteca.exception.BusinessException;
import com.vml.biblioteca.exception.ResourceNotFoundException;
import com.vml.biblioteca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return toDTO(user);
    }

    public List<UserDTO> search(String query) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO create(UserDTO dto) {
        // Validate unique email
        userRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
            throw new BusinessException("Ya existe un usuario con el email: " + dto.getEmail());
        });

        User user = toEntity(dto);
        user.setActive(true);
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public UserDTO update(Long id, UserDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        // Validate unique email (if changed)
        if (!dto.getEmail().equals(existing.getEmail())) {
            userRepository.findByEmail(dto.getEmail()).ifPresent(other -> {
                throw new BusinessException("Ya existe un usuario con el email: " + dto.getEmail());
            });
        }

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        if (dto.getActive() != null) {
            existing.setActive(dto.getActive());
        }

        User saved = userRepository.save(existing);
        return toDTO(saved);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        userRepository.deleteById(id);
    }

    // ---- Mapping helpers ----

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .membershipId(user.getMembershipId())
                .registrationDate(user.getRegistrationDate() != null ? user.getRegistrationDate().toString() : null)
                .active(user.getActive())
                .build();
    }

    private User toEntity(UserDTO dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }
}
