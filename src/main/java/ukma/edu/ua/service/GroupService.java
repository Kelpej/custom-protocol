package ukma.edu.ua.service;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.entity.Group;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import java.util.List;

@RequiredArgsConstructor
public class GroupService {
    private final GroupDao groupDao;

    public Group addGroup(Group group) {
        groupDao.save(group);
        return group;
    }

    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public void deleteGroup(long id) throws NoEntityFoundException {
        Group group = groupDao.findById(id)
                .orElseThrow(() -> new NoEntityFoundException(id));
        groupDao.delete(group);
    }

    public void updateGroup(Group group) {
        groupDao.update(group);
    }

    public Group getGroup(long id) throws NoEntityFoundException {
        return groupDao.findById(id)
                .orElseThrow(() -> new NoEntityFoundException(id));
    }
}
