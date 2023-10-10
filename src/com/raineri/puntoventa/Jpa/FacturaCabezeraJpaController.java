/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.Jpa;

import com.raineri.puntoventa.Entity.FacturaCabezera;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author exera
 */
public class FacturaCabezeraJpaController implements Serializable {

    public FacturaCabezeraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public FacturaCabezeraJpaController() {
        emf = Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }

    public FacturaCabezera create(FacturaCabezera facturaCabezera) {
        if (facturaCabezera.getFacturaDetalleList() == null) {
            facturaCabezera.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FacturaDetalle> attachedFacturaDetalleList = new ArrayList<FacturaDetalle>();
            for (FacturaDetalle facturaDetalleListFacturaDetalleToAttach : facturaCabezera.getFacturaDetalleList()) {
                facturaDetalleListFacturaDetalleToAttach = em.getReference(facturaDetalleListFacturaDetalleToAttach.getClass(), facturaDetalleListFacturaDetalleToAttach.getId());
                attachedFacturaDetalleList.add(facturaDetalleListFacturaDetalleToAttach);
            }
            facturaCabezera.setFacturaDetalleList(attachedFacturaDetalleList);
            em.persist(facturaCabezera);
            for (FacturaDetalle facturaDetalleListFacturaDetalle : facturaCabezera.getFacturaDetalleList()) {
                FacturaCabezera oldIdFacCabezeraOfFacturaDetalleListFacturaDetalle = facturaDetalleListFacturaDetalle.getIdFacCabezera();
                facturaDetalleListFacturaDetalle.setIdFacCabezera(facturaCabezera);
                facturaDetalleListFacturaDetalle = em.merge(facturaDetalleListFacturaDetalle);
                if (oldIdFacCabezeraOfFacturaDetalleListFacturaDetalle != null) {
                    oldIdFacCabezeraOfFacturaDetalleListFacturaDetalle.getFacturaDetalleList().remove(facturaDetalleListFacturaDetalle);
                    oldIdFacCabezeraOfFacturaDetalleListFacturaDetalle = em.merge(oldIdFacCabezeraOfFacturaDetalleListFacturaDetalle);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return facturaCabezera;
    }

    public void edit(FacturaCabezera facturaCabezera) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FacturaCabezera persistentFacturaCabezera = em.find(FacturaCabezera.class, facturaCabezera.getId());
            List<FacturaDetalle> facturaDetalleListOld = persistentFacturaCabezera.getFacturaDetalleList();
            List<FacturaDetalle> facturaDetalleListNew = facturaCabezera.getFacturaDetalleList();
            List<FacturaDetalle> attachedFacturaDetalleListNew = new ArrayList<FacturaDetalle>();
            for (FacturaDetalle facturaDetalleListNewFacturaDetalleToAttach : facturaDetalleListNew) {
                facturaDetalleListNewFacturaDetalleToAttach = em.getReference(facturaDetalleListNewFacturaDetalleToAttach.getClass(), facturaDetalleListNewFacturaDetalleToAttach.getId());
                attachedFacturaDetalleListNew.add(facturaDetalleListNewFacturaDetalleToAttach);
            }
            facturaDetalleListNew = attachedFacturaDetalleListNew;
            facturaCabezera.setFacturaDetalleList(facturaDetalleListNew);
            facturaCabezera = em.merge(facturaCabezera);
            for (FacturaDetalle facturaDetalleListOldFacturaDetalle : facturaDetalleListOld) {
                if (!facturaDetalleListNew.contains(facturaDetalleListOldFacturaDetalle)) {
                    facturaDetalleListOldFacturaDetalle.setIdFacCabezera(null);
                    facturaDetalleListOldFacturaDetalle = em.merge(facturaDetalleListOldFacturaDetalle);
                }
            }
            for (FacturaDetalle facturaDetalleListNewFacturaDetalle : facturaDetalleListNew) {
                if (!facturaDetalleListOld.contains(facturaDetalleListNewFacturaDetalle)) {
                    FacturaCabezera oldIdFacCabezeraOfFacturaDetalleListNewFacturaDetalle = facturaDetalleListNewFacturaDetalle.getIdFacCabezera();
                    facturaDetalleListNewFacturaDetalle.setIdFacCabezera(facturaCabezera);
                    facturaDetalleListNewFacturaDetalle = em.merge(facturaDetalleListNewFacturaDetalle);
                    if (oldIdFacCabezeraOfFacturaDetalleListNewFacturaDetalle != null && !oldIdFacCabezeraOfFacturaDetalleListNewFacturaDetalle.equals(facturaCabezera)) {
                        oldIdFacCabezeraOfFacturaDetalleListNewFacturaDetalle.getFacturaDetalleList().remove(facturaDetalleListNewFacturaDetalle);
                        oldIdFacCabezeraOfFacturaDetalleListNewFacturaDetalle = em.merge(oldIdFacCabezeraOfFacturaDetalleListNewFacturaDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturaCabezera.getId();
                if (findFacturaCabezera(id) == null) {
                    throw new NonexistentEntityException("The facturaCabezera with id " + id + " no longer exists.");
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
            FacturaCabezera facturaCabezera;
            try {
                facturaCabezera = em.getReference(FacturaCabezera.class, id);
                facturaCabezera.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaCabezera with id " + id + " no longer exists.", enfe);
            }
            List<FacturaDetalle> facturaDetalleList = facturaCabezera.getFacturaDetalleList();
            for (FacturaDetalle facturaDetalleListFacturaDetalle : facturaDetalleList) {
                facturaDetalleListFacturaDetalle.setIdFacCabezera(null);
                facturaDetalleListFacturaDetalle = em.merge(facturaDetalleListFacturaDetalle);
            }
            em.remove(facturaCabezera);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FacturaCabezera> findFacturaCabezeraEntities() {
        return findFacturaCabezeraEntities(true, -1, -1);
    }

    public List<FacturaCabezera> findFacturaCabezeraEntities(int maxResults, int firstResult) {
        return findFacturaCabezeraEntities(false, maxResults, firstResult);
    }

    private List<FacturaCabezera> findFacturaCabezeraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FacturaCabezera.class));
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

    public FacturaCabezera findFacturaCabezera(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FacturaCabezera.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCabezeraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FacturaCabezera> rt = cq.from(FacturaCabezera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<FacturaCabezera> findFacturaCabezeraFecha(LocalDate desde, LocalDate hasta) {
        EntityManager em = getEntityManager();
        try {
            Query sql = em.createQuery("SELECT f FROM FacturaCabezera f WHERE f.fechaEmision BETWEEN :desde AND :hasta");
            sql.setParameter("desde", Date.valueOf(desde));
            sql.setParameter("hasta", Date.valueOf(hasta));
            return sql.getResultList();

        } finally {
            em.close();
        }
    }

}
