package jm.task.core.jdbc.util;

import com.mysql.cj.Session;
import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public class Util {
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "Jklgfyr1";
    public static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static SessionFactory getSessionFactory() {
        try {
            Properties prop = new Properties();
            prop.setProperty("hibernate.connection.driver_class", DRIVER);
            prop.setProperty("hibernate.connection.url", URL);
            prop.setProperty("hibernate.connection.username", USER_NAME);
            prop.setProperty("hibernate.connection.password", PASSWORD);
            prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            prop.setProperty("hibernate.hbm2ddl.auto", "update");

            Configuration configuration = new Configuration();
            configuration.setProperties(prop);
            configuration.addAnnotatedClass(User.class);
            HibernateSessionFactory.sessionFactory = configuration.buildSessionFactory();


        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return HibernateSessionFactory.sessionFactory;
    }


    public class HibernateSessionFactory {

        private static volatile SessionFactory sessionFactory;

        static {
            buildSessionFactory();
        }

        private HibernateSessionFactory() {

        }

        public static Session currentSession() {
            return (Session) sessionFactory.getCurrentSession();
        }
        public static void setSessionFactory(SessionFactory sessionFactory) {
            HibernateSessionFactory.sessionFactory = sessionFactory;
        }

        private static void buildSessionFactory() {
            if (sessionFactory != null) {
                org.hibernate.boot.registry.StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();
                sessionFactory = new MetadataSources(ssr).buildMetadata().buildSessionFactory();
            }
        }
    }
}

    /** private static SessionFactory sessionFactory;

    public static SessionFactory getCurrentSession() {
       try {
            Properties prop = new Properties();
            prop.setProperty("hibernate.connection.driver_class", DRIVER);
            prop.setProperty("hibernate.connection.url", URL);
            prop.setProperty("hibernate.connection.username", USER_NAME);
            prop.setProperty("hibernate.connection.password", PASSWORD);
            prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            prop.setProperty("hibernate.hbm2ddl.auto", "update");

            Configuration configuration = new Configuration();
            configuration.setProperties(prop);
            configuration.addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();


        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
}
     */