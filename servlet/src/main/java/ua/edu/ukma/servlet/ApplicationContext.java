package ua.edu.ukma.servlet;

import ukma.edu.ua.persistent.Hibernate;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.persistent.impl.ManufacturerDao;
import ukma.edu.ua.persistent.impl.ProductDao;
import ukma.edu.ua.service.GroupService;
import ukma.edu.ua.service.ManufacturerService;
import ukma.edu.ua.service.ProductService;

public class ApplicationContext {


    private ApplicationContext() {
    }

    private static final ProductDao productDao = new ProductDao(Hibernate.getSessionFactory());
    private static final GroupDao groupDao = new GroupDao(Hibernate.getSessionFactory());
    private static final ManufacturerDao manufacturerDao = new ManufacturerDao(Hibernate.getSessionFactory());

    static final ProductService productService = new ProductService(productDao, groupDao);
    static final GroupService groupService = new GroupService(groupDao);
    static final ManufacturerService manufacturerService = new ManufacturerService(manufacturerDao);
}
