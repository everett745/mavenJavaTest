package ru.sfedu.deals.hibernate.lab3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sfedu.deals.dataProviders.HibernateTest;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.lab3.singleTable.CreditAccount;
import ru.sfedu.deals.lab3.singleTable.DebitAccount;
import ru.sfedu.deals.lab3.singleTable.HibernateDataProvider;

import java.math.BigDecimal;

public class SingleTableTest {
  private static final Logger log = LogManager.getLogger(HibernateTest.class);
  private static final HibernateDataProvider provider = new HibernateDataProvider();


  @Test
  void insertCreditAccount() {
    log.debug("insertCreditAccount");
    CreditAccount account = new CreditAccount();
    account.setBalance(BigDecimal.valueOf(123.0));
    account.setInterestRate(BigDecimal.valueOf(3.0));
    account.setOwner("Owner");
    account.setCreditLimit(BigDecimal.valueOf(500.0));

    Assertions.assertEquals(provider.insertAccount(account), RequestStatuses.SUCCESS);
  }

  @Test
  void insertDebitAccount() {
    log.debug("insertDebitAccount");
    DebitAccount account = new DebitAccount();
    account.setBalance(BigDecimal.valueOf(123.0));
    account.setInterestRate(BigDecimal.valueOf(3.0));
    account.setOwner("Owner");
    account.setOverdraftFee(BigDecimal.valueOf(500.0));

    Assertions.assertEquals(provider.insertAccount(account), RequestStatuses.SUCCESS);
  }

  @Test
  void selectAllCreditAccount() {
    log.debug("selectAllCreditAccount");
    insertCreditAccount();
    log.info(provider.selectAllCreditAccount());
  }

  @Test
  void selectAllDebitAccount() {
    log.debug("selectAllDebitAccount");
    insertDebitAccount();
    log.info(provider.selectAllDebitAccount());
  }

  @Test
  void deleteAccountCreditAccount() {
    log.debug("deleteAccountCreditAccount");
    insertCreditAccount();
    CreditAccount account = provider.selectAllCreditAccount().get().get(0);
    Assertions.assertEquals(provider.deleteAccount(account), RequestStatuses.SUCCESS);
  }

  @Test
  void deleteAccountDebitAccount() {
    log.debug("deleteAccountDebitAccount");
    insertDebitAccount();
    DebitAccount account = provider.selectAllDebitAccount().get().get(0);
    Assertions.assertEquals(provider.deleteAccount(account), RequestStatuses.SUCCESS);
  }

  @Test
  void updateAccountCreditAccount() {
    log.debug("deleteAccountCreditAccount");
    insertCreditAccount();
    CreditAccount account = provider.selectAllCreditAccount().get().get(0);
    account.setCreditLimit(BigDecimal.valueOf(0));
    Assertions.assertEquals(provider.updateAccount(account), RequestStatuses.SUCCESS);
  }

  @Test
  void updateAccountDebitAccount() {
    log.debug("deleteAccountDebitAccount");
    insertDebitAccount();
    DebitAccount account = provider.selectAllDebitAccount().get().get(0);
    account.setOverdraftFee(BigDecimal.valueOf(0));
    Assertions.assertEquals(provider.updateAccount(account), RequestStatuses.SUCCESS);
  }
}
