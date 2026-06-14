package com.sigclin.backend.service.impl;

import com.sigclin.backend.model.Caja;
import com.sigclin.backend.repository.CajaRepository;
import com.sigclin.backend.service.ICajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CajaServiceImpl implements ICajaService {

    @Autowired
    private CajaRepository cajaRepository;

    @Override
    public List<Caja> getCajas() {
        return cajaRepository.findAll();
    }
}