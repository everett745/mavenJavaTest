package ru.sfedu.deals.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "LAB4_ENTITY", schema = "PUBLIC", catalog = "DEALSDB")
public class Lab4Entity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private long id;

  @ElementCollection
  @CollectionTable(name = "SET", joinColumns = @JoinColumn(name = "SET_ID"))
  @Column(name = "SET_NAME")
  protected Set<String> set = new HashSet<String>();

  @ElementCollection
  @CollectionTable(name = "LIST")
  @OrderColumn
  @Column(name = "LIST_NAME")
  protected List<String> list = new ArrayList<>();

  @ElementCollection
  @CollectionTable(name = "MAP")
  @MapKeyColumn(name = "MAP_KEY_NAME")
  @Column(name = "MAP_NAME")
  protected Map<String, String> map = new HashMap<>();

  @ElementCollection
  @CollectionTable(name = "ADDRESS_SET")
  @AttributeOverride(
          name = "address",
          column = @Column(name = "address_name", nullable = false)
  )
  protected Set<AddressT> addressSet = new HashSet<>();

  @ElementCollection
  @CollectionTable(name = "ADDRESS_MAP")
  @MapKeyColumn(name = "ADDRESS_MAP_KEY_NAME")
  protected Map<String, AddressT> addressMap = new HashMap<>();

  public Lab4Entity() { }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Set<String> getSet() {
    return set;
  }

  public void setSet(Set<String> set) {
    this.set = set;
  }

  public List<String> getList() {
    return list;
  }

  public void setList(List<String> list) {
    this.list = list;
  }

  public Map<String, String> getMap() {
    return map;
  }

  public void setMap(Map<String, String> map) {
    this.map = map;
  }

  public Set<AddressT> getAddressSet() {
    return addressSet;
  }

  public void setAddressSet(Set<AddressT> addressSet) {
    this.addressSet = addressSet;
  }

  public Map<String, AddressT> getAddressMap() {
    return addressMap;
  }

  public void setAddressMap(Map<String, AddressT> addressMap) {
    this.addressMap = addressMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lab4Entity that = (Lab4Entity) o;
    return getId() == that.getId() &&
            Objects.equals(getSet(), that.getSet()) &&
            Objects.equals(getList(), that.getList()) &&
            Objects.equals(getMap(), that.getMap()) &&
            Objects.equals(getAddressSet(), that.getAddressSet()) &&
            Objects.equals(getAddressMap(), that.getAddressMap());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getSet(), getList(), getMap(), getAddressSet(), getAddressMap());
  }

  @Override
  public String toString() {
    return "Lab4Entity{" +
            "id=" + id +
            ", set=" + set +
            ", list=" + list +
            ", map=" + map +
            ", addressSet=" + addressSet +
            ", addressMap=" + addressMap +
            '}';
  }
}
