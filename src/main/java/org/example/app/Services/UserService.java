package org.example.app.Services;

import jakarta.transaction.Transactional;
import org.example.app.Daos.UserDao;
import org.example.app.Exceptions.UserException;
import org.example.app.Models.Entities.Role;
import org.example.app.Models.Entities.User;
import org.example.app.Models.Dtos.UserDto;
import org.example.app.RoleType;
import org.example.app.Security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserById(UUID id) {
        return userDao.findUserById(id);
    }


    public User findUserByName(String username) {
        return userDao.findUserByUsername(username);

    }


    public Role findRole(RoleType roleType) {
        return userDao.findRole(roleType);
    }

    @Transactional
    public void saveUser(User user) throws Exception {
        User userByUsername = userDao.findUserByUsername(user.getUsername());
        User userByEmail = userDao.findUserByEmail(user.getEmail());

        if (userByUsername != null) {
            logger.error("user {} is already taken", user.getUsername());
            throw new UserException("Username \"" + user.getUsername() + "\" is already taken");
        }
        if (userByEmail != null) {
            throw new UserException("User with email address \"" + user.getEmail() + "\" already exists");
        }

        String[] hashedPassword = PasswordEncoder.hashPassword(user.getPassword(), null);
        user.setPassword(hashedPassword[0] + "#" + hashedPassword[1]);
        user.syncRoles();

        userDao.saveUser(user);
    }

    public boolean verifyLogin(User user, String password) throws Exception {
        int saltIndex = user.getPassword().indexOf("#");
        String salt = user.getPassword().substring(0, saltIndex);
        String storedHash = user.getPassword().substring(saltIndex + 1);
        String hash = PasswordEncoder.hashPassword(password, salt)[1];

        return storedHash.equals(hash);
    }


    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles().stream().map((role -> role.getRole().toString())).toList(),
                user.getAds().stream().map((ad) -> ad.getId()).toList()
        );
    }
}
