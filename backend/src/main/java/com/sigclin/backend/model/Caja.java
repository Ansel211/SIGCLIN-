package com.sigclin.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "caja")
public class Caja {

    @Id
    @Column(name = "id_caja")
    private Integer idCaja;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura;

    private BigDecimal saldo;
    private String estado;
}