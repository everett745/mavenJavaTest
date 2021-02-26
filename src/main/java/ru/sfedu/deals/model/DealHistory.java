package ru.sfedu.deals.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import ru.sfedu.deals.Constants;
import ru.sfedu.deals.enums.DealStatus;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class DealHistory
 */
@Entity
@Table(name = "DEAL_HISTORY", schema = "PUBLIC", catalog = "DEALSDB")
public class DealHistory implements Serializable {

  @CsvBindByName
  private String id;

  @CsvBindByName
  private String text;

  @CsvBindByName
  private DealStatus status;

  @CsvDate(value = Constants.DATE_FORMAT)
  private Date created_at;

  public DealHistory() {
  }

  /**
   * Set the value of id
   *
   * @param newVar the new value of id
   */
  @Element(name = "id")
  public void setId(String newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   *
   * @return the value of id
   */
  @Basic
  @Column(name = "ID", nullable = true, length = -1)
  @Element(name = "id")
  public String getId() {
    return id;
  }

  /**
   * Set the value of text
   *
   * @param newVar the new value of text
   */
  @Attribute(name = "text")
  public void setText(String newVar) {
    text = newVar;
  }

  /**
   * Get the value of text
   *
   * @return the value of text
   */
  @Basic
  @Column(name = "TEXT", nullable = true, length = -1)
  @Attribute(name = "text")
  public String getText() {
    return text;
  }

  /**
   * Set the value of status
   *
   * @param newVar the new value of status
   */
  @Attribute(name = "status")
  public void setStatus(DealStatus newVar) {
    status = newVar;
  }

  /**
   * Get the value of status
   *
   * @return the value of status
   */
  @Basic
  @Column(name = "STATUS", nullable = true, length = -1)
  @Attribute(name = "status")
  public DealStatus getStatus() {
    return status;
  }

  /**
   * Set the value of created_at
   *
   * @param newVar the new value of created_at
   */
  @Attribute(name = "created_at")
  public void setCreated_at(Date newVar) {
    created_at = newVar;
  }

  /**
   * Get the value of created_at
   *
   * @return the value of created_at
   */
  @Basic
  @Column(name = "CREATED_AT", nullable = true)
  @Attribute(name = "created_at")
  public Date getCreated_at() {
    return created_at;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DealHistory that = (DealHistory) o;
    return getId().equals(that.getId()) &&
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

}
