package ru.sfedu.deals.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sfedu.deals.Constants;

import java.io.File;

public class HibernateUtil {
  private static SessionFactory sessionFactory;
  private static final String DEFAULT_CONFIG_PATH = Constants.DEFAULT_HIBERNATE_PATH;
  private static final Logger log = (Logger) LogManager.getLogger(HibernateUtil.class);

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      sessionFactory = new Configuration().configure(loadProperty()).buildSessionFactory();
    }

    return sessionFactory;
  }

  private static File loadProperty() {
    File nf;

    if (System.getProperty(Constants.HIBERNATE_PATH) != null) {
      nf = new File(System.getProperty(Constants.HIBERNATE_PATH));
    } else {
      nf = new File(DEFAULT_CONFIG_PATH);
    }

    return nf;
  }

}
