package com.sigclin.backend.dto.auth;

import lombok.Data;

@Data
public class RegistroRequest {
    private String email;
    private String password;
    private String nombres;
    private String apellidos;
}
