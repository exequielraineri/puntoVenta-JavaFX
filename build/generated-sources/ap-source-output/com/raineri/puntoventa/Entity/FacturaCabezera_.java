package com.raineri.puntoventa.Entity;

import com.raineri.puntoventa.Entity.Cliente;
import com.raineri.puntoventa.Entity.FacturaDetalle;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-11T17:23:20", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(FacturaCabezera.class)
public class FacturaCabezera_ { 

    public static volatile SingularAttribute<FacturaCabezera, String> metodoPago;
    public static volatile SingularAttribute<FacturaCabezera, Short> estado;
    public static volatile SingularAttribute<FacturaCabezera, String> nroFactura;
    public static volatile SingularAttribute<FacturaCabezera, Cliente> idCliente;
    public static volatile SingularAttribute<FacturaCabezera, Double> iva;
    public static volatile ListAttribute<FacturaCabezera, FacturaDetalle> facturaDetalleList;
    public static volatile SingularAttribute<FacturaCabezera, Date> fechaEmision;
    public static volatile SingularAttribute<FacturaCabezera, Integer> id;

}