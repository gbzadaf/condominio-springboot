package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.LoginRequest;
import com.gabrielf.condoaccess.dto.TokenResponse;
import com.gabrielf.condoaccess.dto.UserRequest;
import com.gabrielf.condoaccess.dto.UserResponse;
import com.gabrielf.condoaccess.security.JwtService;
import com.gabrielf.condoaccess.security.UserDetailsServiceImpl;
import com.gabrielf.condoaccess.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints de autenticação — registro e login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final UserService userService;


    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário", description = "Retorna um token JWT válido por 24 horas")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(TokenResponse.of(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário",
            description = "Cria o usuário no sistema. É necessário fazer login depois para obter o token.")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));

    }

}
