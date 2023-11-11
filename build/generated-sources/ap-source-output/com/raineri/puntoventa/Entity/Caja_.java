package com.raineri.puntoventa.Entity;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-11T17:23:20", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Caja.class)
public class Caja_ { 

    public static volatile SingularAttribute<Caja, String> descripcion;
    public static volatile SingularAttribute<Caja, String> tipo;
    public static volatile SingularAttribute<Caja, Date> fechaRegistro;
    public static volatile SingularAttribute<Caja, Integer> id;
    public static volatile SingularAttribute<Caja, Double> importe;

}