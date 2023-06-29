package ukma.edu.ua.persistent;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Dao<T> {
    private final SessionFactory sessionFactory;
    private final Class<T> entity;

    protected Dao(SessionFactory sessionFactory, Class<T> entity) {
        this.sessionFactory = sessionFactory;
        this.entity = entity;
    }

    public void save(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
    }

    public void update(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        session.close();
    }

    public void delete(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
    }

    public Optional<T> findById(long id) {
        Session session = sessionFactory.openSession();
        T entity = session.get(this.entity, id);
        session.close();
        return Optional.ofNullable(entity);
    }

    public List<T> getAll() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
        Root<T> root = criteriaQuery.from(entity);

        criteriaQuery.select(root);
        Query<T> query = session.createQuery(criteriaQuery);

        List<T> resultList = query.getResultList();
        session.close();

        return resultList;
    }

    public List<T> listByCriteria(Map<String, Object> criteriaMap) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
        Root<T> root = criteriaQuery.from(entity);

        Predicate[] predicates = new Predicate[criteriaMap.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : criteriaMap.entrySet()) {
            predicates[i] = criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue());
            i++;
        }

        criteriaQuery.select(root).where(predicates);
        Query<T> query = session.createQuery(criteriaQuery);

        List<T> resultList = query.getResultList();
        session.close();

        return resultList;
    }
}
