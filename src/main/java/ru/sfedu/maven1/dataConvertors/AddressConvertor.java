package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.maven1.Main;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.Address;

public class AddressConvertor extends AbstractBeanField<Address, Integer> {
  private static Logger log = LogManager.getLogger(Main.class);
  private DataProviderCSV dataProviderCSV = new DataProviderCSV();

  @Override
  protected Object convert(String s) {
    return dataProviderCSV.getAddress(Long.parseLong(s)).get();
  }

  @Override
  protected String convertToWrite(Object value) {
    Address address = (Address) value;
    return String.valueOf(address.getId());
  }
}
