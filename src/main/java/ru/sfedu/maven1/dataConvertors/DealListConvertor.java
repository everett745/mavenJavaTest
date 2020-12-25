package ru.sfedu.maven1.dataConvertors;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.dataProviders.DataProviderCSV;
import ru.sfedu.maven1.model.Deal;
import ru.sfedu.maven1.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealListConvertor extends AbstractBeanField<Deal, Integer> {
  final Pattern patternWithout = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITHOUT_QUOTES);
  final Pattern patternWith = Pattern.compile(Constants.CONVERTER_REGEXP_LIST_WITH_QUOTES);
  private static final Logger log = LogManager.getLogger(UsersListConvertor.class);
  private final DataProviderCSV dataProviderCSV = new DataProviderCSV();

  @Override
  protected Object convert(String s) {
    final Matcher matcherWithout = patternWithout.matcher(s);
    final Matcher matcherWith = patternWith.matcher(s);
    String indexString;

    try {
      if (matcherWithout.find()) {
        indexString = s.substring(1, s.length() - 1);
      } else if (matcherWith.find()) {
        indexString = s.substring(2, s.length() - 2);
      } else {
        return new ArrayList<Deal>();
      }

      String[] uuids = indexString.split(Constants.ARRAY_DELIMITER);

      if (uuids.length == 0 || uuids[0].equals(Constants.EMPTY_STRING)) {
        return new ArrayList<Deal>();
      } else {
        List<Deal> deals = new ArrayList<>();
        Arrays.asList(uuids)
                .forEach(uuid -> {
                  Optional<Deal> optionalDeal = dataProviderCSV.manageDeal(uuid);
                  optionalDeal.ifPresent(deals::add);
                });
        return deals;
      }
    } catch (Exception e) {
      log.error(e);
      return null;
    }
  }

  @Override
  protected String convertToWrite(Object value) {
    List<Deal> userList = (List<Deal>) value;
    List<String> uuids = new ArrayList<>();
    userList.forEach(item -> uuids.add(item.getId()));
    return uuids.toString();
  }
}
