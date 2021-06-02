package dao;

import dao.exceptions.NonexistentEntityException;
import entidades.TestInicio;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author telev
 */
public class TestInicioJpaController implements Serializable {

    public TestInicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestInicio testInicio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(testInicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestInicio testInicio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            testInicio = em.merge(testInicio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = testInicio.getIdInicio();
                if (findTestInicio(id) == null) {
                    throw new NonexistentEntityException("The testInicio with id " + id + " no longer exists.");
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
            TestInicio testInicio;
            try {
                testInicio = em.getReference(TestInicio.class, id);
                testInicio.getIdInicio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testInicio with id " + id + " no longer exists.", enfe);
            }
            em.remove(testInicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestInicio> findTestInicioEntities() {
        return findTestInicioEntities(true, -1, -1);
    }

    public List<TestInicio> findTestInicioEntities(int maxResults, int firstResult) {
        return findTestInicioEntities(false, maxResults, firstResult);
    }

    private List<TestInicio> findTestInicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestInicio.class));
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

    public TestInicio findTestInicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestInicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestInicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestInicio> rt = cq.from(TestInicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
