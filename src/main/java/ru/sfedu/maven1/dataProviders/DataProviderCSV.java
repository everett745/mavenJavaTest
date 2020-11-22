package ru.sfedu.maven1.dataProviders;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.Main;
import ru.sfedu.maven1.model.User;
import ru.sfedu.maven1.utils.PropertyProvider;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class DataProviderCSV {

  private static Logger log = LogManager.getLogger(Main.class);


  public <T> void insertIntoCsv(List<T> object) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    try {
      FileWriter writer = new FileWriter(this.getFilePath(object.get(0).getClass()));

      CSVWriter csvWriter = new CSVWriter(writer);
      StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
              .withApplyQuotesToAll(false)
              .build();

      beanToCsv.write(object);
      csvWriter.close();
    } catch (IndexOutOfBoundsException e) {
      log.error(e);
    }
  }

  public User getUserById(long id) throws IOException {
    FileReader fileReader = new FileReader(this.getFilePath(User.class));

    CSVReader csvReader = new CSVReader(fileReader);
    CsvToBean<User> csvToBean = new CsvToBeanBuilder<User>(csvReader)
            .withType(User.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build();

    List<User> listUser = csvToBean.parse();

    try {
      User user = listUser.stream()
              .filter(el -> el.getId() == id)
              .findFirst()
              .get();

      return user;
    } catch (NoSuchElementException e) {
      log.error(e);
      return null;
    }
  }


  private String getFilePath(Class cl) throws IOException{
    System.out.println(PropertyProvider.getProperty(Constants.CSV_PATH)
            + cl.getSimpleName().toString().toLowerCase()
            + PropertyProvider.getProperty(Constants.CSV_EXTENSION));
    return PropertyProvider.getProperty(Constants.CSV_PATH)
            + cl.getSimpleName().toString().toLowerCase()
            + PropertyProvider.getProperty(Constants.CSV_EXTENSION);
  }
}
