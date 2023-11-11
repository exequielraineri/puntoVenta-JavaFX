/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.Jpa;

import com.raineri.puntoventa.Entity.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.raineri.puntoventa.Entity.FacturaCabezera;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author exera
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

     public ClienteJpaController() {
        emf = Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }

    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getFacturaCabezeraList() == null) {
            cliente.setFacturaCabezeraList(new ArrayList<FacturaCabezera>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FacturaCabezera> attachedFacturaCabezeraList = new ArrayList<FacturaCabezera>();
            for (FacturaCabezera facturaCabezeraListFacturaCabezeraToAttach : cliente.getFacturaCabezeraList()) {
                facturaCabezeraListFacturaCabezeraToAttach = em.getReference(facturaCabezeraListFacturaCabezeraToAttach.getClass(), facturaCabezeraListFacturaCabezeraToAttach.getId());
                attachedFacturaCabezeraList.add(facturaCabezeraListFacturaCabezeraToAttach);
            }
            cliente.setFacturaCabezeraList(attachedFacturaCabezeraList);
            em.persist(cliente);
            for (FacturaCabezera facturaCabezeraListFacturaCabezera : cliente.getFacturaCabezeraList()) {
                Cliente oldIdClienteOfFacturaCabezeraListFacturaCabezera = facturaCabezeraListFacturaCabezera.getIdCliente();
                facturaCabezeraListFacturaCabezera.setIdCliente(cliente);
                facturaCabezeraListFacturaCabezera = em.merge(facturaCabezeraListFacturaCabezera);
                if (oldIdClienteOfFacturaCabezeraListFacturaCabezera != null) {
                    oldIdClienteOfFacturaCabezeraListFacturaCabezera.getFacturaCabezeraList().remove(facturaCabezeraListFacturaCabezera);
                    oldIdClienteOfFacturaCabezeraListFacturaCabezera = em.merge(oldIdClienteOfFacturaCabezeraListFacturaCabezera);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            List<FacturaCabezera> facturaCabezeraListOld = persistentCliente.getFacturaCabezeraList();
            List<FacturaCabezera> facturaCabezeraListNew = cliente.getFacturaCabezeraList();
            List<FacturaCabezera> attachedFacturaCabezeraListNew = new ArrayList<FacturaCabezera>();
            for (FacturaCabezera facturaCabezeraListNewFacturaCabezeraToAttach : facturaCabezeraListNew) {
                facturaCabezeraListNewFacturaCabezeraToAttach = em.getReference(facturaCabezeraListNewFacturaCabezeraToAttach.getClass(), facturaCabezeraListNewFacturaCabezeraToAttach.getId());
                attachedFacturaCabezeraListNew.add(facturaCabezeraListNewFacturaCabezeraToAttach);
            }
            facturaCabezeraListNew = attachedFacturaCabezeraListNew;
            cliente.setFacturaCabezeraList(facturaCabezeraListNew);
            cliente = em.merge(cliente);
            for (FacturaCabezera facturaCabezeraListOldFacturaCabezera : facturaCabezeraListOld) {
                if (!facturaCabezeraListNew.contains(facturaCabezeraListOldFacturaCabezera)) {
                    facturaCabezeraListOldFacturaCabezera.setIdCliente(null);
                    facturaCabezeraListOldFacturaCabezera = em.merge(facturaCabezeraListOldFacturaCabezera);
                }
            }
            for (FacturaCabezera facturaCabezeraListNewFacturaCabezera : facturaCabezeraListNew) {
                if (!facturaCabezeraListOld.contains(facturaCabezeraListNewFacturaCabezera)) {
                    Cliente oldIdClienteOfFacturaCabezeraListNewFacturaCabezera = facturaCabezeraListNewFacturaCabezera.getIdCliente();
                    facturaCabezeraListNewFacturaCabezera.setIdCliente(cliente);
                    facturaCabezeraListNewFacturaCabezera = em.merge(facturaCabezeraListNewFacturaCabezera);
                    if (oldIdClienteOfFacturaCabezeraListNewFacturaCabezera != null && !oldIdClienteOfFacturaCabezeraListNewFacturaCabezera.equals(cliente)) {
                        oldIdClienteOfFacturaCabezeraListNewFacturaCabezera.getFacturaCabezeraList().remove(facturaCabezeraListNewFacturaCabezera);
                        oldIdClienteOfFacturaCabezeraListNewFacturaCabezera = em.merge(oldIdClienteOfFacturaCabezeraListNewFacturaCabezera);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<FacturaCabezera> facturaCabezeraList = cliente.getFacturaCabezeraList();
            for (FacturaCabezera facturaCabezeraListFacturaCabezera : facturaCabezeraList) {
                facturaCabezeraListFacturaCabezera.setIdCliente(null);
                facturaCabezeraListFacturaCabezera = em.merge(facturaCabezeraListFacturaCabezera);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<Cliente> findClienteEntitiesFiltro(String text) {
        EntityManager em = getEntityManager();
        try {
            Query sql = em.createQuery("SELECT c FROM Cliente c WHERE c.nombre LIKE :nombre OR c.apellido LIKE :apellido OR c.cuit LIKE :cuit");
            sql.setParameter("nombre", "%"+text+"%");
            sql.setParameter("apellido","%"+text+"%");
            sql.setParameter("cuit", "%"+text+"%");
            return sql.getResultList();
        } finally {
            em.close();
        }
    }
    
}
