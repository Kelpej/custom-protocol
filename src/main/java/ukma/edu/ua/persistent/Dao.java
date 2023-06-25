package ukma.edu.ua.persistent;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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

    public T getById(int id) {
        Session session = sessionFactory.openSession();
        T entity = session.get(this.entity, id);
        session.close();
        return entity;
    }
}
