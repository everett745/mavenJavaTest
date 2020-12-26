package ru.sfedu.deals.dataProviders;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataProviderXMLTest extends DataProviderTests {
  private static final IDataProvider I_DATA_PROVIDER = DataProviderXML.getInstance();

  @BeforeAll
  static void refreshDB() {
    refreshDB(I_DATA_PROVIDER);
  }

  @BeforeEach
  void setUp() {
    setUp(I_DATA_PROVIDER);
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
  void getAddressesCorrect() {
    super.getAddressesCorrect();
  }

  @Test
  void getAddressesIncorrect() {
    super.getAddressesIncorrect();
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
  void removeAddressCorrect() {
    super.removeAddressCorrect();
  }

  @Test
  void removeAddressIncorrect() {
    super.removeAddressIncorrect();
  }

  @Test
  void updateAddressCorrect() {
    super.updateAddressCorrect();
  }

  @Test
  void updateAddressIncorrect() {
    super.updateAddressIncorrect();
  }

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
  void manageDealPerformerCorrect(){
    super.manageDealPerformerCorrect();
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

  @Test
  void createCompanyCorrect() {
    super.createCompanyCorrect();
  }

  @Test
  void createCompanyIncorrect() {
    super.createCompanyIncorrect();
  }

  @Test
  void getCompanyCorrect() {
    super.getCompanyCorrect();
  }

  @Test
  void getCompanyIncorrect() {
    super.getCompanyIncorrect();
  }

  @Test
  void updateCompanyCorrect() {
    super.updateCompanyCorrect();
  }

  @Test
  void updateCompanyIncorrect() {
    super.updateCompanyIncorrect();
  }

  @Test
  void deleteCompanyCorrect() {
    super.deleteCompanyCorrect();
  }

  @Test
  void deleteCompanyIncorrect() {
    super.deleteCompanyIncorrect();
  }

  @Test
  void createQueueCorrect() {
    super.createQueueCorrect();
  }

  @Test
  void createQueueIncorrect() {
    super.createQueueIncorrect();
  }

  @Test
  void getQueueCorrect() {
    super.getQueueCorrect();
  }

  @Test
  void getQueueIncorrect() {
    super.getQueueIncorrect();
  }

  @Test
  void updateQueueCorrect() {
    super.updateQueueCorrect();
  }

  @Test
  void updateQueueIncorrect() {
    super.updateQueueIncorrect();
  }

  @Test
  void deleteQueueCorrect() {
    super.deleteQueueCorrect();
  }

  @Test
  void deleteQueueIncorrect() {
    super.deleteQueueIncorrect();
  }

  @Test
  void createDealHistoryCorrect() {
    super.createDealHistoryCorrect();
  }

  @Test
  void createDealHistoryIncorrect() {
    super.createDealHistoryIncorrect();
  }

  @Test
  void getDealHistoryCorrect() {
    super.getDealHistoryCorrect();
  }

  @Test
  void getDealHistoryIncorrect() {
    super.getDealHistoryIncorrect();
  }

  @Test
  void updateDealHistoryCorrect() {
    super.updateDealHistoryCorrect();
  }

  @Test
  void updateDealHistoryIncorrect() {
    super.updateDealHistoryIncorrect();
  }

  @Test
  void deleteDealHistoryCorrect() {
    super.deleteDealHistoryCorrect();
  }

  @Test
  void deleteDealHistoryIncorrect() {
    super.deleteDealHistoryIncorrect();
  }
}
