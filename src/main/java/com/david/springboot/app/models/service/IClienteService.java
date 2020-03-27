package com.david.springboot.app.models.service;

import com.david.springboot.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();
    public Page<Cliente> findAll(Pageable pageable);
    public Cliente findOne(Long id);
    public void save(Cliente cliente);
    public void delete(Long id);
}
