package com.raineri.puntoventa.Entity;

import com.raineri.puntoventa.Entity.FacturaCabezera;
import com.raineri.puntoventa.Entity.Producto;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-10T17:52:47", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(FacturaDetalle.class)
public class FacturaDetalle_ { 

    public static volatile SingularAttribute<FacturaDetalle, Double> subtotal;
    public static volatile SingularAttribute<FacturaDetalle, Integer> id;
    public static volatile SingularAttribute<FacturaDetalle, Integer> cantidad;
    public static volatile SingularAttribute<FacturaDetalle, Producto> idProducto;
    public static volatile SingularAttribute<FacturaDetalle, FacturaCabezera> idFacCabezera;

}