package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import ru.sfedu.maven1.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UUIDArrayConvertor extends AbstractBeanField<UUID, Integer> {
  @Override
  protected Object convert(String s) {
    String indexString;
//    if (s.matches(Constants.CONVERTER_REGEXP_LIST_WITHOUT_QUOTES)) {
//      indexString = s.substring(1, s.length() - 1);
//    } else {
//      indexString = s.substring(2, s.length() - 2);
//    }
    indexString = s.substring(1, s.length() - 1);

    String[] uuids = indexString.split(", ");

    if (uuids.length == 0 || uuids[0].equals("")) {
      return new ArrayList<UUID>();
    } else {
      return Arrays.stream(uuids).map(UUID::fromString).collect(Collectors.toList());
    }
  }

  @Override
  protected String convertToWrite(Object value) {
    return value.toString();
  }
}
