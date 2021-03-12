package ru.sfedu.deals.lab5;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * Class Address
 */
@Entity
@Table(name = "ADDRESS")
public class Address implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ADDRESS_ID", unique = true, nullable = false)
  private long id;

  @Column(name = "CITY", nullable = false)
  private String city;

  @Column(name = "REGION", nullable = false)
  private String region;

  @Column(name = "DISTRICT", nullable = false)
  private String district;

  @OneToMany (mappedBy="address", fetch=FetchType.LAZY)
  private Collection<Deal> deals;

  @OneToMany (mappedBy="address", fetch=FetchType.LAZY)
  private Collection<User> users;

  public Address() { }

  /**
   * Set the value of id
   *
   * @param newVar the new value of id
   */
  public void setId(long newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   *
   * @return the value of id
   */
  public long getId() {
    return id;
  }

  /**
   * Set the value of city
   *
   * @param newVar the new value of city
   */
  public void setCity(String newVar) {
    city = newVar;
  }

  /**
   * Get the value of city
   *
   * @return the value of city
   */
  public String getCity() {
    return city;
  }

  /**
   * Set the value of country
   *
   * @param newVar the new value of country
   */
  public void setRegion(String newVar) {
    region = newVar;
  }

  /**
   * Get the value of country
   *
   * @return the value of country
   */
  public String getRegion() {
    return region;
  }

  /**
   * Set the value of region
   *
   * @param newVar the new value of region
   */
  public void setDistrict(String newVar) {
    district = newVar;
  }

  /**
   * Get the value of region
   *
   * @return the value of region
   */
  public String getDistrict() {
    return district;
  }

  public Collection<Deal> getDeals() {
    return deals;
  }

  public void setDeals(Collection<Deal> deals) {
    this.deals = deals;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public void setUsers(Collection<User> users) {
    this.users = users;
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
