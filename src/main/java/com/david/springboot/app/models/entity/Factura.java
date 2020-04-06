package com.david.springboot.app.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="facturas")
public class Factura implements Serializable {

    public Factura() {
        this.items = new ArrayList<ItemFactura>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @NotEmpty
    public String descripcion;
    public String observacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    public Date createAt;
    //Muchas facturas pertenecen a un cliente
    @ManyToOne(fetch=FetchType.LAZY)
    public Cliente cliente;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    public List<ItemFactura> items;

    @PrePersist
    public void prePersist(){
        createAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observación) {
        this.observacion = observación;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public void addItemFactura(ItemFactura item){
        this.items.add(item);
    }

    public Double getTotal(){
        Double total = 0.0;
        int size = items.size();
        for(int i=0; i < size; i++){
            total += items.get(i).calcularImporte();
        }
        return total;
    }

    private static final long serialVersionUID = 34148975024211396L;
}
