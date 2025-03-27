package com.gross.weather.dao;

import com.gross.weather.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class UserDAO {

    private final SessionFactory sessionFactory;


    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM User", User.class).getResultList();
    }


    public User getUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Transactional
    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Transactional
    public void deleteUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(getUser(id));
    }

    @Transactional
    public void updateUser(int id,User updatedUser) {
        User user = getUser(id);
        user.setLogin(updatedUser.getLogin());
        user.setPassword(updatedUser.getPassword());

    }

}
