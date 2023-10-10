package com.raineri.puntoventa.Entity;

import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Entity.Proveedor;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-10-10T17:52:47", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(ProductoProveedor.class)
public class ProductoProveedor_ { 

    public static volatile SingularAttribute<ProductoProveedor, Proveedor> idProveedor;
    public static volatile SingularAttribute<ProductoProveedor, Integer> id;
    public static volatile SingularAttribute<ProductoProveedor, Producto> idProducto;

}