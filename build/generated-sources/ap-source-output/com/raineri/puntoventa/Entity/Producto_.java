package com.raineri.puntoventa.Entity;

import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Entity.ProductoProveedor;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-10T17:52:47", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile SingularAttribute<Producto, String> descripcion;
    public static volatile SingularAttribute<Producto, Double> precio;
    public static volatile SingularAttribute<Producto, Short> estado;
    public static volatile SingularAttribute<Producto, String> codigo;
    public static volatile SingularAttribute<Producto, String> categoria;
    public static volatile ListAttribute<Producto, FacturaDetalle> facturaDetalleList;
    public static volatile ListAttribute<Producto, ProductoProveedor> productoProveedorList;
    public static volatile SingularAttribute<Producto, Integer> id;
    public static volatile SingularAttribute<Producto, Integer> stock;

}