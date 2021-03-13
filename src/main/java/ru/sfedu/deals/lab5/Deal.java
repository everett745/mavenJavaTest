package ru.sfedu.deals.lab5;


import ru.sfedu.deals.enums.DealModel;
import ru.sfedu.deals.enums.DealTypes;
import ru.sfedu.deals.enums.ObjectTypes;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class Deal
 */
@Entity
@Table(name = "DEAL")
public class Deal implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "DEAL_ID", unique = true, nullable = false)
  private String id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToOne (optional=false, cascade=CascadeType.ALL)
  @JoinColumn (name="address")
  private Address address;

  @OneToOne (optional=false, cascade=CascadeType.ALL)
  @JoinColumn (name="user_id")
  private User owner;

  @Enumerated(EnumType.STRING)
  private DealTypes dealType;

  @Enumerated(EnumType.STRING)
  private ObjectTypes object;

  @Enumerated(EnumType.STRING)
  private DealModel dealModel;

  @Column(name = "created_at")
  private Date created_at;

  @Column(name = "price")
  private String price;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable (name="deal_company",
          joinColumns=@JoinColumn (name="company_id"),
          inverseJoinColumns=@JoinColumn(name="deal_id"))
  private List<Company> companies;

  public Deal() {
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
   * Set the value of description
   *
   * @param newVar the new value of description
   */
  public void setDescription(String newVar) {
    description = newVar;
  }

  /**
   * Get the value of description
   *
   * @return the value of description
   */
  public String getDescription() {
    return description;
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
   * Set the value of owner
   *
   * @param newVar the new value of owner
   */
  public void setOwner(User newVar) {
    owner = newVar;
  }

  /**
   * Get the value of owner
   *
   * @return the value of owner
   */
  public User getOwner() {
    return owner;
  }

  /**
   * Set the value of dealType
   *
   * @param newVar the new value of dealType
   */
  public void setDealType(DealTypes newVar) {
    dealType = newVar;
  }

  /**
   * Get the value of dealType
   *
   * @return the value of dealType
   */
  public DealTypes getDealType() {
    return dealType;
  }

  /**
   * Set the value of object
   *
   * @param newVar the new value of object
   */
  public void setObject(ObjectTypes newVar) {
    object = newVar;
  }

  /**
   * Get the value of object
   *
   * @return the value of object
   */
  public ObjectTypes getObject() {
    return object;
  }

  /**
   * Get the value of dealModel
   *
   * @return the value of dealModel
   */
  public DealModel getDealModel() {
    return dealModel;
  }

  /**
   * Set the value of dealModel
   *
   * @param dealModel the new value of DealModel
   */
  public void setDealModel(DealModel dealModel) {
    this.dealModel = dealModel;
  }

  /**
   * Set the value of created_at
   *
   * @param newVar the new value of created_at
   */
  public void setCreated_at(Date newVar) {
    created_at = newVar;
  }

  /**
   * Get the value of created_at
   *
   * @return the value of created_at
   */
  public Date getCreated_at() {
    return created_at;
  }

  /**
   * Set the value of price
   *
   * @param newVar the new value of price
   */
  public void setPrice(String newVar) {
    price = newVar;
  }

  /**
   * Get the value of price
   *
   * @return the value of price
   */
  public String getPrice() {
    return price;
  }

  public List<Company> getCompanies() {
    return companies;
  }

  public void setCompanies(List<Company> companies) {
    this.companies = companies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Deal deal = (Deal) o;
    return Objects.equals(getId(), deal.getId()) &&
            Objects.equals(getName(), deal.getName()) &&
            Objects.equals(getDescription(), deal.getDescription()) &&
            Objects.equals(getAddress(), deal.getAddress()) &&
            Objects.equals(getOwner(), deal.getOwner()) &&
            getDealType() == deal.getDealType() &&
            getObject() == deal.getObject() &&
            getDealModel() == deal.getDealModel() &&
            Objects.equals(getCreated_at(), deal.getCreated_at()) &&
            Objects.equals(getPrice(), deal.getPrice());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
            getId(),
            getName(),
            getDescription(),
            getAddress(),
            getOwner(),
            getDealType(),
            getObject(),
            getCreated_at(),
            getPrice()
    );
  }

  @Override
  public String toString() {
    return "Deal{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", address=" + address +
            ", owner=" + owner +
            ", dealType=" + dealType +
            ", object=" + object +
            ", model=" + dealModel +
            ", created_at=" + created_at +
            ", price='" + price + '\'' +
            '}';
  }
}
