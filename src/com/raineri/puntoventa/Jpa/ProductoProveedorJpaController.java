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
import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Entity.ProductoProveedor;
import com.raineri.puntoventa.Entity.Proveedor;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author exera
 */
public class ProductoProveedorJpaController implements Serializable {

    public ProductoProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ProductoProveedorJpaController() {
    emf=Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }
    
    

    public void create(ProductoProveedor productoProveedor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = productoProveedor.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getId());
                productoProveedor.setIdProducto(idProducto);
            }
            Proveedor idProveedor = productoProveedor.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getId());
                productoProveedor.setIdProveedor(idProveedor);
            }
            em.persist(productoProveedor);
            if (idProducto != null) {
                idProducto.getProductoProveedorList().add(productoProveedor);
                idProducto = em.merge(idProducto);
            }
            if (idProveedor != null) {
                idProveedor.getProductoProveedorList().add(productoProveedor);
                idProveedor = em.merge(idProveedor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoProveedor productoProveedor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoProveedor persistentProductoProveedor = em.find(ProductoProveedor.class, productoProveedor.getId());
            Producto idProductoOld = persistentProductoProveedor.getIdProducto();
            Producto idProductoNew = productoProveedor.getIdProducto();
            Proveedor idProveedorOld = persistentProductoProveedor.getIdProveedor();
            Proveedor idProveedorNew = productoProveedor.getIdProveedor();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getId());
                productoProveedor.setIdProducto(idProductoNew);
            }
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getId());
                productoProveedor.setIdProveedor(idProveedorNew);
            }
            productoProveedor = em.merge(productoProveedor);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getProductoProveedorList().remove(productoProveedor);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getProductoProveedorList().add(productoProveedor);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getProductoProveedorList().remove(productoProveedor);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getProductoProveedorList().add(productoProveedor);
                idProveedorNew = em.merge(idProveedorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productoProveedor.getId();
                if (findProductoProveedor(id) == null) {
                    throw new NonexistentEntityException("The productoProveedor with id " + id + " no longer exists.");
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
            ProductoProveedor productoProveedor;
            try {
                productoProveedor = em.getReference(ProductoProveedor.class, id);
                productoProveedor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoProveedor with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = productoProveedor.getIdProducto();
            if (idProducto != null) {
                idProducto.getProductoProveedorList().remove(productoProveedor);
                idProducto = em.merge(idProducto);
            }
            Proveedor idProveedor = productoProveedor.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getProductoProveedorList().remove(productoProveedor);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(productoProveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductoProveedor> findProductoProveedorEntities() {
        return findProductoProveedorEntities(true, -1, -1);
    }

    public List<ProductoProveedor> findProductoProveedorEntities(int maxResults, int firstResult) {
        return findProductoProveedorEntities(false, maxResults, firstResult);
    }

    private List<ProductoProveedor> findProductoProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoProveedor.class));
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

    public ProductoProveedor findProductoProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoProveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoProveedor> rt = cq.from(ProductoProveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
