package ru.sfedu.deals.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "TEST_ENTITY", schema = "PUBLIC", catalog = "DEALSDB")
public class TestEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private long id;
  @Column(name = "NAME", nullable = false)
  private String name;
  @Column(name = "DESCRIPTION", nullable = false)
  private String description;
  @Column(name = "DATECREATE", nullable = false)
  private Date dateCreated;
  @Column(name = "CHECK_", nullable = false)
  private Boolean check;

  @Embedded
  private AddressT address;

  public TestEntity() {}

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Boolean getCheck() {
    return check;
  }

  public void setCheck(Boolean check) {
    this.check = check;
  }

  public AddressT getAddress() {
    return address;
  }

  public void setAddress(AddressT address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "TestEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", dateCreated=" + dateCreated +
            ", check=" + check +
            ", Address=" + address +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestEntity that = (TestEntity) o;
    return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getDescription(), that.getDescription()) &&
            Objects.equals(getDateCreated(), that.getDateCreated()) &&
            Objects.equals(getCheck(), that.getCheck());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getDescription(), getDateCreated(), getCheck());
  }
}
