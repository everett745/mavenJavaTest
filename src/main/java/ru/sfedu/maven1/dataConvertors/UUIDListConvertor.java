package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.maven1.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UUIDListConvertor extends AbstractBeanField<UUID, Integer> {
  final Pattern patternWithout = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITHOUT_QUOTES);
  final Pattern patternWith = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITH_QUOTES);

  @Override
  protected Object convert(String s) {
    final Matcher matcherWithout = patternWithout.matcher(s);
    final Matcher matcherWith = patternWith.matcher(s);
    String indexString;

    if (matcherWithout.find()) {
      indexString = s.substring(1, s.length() - 1);
    } else if (matcherWith.find()) {
      indexString = s.substring(2, s.length() - 2);
    } else {
      return new ArrayList<UUID>();
    }

    String[] uuids = indexString.split(Constants.ARRAY_DELIMITER);

    if (uuids.length == 0 || uuids[0].equals(Constants.EMPTY_STRING)) {
      return new ArrayList<UUID>();
    } else {
      return Arrays.stream(uuids)
              .map(UUID::fromString)
              .collect(Collectors.toList());
    }
  }

  @Override
  protected String convertToWrite(Object value) {
    return value.toString();
  }
}
