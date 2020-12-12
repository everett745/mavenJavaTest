package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.Queue;

import java.util.UUID;

public class QueueConvertor extends AbstractBeanField<Queue, Integer> {
  private DataProviderCSV dataProviderCSV = new DataProviderCSV();

  @Override
  protected Object convert(String s) {
    return dataProviderCSV.getQueue(UUID.fromString(s)).get();
  }

  @Override
  protected String convertToWrite(Object value) {
    Queue queue = (Queue) value;
    return String.valueOf(queue.getId());
  }
}
