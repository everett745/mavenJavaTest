package ru.sfedu.maven1.model;

import java.util.List;

/**
 * Class PublicDeal
 */
public class PublicDeal extends Deal {

  //
  // Fields
  //

  private List<DealHistory> history;
  
  //
  // Constructors
  //
  public PublicDeal () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of history
   * @param newVar the new value of history
   */
  public void setHistory (List<DealHistory> newVar) {
    history = newVar;
  }

  /**
   * Get the value of history
   * @return the value of history
   */
  public List<DealHistory> getHistory () {
    return history;
  }

  //
  // Other methods
  //

}
