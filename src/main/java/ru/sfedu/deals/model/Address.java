package ru.sfedu.deals.model;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Attribute;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class Address
 */
@Entity
@Embeddable
@Table(name = "ADDRESS", schema = "PUBLIC", catalog = "DEALSDB")
public class Address implements Serializable {

  @Id
  @Column(name = "ID", unique = true, nullable = false)
  @CsvBindByName(column = "ID")
  private long id;

  @Column(name = "CITY", unique = true, nullable = false)
  @CsvBindByName(column = "CITY")
  private String city;

  @Column(name = "REGION", unique = true, nullable = false)
  @CsvBindByName(column = "REGION")
  private String region;

  @Column(name = "DISTRICT", unique = true, nullable = false)
  @CsvBindByName(column = "DISTRICT")
  private String district;

  public Address() { }

  /**
   * Set the value of id
   *
   * @param newVar the new value of id
   */
  @Attribute(name = "id")
  public void setId(long newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   *
   * @return the value of id
   */
  @Id
  @Column(name = "ID", nullable = false)
  @Attribute(name = "id")
  public long getId() {
    return id;
  }

  /**
   * Set the value of city
   *
   * @param newVar the new value of city
   */
  @Attribute(name = "city")
  public void setCity(String newVar) {
    city = newVar;
  }

  /**
   * Get the value of city
   *
   * @return the value of city
   */
  @Basic
  @Column(name = "CITY", nullable = true, length = -1)
  @Attribute(name = "city")
  public String getCity() {
    return city;
  }

  /**
   * Set the value of country
   *
   * @param newVar the new value of country
   */
  @Attribute(name = "region")
  public void setRegion(String newVar) {
    region = newVar;
  }

  /**
   * Get the value of country
   *
   * @return the value of country
   */
  @Basic
  @Column(name = "REGION", nullable = true, length = -1)
  @Attribute(name = "region")
  public String getRegion() {
    return region;
  }

  /**
   * Set the value of region
   *
   * @param newVar the new value of region
   */
  @Attribute(name = "district")
  public void setDistrict(String newVar) {
    district = newVar;
  }

  /**
   * Get the value of region
   *
   * @return the value of region
   */
  @Basic
  @Column(name = "DISTRICT", nullable = true, length = -1)
  @Attribute(name = "district")
  public String getDistrict() {
    return district;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return getId() == address.getId() &&
            Objects.equals(getCity(), address.getCity()) &&
            Objects.equals(getRegion(), address.getRegion()) &&
            Objects.equals(getDistrict(), address.getDistrict());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCity(), getRegion(), getDistrict());
  }

  @Override
  public String toString() {
    return "Address{" +
            "id=" + id +
            ", city='" + city + '\'' +
            ", region='" + region + '\'' +
            ", district='" + district + '\'' +
            '}';
  }
}
