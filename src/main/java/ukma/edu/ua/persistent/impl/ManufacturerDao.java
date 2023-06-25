package ukma.edu.ua.persistent.impl;

import org.hibernate.SessionFactory;
import ukma.edu.ua.model.entity.Manufacturer;
import ukma.edu.ua.persistent.Dao;

public class ManufacturerDao extends Dao<Manufacturer> {
    protected ManufacturerDao(SessionFactory sessionFactory) {
        super(sessionFactory, Manufacturer.class);
    }
}
