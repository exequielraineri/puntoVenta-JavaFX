/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author exera
 */
@Entity
@Table(name = "factura_detalle")
@NamedQueries({
    @NamedQuery(name = "FacturaDetalle.findAll", query = "SELECT f FROM FacturaDetalle f"),
    @NamedQuery(name = "FacturaDetalle.findById", query = "SELECT f FROM FacturaDetalle f WHERE f.id = :id"),
    @NamedQuery(name = "FacturaDetalle.findByCantidad", query = "SELECT f FROM FacturaDetalle f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacturaDetalle.findBySubtotal", query = "SELECT f FROM FacturaDetalle f WHERE f.subtotal = :subtotal")})
public class FacturaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subtotal")
    private Double subtotal;
    @JoinColumn(name = "id_fac_cabezera", referencedColumnName = "id")
    @ManyToOne
    private FacturaCabezera idFacCabezera;
    @JoinColumn(name = "id_producto", referencedColumnName = "id")
    @ManyToOne
    private Producto idProducto;

    public FacturaDetalle() {
    }

    public FacturaDetalle(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public FacturaCabezera getIdFacCabezera() {
        return idFacCabezera;
    }

    public void setIdFacCabezera(FacturaCabezera idFacCabezera) {
        this.idFacCabezera = idFacCabezera;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaDetalle)) {
            return false;
        }
        FacturaDetalle other = (FacturaDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.raineri.puntoventa.Entity.FacturaDetalle[ id=" + id + " ]";
    }
    
}
