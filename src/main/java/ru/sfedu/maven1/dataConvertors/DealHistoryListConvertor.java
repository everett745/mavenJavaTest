package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.DealHistory;

import java.util.*;

public class DealHistoryListConvertor extends AbstractBeanField<UUID, Integer> {
  private final DataProviderCSV dataProviderCSV = new DataProviderCSV();

  @Override
  protected Object convert(String s) {
    Optional<List<DealHistory>> dealHistoryList = dataProviderCSV.getDealHistoryByDeal(s);

    return dealHistoryList.orElseGet(ArrayList::new);
  }

  @Override
  protected String convertToWrite(Object value) {
    List<DealHistory> dealHistoryList = (List<DealHistory>) value;
    return dealHistoryList.get(0).getId();
  }
}
