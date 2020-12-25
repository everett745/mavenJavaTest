package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.maven1.dataConvertors.AddressConvertor;
import ru.sfedu.maven1.dataConvertors.QueueConvertor;

import java.io.Serializable;
import java.util.Objects;

@Root(name = "User")
public class User implements Serializable {

  @CsvBindByName
  private String id;

  @CsvCustomBindByName(converter = AddressConvertor.class)
  private Address address;

  @CsvCustomBindByName(converter = QueueConvertor.class)
  private Queue queue;

  @CsvBindByName
  private String name;

  @CsvBindByName
  private String phone;

  public User() {
  }

  /**
   * Set the value of id
   *
   * @param newVar the new value of id
   */
  @Attribute(name = "id")
  public void setId(String newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   *
   * @return the value of id
   */
  @Attribute(name = "id")
  public String getId() {
    return id;
  }

  /**
   * Set the value of address
   *
   * @param newVar the new value of address
   */
  @Element(name = "address")
  public void setAddress(Address newVar) {
    address = newVar;
  }

  /**
   * Get the value of address
   *
   * @return the value of address
   */
  @Element(name = "address")
  public Address getAddress() {
    return address;
  }

  /**
   * Set the value of queue
   *
   * @param newVar the new value of queue
   */
  @Element(name = "queue", required = false)
  public void setQueue(Queue newVar) {
    queue = newVar;
  }

  /**
   * Get the value of queue
   *
   * @return the value of queue
   */
  @Element(name = "queue", required = false)
  public Queue getQueue() {
    return queue;
  }

  /**
   * Set the value of name
   *
   * @param newVar the new value of name
   */
  @Attribute(name = "name")
  public void setName(String newVar) {
    name = newVar;
  }

  /**
   * Get the value of name
   *
   * @return the value of name
   */
  @Attribute(name = "name")
  public String getName() {
    return name;
  }

  /**
   * Set the value of phone
   *
   * @param newVar the new value of phone
   */
  @Attribute(name = "phone")
  public void setPhone(String newVar) {
    phone = newVar;
  }

  /**
   * Get the value of phone
   *
   * @return the value of phone
   */
  @Attribute(name = "phone")
  public String getPhone() {
    return phone;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", address=" + address +
            ", queue=" + queue +
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
            Objects.equals(getQueue(), user.getQueue()) &&
            Objects.equals(getName(), user.getName()) &&
            Objects.equals(getPhone(), user.getPhone());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getAddress(), getQueue(), getName(), getPhone());
  }
}
