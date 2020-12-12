package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.maven1.dataConvertors.UUIDConvertor;
import ru.sfedu.maven1.dataConvertors.UUIDListConvertor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class Company
 */
public class Company {

  //
  // Fields
  //

  @CsvCustomBindByName(converter = UUIDConvertor.class)
  private UUID id;
  @CsvCustomBindByName(converter = UUIDListConvertor.class)
  private List<UUID> employees = new ArrayList<UUID>();
  @CsvCustomBindByName(converter = UUIDListConvertor.class)
  private List<UUID> deals  = new ArrayList<UUID>();
  
  //
  // Constructors
  //
  public Company () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of id
   * @param newVar the new value of id
   */
  public void setId (UUID newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   * @return the value of id
   */
  public UUID getId () {
    return id;
  }

  /**
   * Set the value of employees
   * @param newVar the new value of employees
   */
  public void setEmployees (List<UUID> newVar) {
    employees = newVar;
  }

  /**
   * Get the value of employees
   * @return the value of employees
   */
  public List<UUID> getEmployees () {
    return employees;
  }

  /**
   * Set the value of deals
   * @param newVar the new value of deals
   */
  public void setDeals (List<UUID> newVar) {
    deals = newVar;
  }

  /**
   * Get the value of deals
   * @return the value of deals
   */
  public List<UUID> getDeals () {
    return deals;
  }

  //
  // Other methods
  //

}
