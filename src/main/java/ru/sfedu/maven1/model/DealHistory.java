package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import ru.sfedu.maven1.enums.DealStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class DealHistory
 */
public class DealHistory implements Serializable {

  //
  // Fields
  //

  private long id;
  @CsvBindByName
  private String text;
  @CsvBindByName
  private DealStatus status;
  @CsvBindByName
  private Date created_at;
  
  //
  // Constructors
  //
  public DealHistory () { };
  
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
   * Set the value of text
   * @param newVar the new value of text
   */
  public void setText (String newVar) {
    text = newVar;
  }

  /**
   * Get the value of text
   * @return the value of text
   */
  public String getText () {
    return text;
  }

  /**
   * Set the value of status
   * @param newVar the new value of status
   */
  public void setStatus (DealStatus newVar) {
    status = newVar;
  }

  /**
   * Get the value of status
   * @return the value of status
   */
  public DealStatus getStatus () {
    return status;
  }

  /**
   * Set the value of created_at
   * @param newVar the new value of created_at
   */
  public void setCreated_at (Date newVar) {
    created_at = newVar;
  }

  /**
   * Get the value of created_at
   * @return the value of created_at
   */
  public Date getCreated_at () {
    return created_at;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DealHistory that = (DealHistory) o;
    return getId() == that.getId() &&
            Objects.equals(getText(), that.getText()) &&
            getStatus() == that.getStatus() &&
            Objects.equals(getCreated_at(), that.getCreated_at());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getText(), getStatus(), getCreated_at());
  }

  @Override
  public String toString() {
    return "DealHistory{" +
            "id=" + id +
            ", text='" + text + '\'' +
            ", status=" + status +
            ", created_at=" + created_at +
            '}';
  }

  //
  // Other methods
  //

}
