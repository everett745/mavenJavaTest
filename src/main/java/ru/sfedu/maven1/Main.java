package ru.sfedu.maven1;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.Address;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;

import java.util.UUID;

public class Main {

  private static Logger log = LogManager.getLogger(Main.class);

  public static void main(String args[]) {

    DataProviderCSV dataProviderCSV = new DataProviderCSV();

    Address address = dataProviderCSV.getAddress(1).get();
    Address address1 = dataProviderCSV.getAddress(2).get();
    Address address2 = dataProviderCSV.getAddress(3).get();
//
//
     dataProviderCSV.createUser("Иван", "896416", address);

    dataProviderCSV.createUser("Адрей", "", address1);
    dataProviderCSV.createUser("Семен", "", address2);


     System.out.println(dataProviderCSV.getUserList());

  }
}
