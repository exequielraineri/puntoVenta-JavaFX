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

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
    public ProveedorJpaController(){
        emf = Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }

    public void create(Proveedor proveedor) {
        if (proveedor.getProductoList() == null) {
            proveedor.setProductoList(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : proveedor.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getId());
                attachedProductoList.add(productoListProductoToAttach);
            }
            proveedor.setProductoList(attachedProductoList);
            em.persist(proveedor);
            for (Producto productoListProducto : proveedor.getProductoList()) {
                Proveedor oldProveedorOfProductoListProducto = productoListProducto.getProveedor();
                productoListProducto.setProveedor(proveedor);
                productoListProducto = em.merge(productoListProducto);
                if (oldProveedorOfProductoListProducto != null) {
                    oldProveedorOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldProveedorOfProductoListProducto = em.merge(oldProveedorOfProductoListProducto);
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
            List<Producto> productoListOld = persistentProveedor.getProductoList();
            List<Producto> productoListNew = proveedor.getProductoList();
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getId());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            proveedor.setProductoList(productoListNew);
            proveedor = em.merge(proveedor);
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    productoListOldProducto.setProveedor(null);
                    productoListOldProducto = em.merge(productoListOldProducto);
                }
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    Proveedor oldProveedorOfProductoListNewProducto = productoListNewProducto.getProveedor();
                    productoListNewProducto.setProveedor(proveedor);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldProveedorOfProductoListNewProducto != null && !oldProveedorOfProductoListNewProducto.equals(proveedor)) {
                        oldProveedorOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldProveedorOfProductoListNewProducto = em.merge(oldProveedorOfProductoListNewProducto);
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
            List<Producto> productoList = proveedor.getProductoList();
            for (Producto productoListProducto : productoList) {
                productoListProducto.setProveedor(null);
                productoListProducto = em.merge(productoListProducto);
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
