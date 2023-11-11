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
import com.raineri.puntoventa.Entity.Proveedor;
import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Entity.Producto;
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
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
    public ProductoJpaController() {
        emf = Persistence.createEntityManagerFactory("PuntoVenta-JavaFXPU");
    }

    
    public void create(Producto producto) {
        if (producto.getFacturaDetalleList() == null) {
            producto.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedor = producto.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getId());
                producto.setProveedor(proveedor);
            }
            List<FacturaDetalle> attachedFacturaDetalleList = new ArrayList<FacturaDetalle>();
            for (FacturaDetalle facturaDetalleListFacturaDetalleToAttach : producto.getFacturaDetalleList()) {
                facturaDetalleListFacturaDetalleToAttach = em.getReference(facturaDetalleListFacturaDetalleToAttach.getClass(), facturaDetalleListFacturaDetalleToAttach.getId());
                attachedFacturaDetalleList.add(facturaDetalleListFacturaDetalleToAttach);
            }
            producto.setFacturaDetalleList(attachedFacturaDetalleList);
            em.persist(producto);
            if (proveedor != null) {
                proveedor.getProductoList().add(producto);
                proveedor = em.merge(proveedor);
            }
            for (FacturaDetalle facturaDetalleListFacturaDetalle : producto.getFacturaDetalleList()) {
                Producto oldIdProductoOfFacturaDetalleListFacturaDetalle = facturaDetalleListFacturaDetalle.getIdProducto();
                facturaDetalleListFacturaDetalle.setIdProducto(producto);
                facturaDetalleListFacturaDetalle = em.merge(facturaDetalleListFacturaDetalle);
                if (oldIdProductoOfFacturaDetalleListFacturaDetalle != null) {
                    oldIdProductoOfFacturaDetalleListFacturaDetalle.getFacturaDetalleList().remove(facturaDetalleListFacturaDetalle);
                    oldIdProductoOfFacturaDetalleListFacturaDetalle = em.merge(oldIdProductoOfFacturaDetalleListFacturaDetalle);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getId());
            Proveedor proveedorOld = persistentProducto.getProveedor();
            Proveedor proveedorNew = producto.getProveedor();
            List<FacturaDetalle> facturaDetalleListOld = persistentProducto.getFacturaDetalleList();
            List<FacturaDetalle> facturaDetalleListNew = producto.getFacturaDetalleList();
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getId());
                producto.setProveedor(proveedorNew);
            }
            List<FacturaDetalle> attachedFacturaDetalleListNew = new ArrayList<FacturaDetalle>();
            for (FacturaDetalle facturaDetalleListNewFacturaDetalleToAttach : facturaDetalleListNew) {
                facturaDetalleListNewFacturaDetalleToAttach = em.getReference(facturaDetalleListNewFacturaDetalleToAttach.getClass(), facturaDetalleListNewFacturaDetalleToAttach.getId());
                attachedFacturaDetalleListNew.add(facturaDetalleListNewFacturaDetalleToAttach);
            }
            facturaDetalleListNew = attachedFacturaDetalleListNew;
            producto.setFacturaDetalleList(facturaDetalleListNew);
            producto = em.merge(producto);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getProductoList().remove(producto);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getProductoList().add(producto);
                proveedorNew = em.merge(proveedorNew);
            }
            for (FacturaDetalle facturaDetalleListOldFacturaDetalle : facturaDetalleListOld) {
                if (!facturaDetalleListNew.contains(facturaDetalleListOldFacturaDetalle)) {
                    facturaDetalleListOldFacturaDetalle.setIdProducto(null);
                    facturaDetalleListOldFacturaDetalle = em.merge(facturaDetalleListOldFacturaDetalle);
                }
            }
            for (FacturaDetalle facturaDetalleListNewFacturaDetalle : facturaDetalleListNew) {
                if (!facturaDetalleListOld.contains(facturaDetalleListNewFacturaDetalle)) {
                    Producto oldIdProductoOfFacturaDetalleListNewFacturaDetalle = facturaDetalleListNewFacturaDetalle.getIdProducto();
                    facturaDetalleListNewFacturaDetalle.setIdProducto(producto);
                    facturaDetalleListNewFacturaDetalle = em.merge(facturaDetalleListNewFacturaDetalle);
                    if (oldIdProductoOfFacturaDetalleListNewFacturaDetalle != null && !oldIdProductoOfFacturaDetalleListNewFacturaDetalle.equals(producto)) {
                        oldIdProductoOfFacturaDetalleListNewFacturaDetalle.getFacturaDetalleList().remove(facturaDetalleListNewFacturaDetalle);
                        oldIdProductoOfFacturaDetalleListNewFacturaDetalle = em.merge(oldIdProductoOfFacturaDetalleListNewFacturaDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getId();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            Proveedor proveedor = producto.getProveedor();
            if (proveedor != null) {
                proveedor.getProductoList().remove(producto);
                proveedor = em.merge(proveedor);
            }
            List<FacturaDetalle> facturaDetalleList = producto.getFacturaDetalleList();
            for (FacturaDetalle facturaDetalleListFacturaDetalle : facturaDetalleList) {
                facturaDetalleListFacturaDetalle.setIdProducto(null);
                facturaDetalleListFacturaDetalle = em.merge(facturaDetalleListFacturaDetalle);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<Producto> findProducto(String busqueda) {
        EntityManager em = getEntityManager();
        try {
            Query sql = em.createQuery("SELECT p FROM Producto p WHERE p.codigo LIKE :cod OR p.descripcion LIKE :desc");
            sql.setParameter("cod", "%" + busqueda + "%");
            sql.setParameter("desc", "%" + busqueda + "%");
            return sql.getResultList();

        } finally {
            em.close();
        }
    }

    public List<Producto> findProductoEntitiesMenorStock() {
        EntityManager em = getEntityManager();
        try {
            Query sql = em.createQuery("SELECT p FROM Producto p WHERE p.stock<20 ORDER BY P.stock ASC");
            return sql.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Producto> findProductoEntitiesProveedor(Proveedor proveedor, String busqueda) {
        EntityManager em = getEntityManager();
        try {
            Query sql = em.createQuery("SELECT p FROM Producto p WHERE p.proveedor=:proveedor AND p.descripcion LIKE :busqueda OR p.codigo=:codigo");
            sql.setParameter("proveedor", proveedor);
            sql.setParameter("busqueda", "%"+busqueda+"%");
            sql.setParameter("codigo", "%"+busqueda+"%");
            return sql.getResultList();
        } finally {
            em.close();
        }

    }

    public List<Producto> findProductoConProveedor(Proveedor proveedor) {
        EntityManager em = getEntityManager();
        try {
            Query sql = em.createQuery("SELECT p FROM Producto p WHERE p.proveedor=:proveedor");
            sql.setParameter("proveedor", proveedor);
            return sql.getResultList();
        } finally {
            em.close();
        }

    }

    
}
