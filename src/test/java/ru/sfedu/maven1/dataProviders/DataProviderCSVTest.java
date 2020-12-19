package ru.sfedu.maven1.dataProviders;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DataProviderCSVTest extends DataProviderTests {

  private static final DataProvider dataProvider = DataProviderCSV.getInstance();

  @BeforeAll
  static void setUp() {
    setUp(dataProvider);
  }

  @Test
  void preTest() {
    super.preTest();
  }

  @Test
  void createUserCorrect() {
    super.createUserCorrect();
  }

  @Test
  void createUserIncorrect() {
    super.createUserIncorrect();
  }

  @Test
  void getUserByIdCorrect() {
    super.getUserByIdCorrect();
  }

  @Test
  void getUserByIdIncorrect() {
    super.getUserByIdIncorrect();
  }

  @Test
  void editUserCorrect() {
    super.editUserCorrect();
  }

  @Test
  void editUserIncorrect() {
    super.editUserIncorrect();
  }

  @Test
  void deleteUserCorrect() {
    super.deleteUserCorrect();
  }

  @Test
  void deleteUserIncorrect() {
    super.deleteUserIncorrect();
  }

  @Test
  void getUsersCorrect() {
    super.getUsersCorrect();
  }

  @Test
  void getUsersIncorrect() {
    super.getUsersIncorrect();
  }

  @Test
  void getAddresses() {
    super.getAddresses();
  }

  @Test
  void getAddressCorrect() {
    super.getAddressCorrect();
  }

  @Test
  void getAddressIncorrect() {
    super.getAddressIncorrect();
  }

  @Test
  void getAddressByNameCorrect() {
    super.getAddressByNameCorrect();
  }

  @Test
  void getAddressByNameIncorrect() {
    super.getAddressByNameIncorrect();
  }

  @Test
  void getQueueCorrect() { }

  @Test
  void getQueueIncorrect() { }

  @Test
  void createDealCorrect() {
    super.createDealCorrect();
  }

  @Test
  void createDealIncorrect() {
    super.createDealIncorrect();
  }

  @Test
  void createPublicDealCorrect() {
    super.createPublicDealCorrect();
  }

  @Test
  void createPublicDealIncorrect() {
    super.createPublicDealIncorrect();
  }

  @Test
  void getGlobalDealsCorrect() {
    super.getGlobalDealsCorrect();
  }

  @Test
  void getGlobalDealsIncorrect() {
    super.getGlobalDealsIncorrect();
  }

  @Test
  void getMyDealsCorrect() {
    super.getMyDealsCorrect();
  }

  @Test
  void getMyDealsIncorrect() {
    super.getMyDealsIncorrect();
  }

  @Test
  void manageDealCorrect() {
    super.manageDealCorrect();
  }

  @Test
  void manageDealIncorrect() {
    super.manageDealIncorrect();
  }

  @Test
  void removeDealCorrect() {
    super.removeDealCorrect();
  }

  @Test
  void removeDealIncorrect() {
    super.removeDealIncorrect();
  }

  @Test
  void updateDealCorrect() {
    super.updateDealCorrect();
  }

  @Test
  void updateDealIncorrect() {
    super.updateDealIncorrect();
  }

  @Test
  void setStatusCorrect(){
    super.setStatusCorrect();
  }

  @Test
  void setStatusIncorrect(){
    super.setStatusIncorrect();
  }

  @Test
  void addDealRequestCorrect(){
    super.addDealRequestCorrect();
  }

  @Test
  void addDealRequestIncorrect(){
    super.addDealRequestIncorrect();
  }

  @Test
  void getDealQueueCorrect(){
    super.getDealQueueCorrect();
  }

  @Test
  void getDealQueueIncorrect(){
    super.getDealQueueIncorrect();
  }

  @Test
  void manageDealRequestQueueCorrect(){
    super.manageDealRequestQueueCorrect();
  }

  @Test
  void manageDealRequestIncorrect(){
    super.manageDealRequestIncorrect();
  }

  @Test
  void acceptDealRequestCorrect() {
    super.acceptDealRequestCorrect();
  }

  @Test
  void acceptDealRequestIncorrect() {
    super.acceptDealRequestIncorrect();
  }

  @Test
  void refuseDealRequestCorrect() {
    super.refuseDealRequestCorrect();
  }

  @Test
  void refuseDealRequestIncorrect() {
    super.refuseDealRequestIncorrect();
  }

  @Test
  void addDealPerformerCorrect(){
    super.addDealPerformerCorrect();
  }

  @Test
  void addDealPerformerIncorrect(){
    super.addDealPerformerIncorrect();
  }

  @Test
  void getMyQueueCorrect(){
    super.getMyQueueCorrect();
  }

  @Test
  void getMyQueueIncorrect(){
    super.getMyQueueIncorrect();
  }

  @Test
  void manageDealPerformerQueueCorrect(){
    super.manageDealPerformerQueueCorrect();
  }

  @Test
  void manageDealPerformerIncorrect(){
    super.manageDealPerformerIncorrect();
  }

  @Test
  void acceptDealPerformerCorrect() {
    super.acceptDealPerformerCorrect();
  }

  @Test
  void acceptDealPerformerIncorrect() {
    super.acceptDealPerformerIncorrect();
  }

  @Test
  void refuseDealPerformerCorrect() {
    super.refuseDealPerformerCorrect();
  }

  @Test
  void refuseDealPerformerIncorrect() {
    super.refuseDealPerformerIncorrect();
  }
}
