package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.maven1.model.Queue;

import java.util.UUID;

public class UUIDConvertor extends AbstractBeanField<Queue, Integer>  {
  @Override
  protected Object convert(String s) {
    return UUID.fromString(s);
  }

  @Override
  protected String convertToWrite(Object value) {
    return value.toString();
  }
}
