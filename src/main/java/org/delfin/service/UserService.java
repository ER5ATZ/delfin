package org.delfin.service;

import org.delfin.exception.UserNotFoundException;
import org.delfin.model.User;
import org.delfin.model.entity.UserDetailsEntity;
import org.delfin.repository.UserRepository;
import org.delfin.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();
        for (UserDetailsEntity entity : userRepository.findAll()) {
            users.add(new User(entity));
        }
        return users;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user) {
        UserDetailsEntity entity = user.toEntity();
        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        entity.setPassword(hashedPassword);
        UserDetailsEntity result = userRepository.save(entity);
        return new User(result);
    }

    public User update(User user) {
        UserDetailsEntity updatedUser = user.toEntity();
        UserDetailsEntity existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + user.getUsername()));

        existingUser.setUsername(updatedUser.getUsername());

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt());
        existingUser.setPassword(hashedPassword);

        existingUser.setExpires(updatedUser.getExpires());
        existingUser.setPwexpires(updatedUser.getPwexpires());
        existingUser.setFailedlogins(updatedUser.getFailedlogins());
        existingUser.setActive(updatedUser.isActive());
        existingUser.setRoles(updatedUser.getRoles());
        existingUser.setUpdated(LocalDateTime.now());
        return new User(userRepository.save(existingUser));
    }

    public User getUserByUserName(String username) throws UserNotFoundException {
        return new User(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username)));
    }

    public void requestReset(String userName, String email) throws Exception {
        if (StringUtils.isEmail(email) && userRepository.findByEmail(email).isPresent()) {
            sendResetCode(email);
            return;
        }

        UserDetailsEntity entity = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
        sendResetCode(entity.getEmail());
    }

    public void resetPassword(String code, User user) {
        UserDetailsEntity entity = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
        // Hash the new password using BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        entity.setPassword(hashedPassword);
        entity.setFailedlogins(0);
        entity.setUpdated(LocalDateTime.now());

        userRepository.save(entity);
    }

    private void sendResetCode(String email) {
    }

    public User findById(Long id) throws UserNotFoundException {
        return new User(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
}
