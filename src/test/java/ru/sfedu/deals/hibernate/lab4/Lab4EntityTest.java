package ru.sfedu.deals.hibernate.lab4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sfedu.deals.dataProviders.HibernateDataProvider;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.model.AddressT;
import ru.sfedu.deals.model.Lab4Entity;

import java.util.*;

public class Lab4EntityTest {
  private static final Logger log = LogManager.getLogger(Lab4EntityTest.class);
  private static final HibernateDataProvider provider = new HibernateDataProvider();

  private AddressT getAddress() {
    AddressT address = new AddressT();
    address.setCity("Москва");
    address.setRegion("Москвовская область");
    address.setDistrict("");
    return address;
  }

  private Set<String> getSet() {
    Set<String> set = new HashSet();
    set.add("set 1");
    set.add("set 2");
    set.add("set 3");
    return set;
  }

  private Set<String> getSet2() {
    Set<String> set = new HashSet();
    set.add("set 5");
    set.add("set 6");
    set.add("set 7");
    return set;
  }

  private List<String> getList() {
    List<String> list = new ArrayList<>();
    list.add("set 1");
    list.add("set 2");
    list.add("set 3");
    return list;
  }

  private Map<String, String> getMap() {
    Map<String, String> map = new HashMap();
    map.put("key1", "val1");
    map.put("key2", "val2");
    map.put("key3", "val3");
    return map;
  }

  private Set<AddressT> getAddressSet() {
    Set<AddressT> set = new HashSet();
    set.add(getAddress());
    return set;
  }

  private Map<String, AddressT> getAddressMap() {
    Map<String, AddressT> map = new HashMap();
    map.put("key1", getAddress());
    map.put("key2", getAddress());
    return map;
  }


  @Test
  void insertLab4Entity() {
    log.debug("insertLab4Entity");
    Lab4Entity entity = new Lab4Entity();
    entity.setSet(getSet());
    entity.setList(getList());
    entity.setMap(getMap());
    entity.setAddressSet(getAddressSet());
    entity.setAddressMap(getAddressMap());

    log.debug("-----------------------------------");
    log.debug(entity);

    Assertions.assertEquals(provider.createLab4Entity(entity), RequestStatuses.SUCCESS);
  }

  @Test
  void selectLab4Entity() {
    log.debug("selectLab4Entity");
    insertLab4Entity();
    insertLab4Entity();
    log.info(provider.selectAllLab4Entity());
  }

  @Test
  void deleteLab4Entity() {
    log.debug("deleteLab4Entity");
    insertLab4Entity();
    Lab4Entity entity = provider.selectAllLab4Entity().get().get(0);
    log.info(entity);
    Assertions.assertEquals(provider.deleteLab4Entity(entity), RequestStatuses.SUCCESS);
  }

  @Test
  void updateLab4Entity() {
    log.debug("updateLab4Entity");
    insertLab4Entity();
    Lab4Entity entity = provider.selectAllLab4Entity().get().get(0);
    entity.setSet(getSet2());
    Assertions.assertEquals(provider.updateLab4Entity(entity), RequestStatuses.SUCCESS);
  }
}
