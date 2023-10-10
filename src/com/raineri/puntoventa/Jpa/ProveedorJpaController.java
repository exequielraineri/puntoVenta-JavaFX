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
import com.raineri.puntoventa.Entity.ProductoProveedor;
import com.raineri.puntoventa.Entity.Proveedor;
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
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public ProveedorJpaController() {
        emf = Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) {
        if (proveedor.getProductoProveedorList() == null) {
            proveedor.setProductoProveedorList(new ArrayList<ProductoProveedor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProductoProveedor> attachedProductoProveedorList = new ArrayList<ProductoProveedor>();
            for (ProductoProveedor productoProveedorListProductoProveedorToAttach : proveedor.getProductoProveedorList()) {
                productoProveedorListProductoProveedorToAttach = em.getReference(productoProveedorListProductoProveedorToAttach.getClass(), productoProveedorListProductoProveedorToAttach.getId());
                attachedProductoProveedorList.add(productoProveedorListProductoProveedorToAttach);
            }
            proveedor.setProductoProveedorList(attachedProductoProveedorList);
            em.persist(proveedor);
            for (ProductoProveedor productoProveedorListProductoProveedor : proveedor.getProductoProveedorList()) {
                Proveedor oldIdProveedorOfProductoProveedorListProductoProveedor = productoProveedorListProductoProveedor.getIdProveedor();
                productoProveedorListProductoProveedor.setIdProveedor(proveedor);
                productoProveedorListProductoProveedor = em.merge(productoProveedorListProductoProveedor);
                if (oldIdProveedorOfProductoProveedorListProductoProveedor != null) {
                    oldIdProveedorOfProductoProveedorListProductoProveedor.getProductoProveedorList().remove(productoProveedorListProductoProveedor);
                    oldIdProveedorOfProductoProveedorListProductoProveedor = em.merge(oldIdProveedorOfProductoProveedorListProductoProveedor);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getId());
            List<ProductoProveedor> productoProveedorListOld = persistentProveedor.getProductoProveedorList();
            List<ProductoProveedor> productoProveedorListNew = proveedor.getProductoProveedorList();
            List<ProductoProveedor> attachedProductoProveedorListNew = new ArrayList<ProductoProveedor>();
            for (ProductoProveedor productoProveedorListNewProductoProveedorToAttach : productoProveedorListNew) {
                productoProveedorListNewProductoProveedorToAttach = em.getReference(productoProveedorListNewProductoProveedorToAttach.getClass(), productoProveedorListNewProductoProveedorToAttach.getId());
                attachedProductoProveedorListNew.add(productoProveedorListNewProductoProveedorToAttach);
            }
            productoProveedorListNew = attachedProductoProveedorListNew;
            proveedor.setProductoProveedorList(productoProveedorListNew);
            proveedor = em.merge(proveedor);
            for (ProductoProveedor productoProveedorListOldProductoProveedor : productoProveedorListOld) {
                if (!productoProveedorListNew.contains(productoProveedorListOldProductoProveedor)) {
                    productoProveedorListOldProductoProveedor.setIdProveedor(null);
                    productoProveedorListOldProductoProveedor = em.merge(productoProveedorListOldProductoProveedor);
                }
            }
            for (ProductoProveedor productoProveedorListNewProductoProveedor : productoProveedorListNew) {
                if (!productoProveedorListOld.contains(productoProveedorListNewProductoProveedor)) {
                    Proveedor oldIdProveedorOfProductoProveedorListNewProductoProveedor = productoProveedorListNewProductoProveedor.getIdProveedor();
                    productoProveedorListNewProductoProveedor.setIdProveedor(proveedor);
                    productoProveedorListNewProductoProveedor = em.merge(productoProveedorListNewProductoProveedor);
                    if (oldIdProveedorOfProductoProveedorListNewProductoProveedor != null && !oldIdProveedorOfProductoProveedorListNewProductoProveedor.equals(proveedor)) {
                        oldIdProveedorOfProductoProveedorListNewProductoProveedor.getProductoProveedorList().remove(productoProveedorListNewProductoProveedor);
                        oldIdProveedorOfProductoProveedorListNewProductoProveedor = em.merge(oldIdProveedorOfProductoProveedorListNewProductoProveedor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedor.getId();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<ProductoProveedor> productoProveedorList = proveedor.getProductoProveedorList();
            for (ProductoProveedor productoProveedorListProductoProveedor : productoProveedorList) {
                productoProveedorListProductoProveedor.setIdProveedor(null);
                productoProveedorListProductoProveedor = em.merge(productoProveedorListProductoProveedor);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Proveedor> findProveedor(String busqueda) {
        EntityManager em = getEntityManager();
        try {

            Query sql = em.createQuery("SELECT p FROM Proveedor p WHERE p.nombre LIKE :nombre OR p.cuit LIKE :cuit");
            sql.setParameter("nombre", "%" + busqueda + "%");
            sql.setParameter("cuit", "%" + busqueda + "%");
            return sql.getResultList();
        } finally {
            em.close();
        }
    }

}
