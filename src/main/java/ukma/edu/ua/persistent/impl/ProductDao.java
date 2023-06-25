package ukma.edu.ua.persistent.impl;

import org.hibernate.SessionFactory;
import ukma.edu.ua.model.entity.Product;
import ukma.edu.ua.persistent.Dao;

public class ProductDao extends Dao<Product> {
    public ProductDao(SessionFactory sessionFactory) {
        super(sessionFactory, Product.class);
    }
}
