package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.util.UUID;

public class UUIDConvertor extends AbstractBeanField<UUID, Integer> {
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
