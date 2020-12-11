package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.maven1.Main;
import ru.sfedu.maven1.model.Address;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConvertor extends AbstractBeanField<Date, Integer> {
  private static Logger log = LogManager.getLogger(Main.class);

  @Override
  protected Object convert(String s) {
    System.out.println(s);
    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    try {
      System.out.println(format.parse(s));
      return format.parse(s);
    } catch (ParseException e) {
      log.error(e);
      return new Date();
    }
  }

  @Override
  protected String convertToWrite(Object value) {
    return value.toString();
  }
}
