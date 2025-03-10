package com.example.back_end.service;

import com.example.back_end.entity.ERole;
import com.example.back_end.entity.Utilisateur;
import com.example.back_end.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailSenderService emailSenderService;

    @Transactional
    public Utilisateur registerUser(String username, String email, String password, Date dateNaissance, String codePostal, String phoneNumber) {
        Utilisateur user = new Utilisateur();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setDateNaissance(dateNaissance);
        user.setCodePostal(codePostal);
        user.setPhoneNumber(phoneNumber);
        user.setRole(ERole.CLIENT);

        // Générer un token de vérification
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        Utilisateur savedUser = userRepository.save(user);

        // Envoyer l'e-mail de vérification
        emailSenderService.sendVerificationEmail(savedUser);

        return savedUser;
    }
}
