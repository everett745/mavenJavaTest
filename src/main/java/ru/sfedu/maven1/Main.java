package ru.sfedu.maven1;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.model.*;

import java.util.Optional;
import java.util.UUID;

public class Main {

  private static Logger log = LogManager.getLogger(Main.class);

  public static void main(String args[]) {

    DataProviderCSV dataProviderCSV = new DataProviderCSV();
    Address address = new Address();
    address.setId(1);

    // dataProviderCSV.createUser("Иванов Иван", "7864514", address);

//      Optional<User> userO = dataProviderCSV.getUser(UUID.fromString("05b9499d-c8fd-452b-a807-6fba0c797d56"));
//      User user = userO.get();
//      dataProviderCSV.createDeal(
//              user.getId(),
//              "Новая сделка",
//              "Тестовое описание",
//              user.getAddress(),
//              DealTypes.PURCHASE,
//              ObjectTypes.BUILDING,
//              "100000"
//      );

    // System.out.println(dataProviderCSV.getUser(UUID.fromString("05b9499d-c8fd-452b-a807-6fba0c797d56")));

    Optional<Deal> dealO = dataProviderCSV.manageDeal(UUID.fromString("79356e63-da22-4048-978f-aae08265063a"));
    Deal deal = dealO.get();


    deal.setPrice("23423");
    deal.setName("новое имя");
    dataProviderCSV.updateDeal(deal);

    System.out.println(dataProviderCSV.manageDeal(UUID.fromString("79356e63-da22-4048-978f-aae08265063a")));
     // System.out.println(dataProviderCSV.removeDeal(UUID.fromString("cbd166ab-4d06-4a9c-b799-aef04d77546a")));

  }
}
