package ru.sfedu.maven1.model;

import java.util.List;

/**
 * Class Company
 */
public class Company {

  //
  // Fields
  //

  private long id;
  private List<User> employees;
  private List<Deal> deals;
  
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
  public void setId (long newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   * @return the value of id
   */
  public long getId () {
    return id;
  }

  /**
   * Set the value of employees
   * @param newVar the new value of employees
   */
  public void setEmployees (List<User> newVar) {
    employees = newVar;
  }

  /**
   * Get the value of employees
   * @return the value of employees
   */
  public List<User> getEmployees () {
    return employees;
  }

  /**
   * Set the value of deals
   * @param newVar the new value of deals
   */
  public void setDeals (List<Deal> newVar) {
    deals = newVar;
  }

  /**
   * Get the value of deals
   * @return the value of deals
   */
  public List<Deal> getDeals () {
    return deals;
  }

  //
  // Other methods
  //

}
