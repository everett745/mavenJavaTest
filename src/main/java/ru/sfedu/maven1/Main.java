package ru.sfedu.maven1;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.User;

import java.io.IOException;
import java.util.*;

public class Main {

  private static Logger log = LogManager.getLogger(Main.class);

  public static void main(String args[]) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
//    User user = new User();
//    user.setId(1);
//    user.setName("Test Name");
//
//    List<User> listUsers = new ArrayList<>();
//    listUsers.add(user);
//
//    DataProviderCSV providerCSV = new DataProviderCSV();
//    providerCSV.insertUsers(listUsers);

  }
}
