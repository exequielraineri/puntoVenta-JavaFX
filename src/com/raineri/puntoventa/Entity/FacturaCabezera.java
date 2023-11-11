/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.Entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author exera
 */
@Entity
@Table(name = "factura_cabezera")
@NamedQueries({
    @NamedQuery(name = "FacturaCabezera.findAll", query = "SELECT f FROM FacturaCabezera f"),
    @NamedQuery(name = "FacturaCabezera.findById", query = "SELECT f FROM FacturaCabezera f WHERE f.id = :id"),
    @NamedQuery(name = "FacturaCabezera.findByNroFactura", query = "SELECT f FROM FacturaCabezera f WHERE f.nroFactura = :nroFactura"),
    @NamedQuery(name = "FacturaCabezera.findByFechaEmision", query = "SELECT f FROM FacturaCabezera f WHERE f.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "FacturaCabezera.findByEstado", query = "SELECT f FROM FacturaCabezera f WHERE f.estado = :estado")})
public class FacturaCabezera implements Serializable {

    @JoinColumn(name = "idCliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente idCliente;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "iva")
    private Double iva;

    @Column(name = "metodo_pago")
    private String metodoPago;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nro_factura")
    private String nroFactura;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "estado")
    private Short estado;
    @OneToMany(mappedBy = "idFacCabezera")
    private List<FacturaDetalle> facturaDetalleList=new ArrayList<>();
    
    @Transient
    private double total;

    public double getTotal() {
        total=0.0;
        for (FacturaDetalle detalle : facturaDetalleList) {
            total+=detalle.getSubtotal();
        }
        total=(total*iva)/100+total;
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    
    
    
    public FacturaCabezera() {
    }

    public FacturaCabezera(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }
    
    public String getFechaEmisionS() {
        SimpleDateFormat sf=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return sf.format(fechaEmision);
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public List<FacturaDetalle> getFacturaDetalleList() {
        return facturaDetalleList;
    }

    public void setFacturaDetalleList(List<FacturaDetalle> facturaDetalleList) {
        this.facturaDetalleList = facturaDetalleList;
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
        if (!(object instanceof FacturaCabezera)) {
            return false;
        }
        FacturaCabezera other = (FacturaCabezera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.raineri.puntoventa.Entity.FacturaCabezera[ id=" + id + " ]";
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }
    
}
