package ru.sfedu.deals.lab4.list;

import javax.persistence.*;
import java.util.*;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "LAST_NAME")
  private String last_name;

  @ElementCollection
  @CollectionTable(name = "PHONE")
  @OrderColumn
  @Column(name = "PHONE_NAME")
  protected List<String> phones = new ArrayList<>();

  public User() { }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public List<String> getPhones() {
    return phones;
  }

  public void setPhones(List<String> phones) {
    this.phones = phones;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return getId() == user.getId() &&
            Objects.equals(getName(), user.getName()) &&
            Objects.equals(getLast_name(), user.getLast_name()) &&
            Objects.equals(getPhones(), user.getPhones());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getLast_name(), getPhones());
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", phones=" + phones +
            '}';
  }
}
