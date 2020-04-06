package com.david.springboot.app.models.dao;

import com.david.springboot.app.models.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
}
