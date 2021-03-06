package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Distribuye;
import entidades.Juego;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author telev
 */
public class JuegoJpaController implements Serializable {

    public JuegoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juego juego) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distribuye distribuidor = juego.getDistribuidor();
            if (distribuidor != null) {
                distribuidor = em.getReference(distribuidor.getClass(), distribuidor.getIdDistribuidor());
                juego.setDistribuidor(distribuidor);
            }
            Usuario usuario = juego.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                juego.setUsuario(usuario);
            }
            em.persist(juego);
            if (distribuidor != null) {
                distribuidor.getJuegoList().add(juego);
                distribuidor = em.merge(distribuidor);
            }
            if (usuario != null) {
                usuario.getJuegoList().add(juego);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juego juego) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juego persistentJuego = em.find(Juego.class, juego.getIdJuego());
            Distribuye distribuidorOld = persistentJuego.getDistribuidor();
            Distribuye distribuidorNew = juego.getDistribuidor();
            Usuario usuarioOld = persistentJuego.getUsuario();
            Usuario usuarioNew = juego.getUsuario();
            if (distribuidorNew != null) {
                distribuidorNew = em.getReference(distribuidorNew.getClass(), distribuidorNew.getIdDistribuidor());
                juego.setDistribuidor(distribuidorNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                juego.setUsuario(usuarioNew);
            }
            juego = em.merge(juego);
            if (distribuidorOld != null && !distribuidorOld.equals(distribuidorNew)) {
                distribuidorOld.getJuegoList().remove(juego);
                distribuidorOld = em.merge(distribuidorOld);
            }
            if (distribuidorNew != null && !distribuidorNew.equals(distribuidorOld)) {
                distribuidorNew.getJuegoList().add(juego);
                distribuidorNew = em.merge(distribuidorNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getJuegoList().remove(juego);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getJuegoList().add(juego);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = juego.getIdJuego();
                if (findJuego(id) == null) {
                    throw new NonexistentEntityException("The juego with id " + id + " no longer exists.");
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
            Juego juego;
            try {
                juego = em.getReference(Juego.class, id);
                juego.getIdJuego();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juego with id " + id + " no longer exists.", enfe);
            }
            Distribuye distribuidor = juego.getDistribuidor();
            if (distribuidor != null) {
                distribuidor.getJuegoList().remove(juego);
                distribuidor = em.merge(distribuidor);
            }
            Usuario usuario = juego.getUsuario();
            if (usuario != null) {
                usuario.getJuegoList().remove(juego);
                usuario = em.merge(usuario);
            }
            em.remove(juego);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juego> findJuegoEntities() {
        return findJuegoEntities(true, -1, -1);
    }

    public List<Juego> findJuegoEntities(int maxResults, int firstResult) {
        return findJuegoEntities(false, maxResults, firstResult);
    }

    private List<Juego> findJuegoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juego.class));
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

    public Juego findJuego(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juego.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuegoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juego> rt = cq.from(Juego.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public boolean existeJuego(Juego juego) {
        EntityManager em = getEntityManager();
        List<Juego> juegotmp = new ArrayList<>();
        boolean respuesta = false;
        try {
            TypedQuery<Juego> consulta = em.createNamedQuery("Juego.findByTitulo", Juego.class);    //preparamos la consulta QUERY a realizar
            consulta.setParameter("titulo", juego.getTitulo());    //indico el campo y la cadena a buscar 
            juegotmp = consulta.getResultList();     //guardo la consulta realiza en un objeto de tipo ArrayList
            if (!juegotmp.isEmpty()) {       //si el resultado es vacio es que no existe
                respuesta = true;
            }
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return respuesta;
    }

    public boolean existeAllJuego(Juego juego) {
        //Creamos nuestra propia QUERY para conocer si existe alg??n registro con los mismos datos
        String QUERY = "SELECT j FROM Juego j WHERE j.titulo = :titulo AND j.sistemaOperativo = :sistemaOperativo"
                + " AND j.fechaJuego = :fechaJuego AND j.precio = :precio";

        EntityManager em = getEntityManager();
        List<Juego> juegotmp = new ArrayList<>();
        boolean respuesta = false;
        try {
            TypedQuery<Juego> consulta = em.createQuery(QUERY, Juego.class);    //preparamos la consulta QUERY a realizar
            consulta.setParameter("titulo", juego.getTitulo());    //indico el campo y la cadena a buscar 
            consulta.setParameter("sistemaOperativo", juego.getSistemaOperativo());
            consulta.setParameter("fechaJuego", juego.getFechaJuego());
            consulta.setParameter("precio", juego.getPrecio());
            juegotmp = consulta.getResultList();     //guardo la consulta realiza en un objeto de tipo ArrayList
            if (!juegotmp.isEmpty()) {       //si el resultado es vacio es que no existe
                respuesta = true;
            }
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return respuesta;
    }

}
