package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.Queue;

import java.util.Optional;

public class QueueConvertor extends AbstractBeanField<Queue, Integer> {
  private final DataProviderCSV dataProviderCSV = new DataProviderCSV();

  @Override
  protected Object convert(String s) {
    Optional<Queue> queueOptional = dataProviderCSV.getQueue(s);
    return queueOptional.orElse(null);
  }

  @Override
  protected String convertToWrite(Object value) {
    Queue queue = (Queue) value;
    return String.valueOf(queue.getId());
  }
}
