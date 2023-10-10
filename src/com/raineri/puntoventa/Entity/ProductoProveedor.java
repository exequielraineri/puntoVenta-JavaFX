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
@Table(name = "producto_proveedor")
@NamedQueries({
    @NamedQuery(name = "ProductoProveedor.findAll", query = "SELECT p FROM ProductoProveedor p"),
    @NamedQuery(name = "ProductoProveedor.findById", query = "SELECT p FROM ProductoProveedor p WHERE p.id = :id")})
public class ProductoProveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_producto", referencedColumnName = "id")
    @ManyToOne
    private Producto idProducto;
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id")
    @ManyToOne
    private Proveedor idProveedor;

    public ProductoProveedor() {
    }

    public ProductoProveedor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
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
        if (!(object instanceof ProductoProveedor)) {
            return false;
        }
        ProductoProveedor other = (ProductoProveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.raineri.puntoventa.Entity.ProductoProveedor[ id=" + id + " ]";
    }
    
}
