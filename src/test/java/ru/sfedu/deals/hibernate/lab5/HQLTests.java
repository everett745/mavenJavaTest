package ru.sfedu.deals.hibernate.lab5;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.deals.lab5.provider.HQLProvider;
import ru.sfedu.deals.lab5.provider.IProvider;

public class HQLTests extends Tests {
  private static final IProvider provider = new HQLProvider();

  @BeforeAll
  static void initialTest() {
    setUp(provider);
  }
  
  @Test
  void insertCompany() {
    super.insertCompany();
  }

  @Test
  void selectAllCompanies() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    super.selectAllCompanies();
    stopWatch.stop();
    System.out.println("Время выполнения, мс: " + stopWatch.getTime());
  }

  @Test
  void deleteCompany() {
    super.deleteCompany();
  }

  @Test
  void updateCompany() {
    super.updateCompany();
  }

}
