package ru.sfedu.maven1.dataConvertors;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.maven1.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListConvertor {
  private static final Logger log = LogManager.getLogger(ListConvertor.class);
  final Pattern patternWithout = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITHOUT_QUOTES);
  final Pattern patternWith = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITH_QUOTES);

  public final <T> List<String> convert(String s) {
    final Matcher matcherWithout = patternWithout.matcher(s);
    final Matcher matcherWith = patternWith.matcher(s);
    String indexString;

    try {
      if (matcherWithout.find()) {
        indexString = s.substring(1, s.length() - 1);
      } else if (matcherWith.find()) {
        indexString = s.substring(2, s.length() - 2);
      } else {
        throw new CsvDataTypeMismatchException();
      }

      String[] list = indexString.split(Constants.ARRAY_DELIMITER);

      return Arrays.asList(list);
    } catch (CsvDataTypeMismatchException e) {
      log.error(e);
      return new ArrayList<>();
    }
  }
}
