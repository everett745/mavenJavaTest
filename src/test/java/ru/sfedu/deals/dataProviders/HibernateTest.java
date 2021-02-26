package ru.sfedu.deals.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.model.AddressT;
import ru.sfedu.deals.model.TestEntity;

import java.util.Date;
import java.util.List;

public class HibernateTest {
  private static final Logger log = LogManager.getLogger(HibernateTest.class);
  private static final HibernateDataProvider provider = new HibernateDataProvider();


  @Test
  void getAllSchemas() {
    log.debug("getAllSchemas");
    List res = provider.getAllSchemas();
    log.info(res);
  }

  @Test
  void getSqlHelp() {
    log.debug("getSqlHelp");
    List res = provider.getSqlHelp();
    log.info(res);
  }

  @Test
  void getAllTables() {
    log.debug("getAllTables");
    List res = provider.getAllTables();
    log.info(res);
  }

  @Test
  void getAllUsers() {
    log.debug("getAllUsers");
    List res = provider.getAllUsers();
    log.info(res);
  }

  @Test
  void insertTestEntity() {
    log.debug("insertTestEntity");
    AddressT address = new AddressT();
    address.setCity("Москва");
    address.setDistrict("Московская");
    address.setRegion("Ммм");

    TestEntity testEntity = new TestEntity();
    testEntity.setName("Name");
    testEntity.setDescription("Description");
    testEntity.setDateCreated(new Date());
    testEntity.setCheck(false);
    testEntity.setAddress(address);
    Assertions.assertEquals(provider.insertTestEntity(testEntity), RequestStatuses.SUCCESS);
  }

  @Test
  void insertTestEntityFailed() {
    log.debug("insertTestEntityFailed");
    Assertions.assertThrows(IllegalArgumentException.class, () ->
            provider.insertTestEntity(null));
  }

  @Test
  void selectAllTestEntity() {
    log.debug("selectAllTestEntity");
    insertTestEntity();
    log.debug(provider.selectAllTestEntity());
  }

  @Test
  void deleteTestEntity() {
    log.debug("deleteTestEntity");
    insertTestEntity();
    TestEntity entity = provider.selectAllTestEntity().get().get(0);
    Assertions.assertEquals(provider.deleteTestEntity(entity), RequestStatuses.SUCCESS);
  }

  @Test
  void deleteTestEntityFailed() {
    log.debug("deleteTestEntityFailed");
    Assertions.assertThrows(IllegalArgumentException.class, () ->
            provider.deleteTestEntity(null));
  }

  @Test
  void updateTestEntity() {
    log.debug("updateTestEntity");
    insertTestEntity();
    TestEntity entity = provider.selectAllTestEntity().get().get(0);
    log.debug(entity);
    entity.setName("New Name");
    entity.setDescription("New description");
    Assertions.assertEquals(provider.updateTestEntity(entity), RequestStatuses.SUCCESS);

    TestEntity updatedEntity = provider.selectAllTestEntity().get().get(0);
    log.debug(updatedEntity);
  }

  @Test
  void updateTestEntityFailed() {
    log.debug("updateTestEntityFailed");

    TestEntity updatedEntity = new TestEntity();
    Assertions.assertEquals(provider.updateTestEntity(updatedEntity), RequestStatuses.FAILED);
  }
}
