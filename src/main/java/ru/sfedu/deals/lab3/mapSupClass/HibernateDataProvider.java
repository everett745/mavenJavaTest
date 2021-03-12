package ru.sfedu.deals.lab3.mapSupClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class HibernateDataProvider {
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(HibernateDataProvider.class);

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
  }

  private Session getSession() {
    return HibernateUtil.getSessionFactory().openSession();
  }

  public RequestStatuses insertAccount(@NotNull Account account) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.save(account);
      session.getTransaction().commit();

      session.close();

      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public Optional<List<CreditAccount>> selectAllCreditAccount() {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(SqlQueriesConstants.SQL_SELECT_CREDIT_ACCOUNT);
      List<CreditAccount> resList = query.getResultList();

      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public Optional<Account> selectById(long id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      Account item = session.get(Account.class, id);

      session.close();
      return item != null ? Optional.of(item) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public Optional<List<DebitAccount>> selectAllDebitAccount() {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(SqlQueriesConstants.SQL_SELECT_DEBIT_ACCOUNT);
      List<DebitAccount> resList = query.getResultList();

      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public RequestStatuses deleteAccount(@NotNull Account account) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.delete(account);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses deleteAccount(long id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      Optional<Account> account = selectById(id);
      if (account.isPresent()) {
        session.delete(account.get());
        session.getTransaction().commit();
        session.close();
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses updateAccount(@NotNull Account account) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.update(account);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

}
