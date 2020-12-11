package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;

import java.util.UUID;

public class UUIDConvertor extends AbstractBeanField<UUID, Integer>  {
  @Override
  protected Object convert(String s) {
    try {
      return UUID.fromString(s);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  protected String convertToWrite(Object value) {
    try {
      return value.toString();
    } catch (NullPointerException e) {
      return null;
    }
  }
}
