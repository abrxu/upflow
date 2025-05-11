package com.abrxu.upflow.services;

import com.abrxu.upflow.models.user.UserCredentials;
import com.abrxu.upflow.repositories.UserCredentialsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService {

    final UserCredentialsRepository userCredentialsRepository;

    public UserCredentialsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public UserCredentials getUserCredentialsById(Long id) {
        return userCredentialsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User credentials not found."));
    }
}