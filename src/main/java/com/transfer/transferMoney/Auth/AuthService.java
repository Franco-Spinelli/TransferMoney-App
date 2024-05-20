package com.transfer.transferMoney.Auth;

import com.transfer.transferMoney.JWT.JwtService;
import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.User.Role;
import com.transfer.transferMoney.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails userDetails = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        BigInteger cbu = generateRandomNumber();
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .dni(request.getDni())
                .cbu(cbu)
                .moneyAccount(BigDecimal.valueOf(0))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
    public BigInteger generateRandomNumber() {
        final SecureRandom RANDOM = new SecureRandom();
         final int NUM_DIGITS = 22;
        BigInteger min = new BigInteger("1" + "0".repeat(NUM_DIGITS - 1));
        BigInteger max = new BigInteger("9".repeat(NUM_DIGITS));
        BigInteger range = max.subtract(min).add(BigInteger.ONE);

        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(range.bitLength(), RANDOM);
        } while (randomNumber.compareTo(min) < 0 || randomNumber.compareTo(max) > 0);

        return randomNumber;
    }
}
