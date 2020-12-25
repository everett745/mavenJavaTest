package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import ru.sfedu.maven1.Constants;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDListConvertor extends AbstractBeanField<UUID, Integer> {
  final Pattern patternWithout = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITHOUT_QUOTES);
  final Pattern patternWith = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITH_QUOTES);

  @Override
  protected Object convert(String s) throws CsvDataTypeMismatchException {
    final Matcher matcherWithout = patternWithout.matcher(s);
    final Matcher matcherWith = patternWith.matcher(s);
    String indexString;

    if (matcherWithout.find()) {
      indexString = s.substring(1, s.length() - 1);
    } else if (matcherWith.find()) {
      indexString = s.substring(2, s.length() - 2);
    } else {
      throw new CsvDataTypeMismatchException();
    }

    String[] list = indexString.split(Constants.ARRAY_DELIMITER);

    return Arrays.asList(list);
  }

  @Override
  protected String convertToWrite(Object value) {
    return value.toString();
  }
}
