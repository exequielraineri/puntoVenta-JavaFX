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
import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Entity.Producto;
import java.util.ArrayList;
import java.util.List;
import com.raineri.puntoventa.Entity.ProductoProveedor;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
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
        if (producto.getProductoProveedorList() == null) {
            producto.setProductoProveedorList(new ArrayList<ProductoProveedor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FacturaDetalle> attachedFacturaDetalleList = new ArrayList<FacturaDetalle>();
            for (FacturaDetalle facturaDetalleListFacturaDetalleToAttach : producto.getFacturaDetalleList()) {
                facturaDetalleListFacturaDetalleToAttach = em.getReference(facturaDetalleListFacturaDetalleToAttach.getClass(), facturaDetalleListFacturaDetalleToAttach.getId());
                attachedFacturaDetalleList.add(facturaDetalleListFacturaDetalleToAttach);
            }
            producto.setFacturaDetalleList(attachedFacturaDetalleList);
            List<ProductoProveedor> attachedProductoProveedorList = new ArrayList<ProductoProveedor>();
            for (ProductoProveedor productoProveedorListProductoProveedorToAttach : producto.getProductoProveedorList()) {
                productoProveedorListProductoProveedorToAttach = em.getReference(productoProveedorListProductoProveedorToAttach.getClass(), productoProveedorListProductoProveedorToAttach.getId());
                attachedProductoProveedorList.add(productoProveedorListProductoProveedorToAttach);
            }
            producto.setProductoProveedorList(attachedProductoProveedorList);
            em.persist(producto);
            for (FacturaDetalle facturaDetalleListFacturaDetalle : producto.getFacturaDetalleList()) {
                Producto oldIdProductoOfFacturaDetalleListFacturaDetalle = facturaDetalleListFacturaDetalle.getIdProducto();
                facturaDetalleListFacturaDetalle.setIdProducto(producto);
                facturaDetalleListFacturaDetalle = em.merge(facturaDetalleListFacturaDetalle);
                if (oldIdProductoOfFacturaDetalleListFacturaDetalle != null) {
                    oldIdProductoOfFacturaDetalleListFacturaDetalle.getFacturaDetalleList().remove(facturaDetalleListFacturaDetalle);
                    oldIdProductoOfFacturaDetalleListFacturaDetalle = em.merge(oldIdProductoOfFacturaDetalleListFacturaDetalle);
                }
            }
            for (ProductoProveedor productoProveedorListProductoProveedor : producto.getProductoProveedorList()) {
                Producto oldIdProductoOfProductoProveedorListProductoProveedor = productoProveedorListProductoProveedor.getIdProducto();
                productoProveedorListProductoProveedor.setIdProducto(producto);
                productoProveedorListProductoProveedor = em.merge(productoProveedorListProductoProveedor);
                if (oldIdProductoOfProductoProveedorListProductoProveedor != null) {
                    oldIdProductoOfProductoProveedorListProductoProveedor.getProductoProveedorList().remove(productoProveedorListProductoProveedor);
                    oldIdProductoOfProductoProveedorListProductoProveedor = em.merge(oldIdProductoOfProductoProveedorListProductoProveedor);
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
            List<FacturaDetalle> facturaDetalleListOld = persistentProducto.getFacturaDetalleList();
            List<FacturaDetalle> facturaDetalleListNew = producto.getFacturaDetalleList();
            List<ProductoProveedor> productoProveedorListOld = persistentProducto.getProductoProveedorList();
            List<ProductoProveedor> productoProveedorListNew = producto.getProductoProveedorList();
            List<FacturaDetalle> attachedFacturaDetalleListNew = new ArrayList<FacturaDetalle>();
            for (FacturaDetalle facturaDetalleListNewFacturaDetalleToAttach : facturaDetalleListNew) {
                facturaDetalleListNewFacturaDetalleToAttach = em.getReference(facturaDetalleListNewFacturaDetalleToAttach.getClass(), facturaDetalleListNewFacturaDetalleToAttach.getId());
                attachedFacturaDetalleListNew.add(facturaDetalleListNewFacturaDetalleToAttach);
            }
            facturaDetalleListNew = attachedFacturaDetalleListNew;
            producto.setFacturaDetalleList(facturaDetalleListNew);
            List<ProductoProveedor> attachedProductoProveedorListNew = new ArrayList<ProductoProveedor>();
            for (ProductoProveedor productoProveedorListNewProductoProveedorToAttach : productoProveedorListNew) {
                productoProveedorListNewProductoProveedorToAttach = em.getReference(productoProveedorListNewProductoProveedorToAttach.getClass(), productoProveedorListNewProductoProveedorToAttach.getId());
                attachedProductoProveedorListNew.add(productoProveedorListNewProductoProveedorToAttach);
            }
            productoProveedorListNew = attachedProductoProveedorListNew;
            producto.setProductoProveedorList(productoProveedorListNew);
            producto = em.merge(producto);
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
            for (ProductoProveedor productoProveedorListOldProductoProveedor : productoProveedorListOld) {
                if (!productoProveedorListNew.contains(productoProveedorListOldProductoProveedor)) {
                    productoProveedorListOldProductoProveedor.setIdProducto(null);
                    productoProveedorListOldProductoProveedor = em.merge(productoProveedorListOldProductoProveedor);
                }
            }
            for (ProductoProveedor productoProveedorListNewProductoProveedor : productoProveedorListNew) {
                if (!productoProveedorListOld.contains(productoProveedorListNewProductoProveedor)) {
                    Producto oldIdProductoOfProductoProveedorListNewProductoProveedor = productoProveedorListNewProductoProveedor.getIdProducto();
                    productoProveedorListNewProductoProveedor.setIdProducto(producto);
                    productoProveedorListNewProductoProveedor = em.merge(productoProveedorListNewProductoProveedor);
                    if (oldIdProductoOfProductoProveedorListNewProductoProveedor != null && !oldIdProductoOfProductoProveedorListNewProductoProveedor.equals(producto)) {
                        oldIdProductoOfProductoProveedorListNewProductoProveedor.getProductoProveedorList().remove(productoProveedorListNewProductoProveedor);
                        oldIdProductoOfProductoProveedorListNewProductoProveedor = em.merge(oldIdProductoOfProductoProveedorListNewProductoProveedor);
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
            List<FacturaDetalle> facturaDetalleList = producto.getFacturaDetalleList();
            for (FacturaDetalle facturaDetalleListFacturaDetalle : facturaDetalleList) {
                facturaDetalleListFacturaDetalle.setIdProducto(null);
                facturaDetalleListFacturaDetalle = em.merge(facturaDetalleListFacturaDetalle);
            }
            List<ProductoProveedor> productoProveedorList = producto.getProductoProveedorList();
            for (ProductoProveedor productoProveedorListProductoProveedor : productoProveedorList) {
                productoProveedorListProductoProveedor.setIdProducto(null);
                productoProveedorListProductoProveedor = em.merge(productoProveedorListProductoProveedor);
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

}
