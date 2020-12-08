package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class Deal
 */
public class Deal implements Serializable {

  //
  // Fields
  //

  @CsvBindByName
  private long id;
  @CsvBindByName
  private String name;
  @CsvBindByName
  private String description;
  @CsvBindByName
  private Address address;
  @CsvBindByName
  private Queue requests;
  @CsvBindByName
  private long owner;
  @CsvBindByName
  private long performer;
  @CsvBindByName
  private DealTypes dealType;
  @CsvBindByName
  private ObjectTypes object;
  @CsvBindByName
  private Date created_at;
  @CsvBindByName
  private String price;
  
  //
  // Constructors
  //
  public Deal () { };
  
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
   * Set the value of name
   * @param newVar the new value of name
   */
  public void setName (String newVar) {
    name = newVar;
  }

  /**
   * Get the value of name
   * @return the value of name
   */
  public String getName () {
    return name;
  }

  /**
   * Set the value of description
   * @param newVar the new value of description
   */
  public void setDescription (String newVar) {
    description = newVar;
  }

  /**
   * Get the value of description
   * @return the value of description
   */
  public String getDescription () {
    return description;
  }

  /**
   * Set the value of address
   * @param newVar the new value of address
   */
  public void setAddress (Address newVar) {
    address = newVar;
  }

  /**
   * Get the value of address
   * @return the value of address
   */
  public Address getAddress () {
    return address;
  }

  /**
   * Set the value of requests
   * @param newVar the new value of requests
   */
  public void setRequests (Queue newVar) {
    requests = newVar;
  }

  /**
   * Get the value of requests
   * @return the value of requests
   */
  public Queue getRequests () {
    return requests;
  }

  /**
   * Set the value of owner
   * @param newVar the new value of owner
   */
  public void setOwner (long newVar) {
    owner = newVar;
  }

  /**
   * Get the value of owner
   * @return the value of owner
   */
  public long getOwner () {
    return owner;
  }

  /**
   * Set the value of performer
   * @param newVar the new value of performer
   */
  public void setPerformer (long newVar) {
    performer = newVar;
  }

  /**
   * Get the value of performer
   * @return the value of performer
   */
  public long getPerformer () {
    return performer;
  }

  /**
   * Set the value of dealType
   * @param newVar the new value of dealType
   */
  public void setDealType (DealTypes newVar) {
    dealType = newVar;
  }

  /**
   * Get the value of dealType
   * @return the value of dealType
   */
  public DealTypes getDealType () {
    return dealType;
  }

  /**
   * Set the value of object
   * @param newVar the new value of object
   */
  public void setObject (ObjectTypes newVar) {
    object = newVar;
  }

  /**
   * Get the value of object
   * @return the value of object
   */
  public ObjectTypes getObject () {
    return object;
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

  /**
   * Set the value of price
   * @param newVar the new value of price
   */
  public void setPrice (String newVar) {
    price = newVar;
  }

  /**
   * Get the value of price
   * @return the value of price
   */
  public String getPrice () {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Deal deal = (Deal) o;
    return getId() == deal.getId() &&
            getOwner() == deal.getOwner() &&
            getPerformer() == deal.getPerformer() &&
            Objects.equals(getName(), deal.getName()) &&
            Objects.equals(getDescription(), deal.getDescription()) &&
            Objects.equals(getAddress(), deal.getAddress()) &&
            Objects.equals(getRequests(), deal.getRequests()) &&
            getDealType() == deal.getDealType() &&
            getObject() == deal.getObject() &&
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
            getRequests(),
            getOwner(),
            getPerformer(),
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
            ", requests=" + requests +
            ", owner=" + owner +
            ", performer=" + performer +
            ", dealType=" + dealType +
            ", object=" + object +
            ", created_at=" + created_at +
            ", price='" + price + '\'' +
            '}';
  }

  //
  // Other methods
  //

}
