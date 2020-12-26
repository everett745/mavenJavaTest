package ru.sfedu.deals.enums;

public enum DealStatus {
  NEW_DEAL("Новая сделка"),
  FIRST_CONTACT("Первый контакт"),
  AD("Заклечен АД"),
  DOY("Заключен ДОУ"),
  SUCCESS_CLOSED("Успешно закрыта"),
  AWAIT("Отложенный спрос"),
  CANCEL("Отказ");

  private final String message;

  DealStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
