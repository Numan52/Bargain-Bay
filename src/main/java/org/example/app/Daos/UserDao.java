package org.example.app.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.app.Models.Entities.Role;
import org.example.app.Models.Entities.User;
import org.example.app.RoleType;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;


    public User findUserByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }


    public User findUserByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public Role findRole(RoleType roleType) {
        try {
            return entityManager.createQuery("SELECT r FROM Role r WHERE r.role = :roleType", Role.class)
                    .setParameter("roleType", roleType)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public void saveUser(User user) {
        entityManager.persist(user);
    }
}
