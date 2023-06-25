package ukma.edu.ua.service;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.entity.Group;
import ukma.edu.ua.persistent.impl.GroupDao;

@RequiredArgsConstructor
public class GroupService {
    private final GroupDao groupDao;

    public Group addGroup(String name, String description) {
        Group group = Group.builder()
                .name(name)
                .description(description)
                .build();

        groupDao.save(group);
        return group;
    }
}
