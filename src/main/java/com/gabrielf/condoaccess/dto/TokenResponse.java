package com.gabrielf.condoaccess.dto;

public record TokenResponse(
        String token,
        String type
) {
    public static TokenResponse of(String token) {
        return new TokenResponse(token, "Bearer");

    }
}
