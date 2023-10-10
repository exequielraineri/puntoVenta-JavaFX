/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.Jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.raineri.puntoventa.Entity.FacturaCabezera;
import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author exera
 */
public class FacturaDetalleJpaController implements Serializable {

    public FacturaDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public FacturaDetalleJpaController() {
        emf = Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public FacturaDetalle create(FacturaDetalle facturaDetalle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaCabezera idFacCabezera = facturaDetalle.getIdFacCabezera();
            if (idFacCabezera != null) {
                idFacCabezera = em.getReference(idFacCabezera.getClass(), idFacCabezera.getId());
                facturaDetalle.setIdFacCabezera(idFacCabezera);
            }
            Producto idProducto = facturaDetalle.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getId());
                facturaDetalle.setIdProducto(idProducto);
            }
            em.persist(facturaDetalle);
            if (idFacCabezera != null) {
                idFacCabezera.getFacturaDetalleList().add(facturaDetalle);
                idFacCabezera = em.merge(idFacCabezera);
            }
            if (idProducto != null) {
                idProducto.getFacturaDetalleList().add(facturaDetalle);
                idProducto = em.merge(idProducto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return facturaDetalle;
    }

    public void edit(FacturaDetalle facturaDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaDetalle persistentFacturaDetalle = em.find(FacturaDetalle.class, facturaDetalle.getId());
            FacturaCabezera idFacCabezeraOld = persistentFacturaDetalle.getIdFacCabezera();
            FacturaCabezera idFacCabezeraNew = facturaDetalle.getIdFacCabezera();
            Producto idProductoOld = persistentFacturaDetalle.getIdProducto();
            Producto idProductoNew = facturaDetalle.getIdProducto();
            if (idFacCabezeraNew != null) {
                idFacCabezeraNew = em.getReference(idFacCabezeraNew.getClass(), idFacCabezeraNew.getId());
                facturaDetalle.setIdFacCabezera(idFacCabezeraNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getId());
                facturaDetalle.setIdProducto(idProductoNew);
            }
            facturaDetalle = em.merge(facturaDetalle);
            if (idFacCabezeraOld != null && !idFacCabezeraOld.equals(idFacCabezeraNew)) {
                idFacCabezeraOld.getFacturaDetalleList().remove(facturaDetalle);
                idFacCabezeraOld = em.merge(idFacCabezeraOld);
            }
            if (idFacCabezeraNew != null && !idFacCabezeraNew.equals(idFacCabezeraOld)) {
                idFacCabezeraNew.getFacturaDetalleList().add(facturaDetalle);
                idFacCabezeraNew = em.merge(idFacCabezeraNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getFacturaDetalleList().remove(facturaDetalle);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getFacturaDetalleList().add(facturaDetalle);
                idProductoNew = em.merge(idProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturaDetalle.getId();
                if (findFacturaDetalle(id) == null) {
                    throw new NonexistentEntityException("The facturaDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaDetalle facturaDetalle;
            try {
                facturaDetalle = em.getReference(FacturaDetalle.class, id);
                facturaDetalle.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaDetalle with id " + id + " no longer exists.", enfe);
            }
            FacturaCabezera idFacCabezera = facturaDetalle.getIdFacCabezera();
            if (idFacCabezera != null) {
                idFacCabezera.getFacturaDetalleList().remove(facturaDetalle);
                idFacCabezera = em.merge(idFacCabezera);
            }
            Producto idProducto = facturaDetalle.getIdProducto();
            if (idProducto != null) {
                idProducto.getFacturaDetalleList().remove(facturaDetalle);
                idProducto = em.merge(idProducto);
            }
            em.remove(facturaDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FacturaDetalle> findFacturaDetalleEntities() {
        return findFacturaDetalleEntities(true, -1, -1);
    }

    public List<FacturaDetalle> findFacturaDetalleEntities(int maxResults, int firstResult) {
        return findFacturaDetalleEntities(false, maxResults, firstResult);
    }

    private List<FacturaDetalle> findFacturaDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FacturaDetalle.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FacturaDetalle findFacturaDetalle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FacturaDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FacturaDetalle> rt = cq.from(FacturaDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
