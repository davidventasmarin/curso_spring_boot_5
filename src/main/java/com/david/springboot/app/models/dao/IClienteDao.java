package com.david.springboot.app.models.dao;

import com.david.springboot.app.models.entity.Cliente;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface IClienteDao extends PagingAndSortingRepository<Cliente,Long> {


}
