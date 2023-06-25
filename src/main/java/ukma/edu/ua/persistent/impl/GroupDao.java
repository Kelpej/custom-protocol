package ukma.edu.ua.persistent.impl;

import org.hibernate.SessionFactory;
import ukma.edu.ua.model.entity.Group;
import ukma.edu.ua.persistent.Dao;

public class GroupDao extends Dao<Group> {
    public GroupDao(SessionFactory sessionFactory) {
        super(sessionFactory, Group.class);
    }
}
