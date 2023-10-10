package com.raineri.puntoventa.Entity;

import com.raineri.puntoventa.Entity.FacturaDetalle;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-10T17:52:47", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(FacturaCabezera.class)
public class FacturaCabezera_ { 

    public static volatile SingularAttribute<FacturaCabezera, Short> estado;
    public static volatile SingularAttribute<FacturaCabezera, String> nroFactura;
    public static volatile ListAttribute<FacturaCabezera, FacturaDetalle> facturaDetalleList;
    public static volatile SingularAttribute<FacturaCabezera, Date> fechaEmision;
    public static volatile SingularAttribute<FacturaCabezera, Integer> id;

}