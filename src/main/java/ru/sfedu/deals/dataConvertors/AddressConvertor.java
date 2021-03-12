package ru.sfedu.deals.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.deals.dataProviders.DataProviderCSV;
import ru.sfedu.deals.model.Address;

import java.util.Optional;

public class AddressConvertor extends AbstractBeanField<Address, Integer> {
  private final DataProviderCSV dataProviderCSV = new DataProviderCSV();

  @Override
  protected Object convert(String s) {
    Optional<Address> address = dataProviderCSV.getAddress(Long.parseLong(s));
    return address.orElseGet(Address::new);
  }

  @Override
  protected String convertToWrite(Object value) {
    Address address = (Address) value;
    return String.valueOf(address.getId());
  }
}
