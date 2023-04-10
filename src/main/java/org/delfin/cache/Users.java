package org.delfin.cache;

import org.delfin.model.entity.UserDetailsEntity;
import org.delfin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class Users {
    private static final Logger LOG = LoggerFactory.getLogger(Users.class);
    private final UserRepository userRepository;

    private List<UserDetailsEntity> users;
    private Map<String, UserDetailsEntity> userMap;

    @Autowired
    public Users(UserRepository userRepository) {
        this.userRepository = userRepository;
        init();
    }

    public List<UserDetailsEntity> getUsers() {
        return users;
    }

    public Map<String, UserDetailsEntity> getUserMap() {
        return userMap;
    }

    public void init() {
        this.users = userRepository.findAll();
        this.userMap = new HashMap<>();
        for (UserDetailsEntity user : users) {
            userMap.put(user.getUsername(), user);
        }
        if (users.size() != userMap.size()) {
            LOG.warn("Some usernames appear to be invalid!");
        } else {
            LOG.info("Loaded " + users.size() + " User profiles.");
        }
    }
}
