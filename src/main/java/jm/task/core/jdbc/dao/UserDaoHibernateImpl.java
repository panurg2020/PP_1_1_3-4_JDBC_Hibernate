package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    protected final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), lastName VARCHAR(255), age INT)";
    protected final String HQL_REMOVE_USER_BY_ID = "DELETE User u where u.id = :id";
    protected final String HQL_GET_ALL_USERS = "FROM User";
    protected final String HQL_CLEAN_TABLE = "DELETE FROM User";
    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.createNativeQuery(SQL_CREATE).executeUpdate();
            transaction.commit();
            System.out.println("table create");
        } catch (HibernateException e) {
            System.err.println("При создании таблицы пользователей произошло исключение\n" + e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
            System.out.println("drop table");
        } catch (HibernateException e) {
            System.err.println("При удалении таблицы пользователей произошло исключение\n" + e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.save(user);
            transaction.commit();
        } catch (HibernateException e) {
            System.err.println("Во время сохранения пользователя произошло исключение\n" + e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.createQuery(HQL_REMOVE_USER_BY_ID).setBigInteger("id", BigInteger.valueOf(id )).executeUpdate();
            //session.flush();
            transaction.commit();
            System.out.println("user delete");
        } catch (HibernateException e) {
            System.err.println("При удаление пользователя по id произошло исключение\n" + e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<User> users = new ArrayList<>();
        try {
            Query query = session.createQuery(HQL_GET_ALL_USERS);
            users = query.getResultList();
            transaction.commit();
        } catch (HibernateException e) {
            System.err.println("При попытке достать всех пользователей из базы данных произошло исключение\n" + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.createQuery(HQL_CLEAN_TABLE).executeUpdate();
            transaction.commit();
            System.out.println("table clear");
        } catch (HibernateException e) {
            System.err.println("При тестировании очистки таблицы пользователей произошло исключение\n" + e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}