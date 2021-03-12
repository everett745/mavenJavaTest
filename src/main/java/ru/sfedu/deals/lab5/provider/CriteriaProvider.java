package ru.sfedu.deals.lab5.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.lab5.Company;
import ru.sfedu.deals.lab5.Deal;
import ru.sfedu.deals.lab5.User;
import ru.sfedu.deals.utils.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class CriteriaProvider implements IProvider{
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(CriteriaProvider.class);

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
  }

  private Session getSession() {
    return HibernateUtil.getSessionFactory().openSession();
  }

  @Override
  public Optional<List<Address>> selectAllAddress() {
    Session session = getSession();

    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<Address> cr = cb.createQuery(Address.class);
      Root<Address> root = cr.from(Address.class);
      cr.select(root);

      Query<Address> query = session.createQuery(cr);
      List<Address> resList = query.getResultList();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<User>> selectAllUsers() {
    Session session = getSession();

    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<User> cr = cb.createQuery(User.class);
      Root<User> root = cr.from(User.class);
      cr.select(root);

      Query<User> query = session.createQuery(cr);
      List<User> resList = query.getResultList();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Deal>> selectAllDeals() {
    Session session = getSession();

    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<Deal> cr = cb.createQuery(Deal.class);
      Root<Deal> root = cr.from(Deal.class);
      cr.select(root);

      Query<Deal> query = session.createQuery(cr);
      List<Deal> resList = query.getResultList();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Company>> selectAllCompanies() {
    Session session = getSession();

    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<Company> cr = cb.createQuery(Company.class);
      Root<Company> root = cr.from(Company.class);
      cr.select(root);

      Query<Company> query = session.createQuery(cr);
      List<Company> resList = query.getResultList();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }
}
