package com.sigclin.backend.controller;

import com.sigclin.backend.model.Caja;
import com.sigclin.backend.service.ICajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/caja")
public class CajaController {

    @Autowired
    private ICajaService cajaService;

    @GetMapping
    public List<Caja> listarCajas() {
        return cajaService.getCajas();
    }
}