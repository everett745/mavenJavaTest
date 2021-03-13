package ru.sfedu.deals.lab5;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USER")
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID", unique = true, nullable = false)
  private String id;

  @ManyToOne (optional=false, cascade=CascadeType.ALL)
  @JoinColumn (name="address")
  private Address address;

  @Column(name = "NAME")
  private String name;

  @Column(name = "phone")
  private String phone;

  public User() {
  }

  /**
   * Set the value of id
   *
   * @param newVar the new value of id
   */
  public void setId(String newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   *
   * @return the value of id
   */
  public String getId() {
    return id;
  }

  /**
   * Set the value of address
   *
   * @param newVar the new value of address
   */
  public void setAddress(Address newVar) {
    address = newVar;
  }

  /**
   * Get the value of address
   *
   * @return the value of address
   */
  public Address getAddress() {
    return address;
  }

  /**
   * Set the value of name
   *
   * @param newVar the new value of name
   */
  public void setName(String newVar) {
    name = newVar;
  }

  /**
   * Get the value of name
   *
   * @return the value of name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the value of phone
   *
   * @param newVar the new value of phone
   */
  public void setPhone(String newVar) {
    phone = newVar;
  }

  /**
   * Get the value of phone
   *
   * @return the value of phone
   */
  public String getPhone() {
    return phone;
  }


  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", address=" + address +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(getId(), user.getId()) &&
            Objects.equals(getAddress(), user.getAddress()) &&
            Objects.equals(getName(), user.getName()) &&
            Objects.equals(getPhone(), user.getPhone());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getAddress(), getName(), getPhone());
  }
}
