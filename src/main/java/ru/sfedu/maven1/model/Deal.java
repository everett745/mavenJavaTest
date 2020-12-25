package ru.sfedu.maven1.model;

import com.opencsv.bean.*;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.dataConvertors.AddressConvertor;
import ru.sfedu.maven1.dataConvertors.QueueConvertor;
import ru.sfedu.maven1.enums.DealModel;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class Deal
 */
public class Deal implements Serializable {

  @CsvBindByName
  private String id;

  @CsvBindByName
  private String name;

  @CsvBindByName
  private String description;

  @CsvCustomBindByName(converter = AddressConvertor.class)
  private Address address;

  @CsvCustomBindByName(converter = QueueConvertor.class)
  private Queue requests;

  @CsvBindByName
  private String owner;

  @CsvBindByName
  private String performer;

  @CsvBindByName
  private DealTypes dealType;

  @CsvBindByName
  private ObjectTypes object;

  @CsvBindByName
  private DealModel dealModel;

  @CsvDate(value = Constants.DATE_FORMAT)
  private Date created_at;

  @CsvBindByName
  private String price;

  public Deal() {
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
   * Set the value of description
   *
   * @param newVar the new value of description
   */
  @Attribute(name = "description")
  public void setDescription(String newVar) {
    description = newVar;
  }

  /**
   * Get the value of description
   *
   * @return the value of description
   */
  @Attribute(name = "description")
  public String getDescription() {
    return description;
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
   * Set the value of requests
   *
   * @param newVar the new value of requests
   */
  @Element(name = "requests")
  public void setRequests(Queue newVar) {
    requests = newVar;
  }

  /**
   * Get the value of requests
   *
   * @return the value of requests
   */
  @Element(name = "requests")
  public Queue getRequests() {
    return requests;
  }

  /**
   * Set the value of owner
   *
   * @param newVar the new value of owner
   */
  @Attribute(name = "owner")
  public void setOwner(String newVar) {
    owner = newVar;
  }

  /**
   * Get the value of owner
   *
   * @return the value of owner
   */
  @Attribute(name = "owner")
  public String getOwner() {
    return owner;
  }

  /**
   * Set the value of performer
   *
   * @param newVar the new value of performer
   */
  @Attribute(name = "performer", required = false)
  public void setPerformer(String newVar) {
    performer = newVar;
  }

  /**
   * Get the value of performer
   *
   * @return the value of performer
   */
  @Attribute(name = "performer", required = false)
  public String getPerformer() {
    return performer;
  }

  /**
   * Set the value of dealType
   *
   * @param newVar the new value of dealType
   */
  @Attribute(name = "dealType")
  public void setDealType(DealTypes newVar) {
    dealType = newVar;
  }

  /**
   * Get the value of dealType
   *
   * @return the value of dealType
   */
  @Attribute(name = "dealType")
  public DealTypes getDealType() {
    return dealType;
  }

  /**
   * Set the value of object
   *
   * @param newVar the new value of object
   */
  @Attribute(name = "object")
  public void setObject(ObjectTypes newVar) {
    object = newVar;
  }

  /**
   * Get the value of object
   *
   * @return the value of object
   */
  @Attribute(name = "object")
  public ObjectTypes getObject() {
    return object;
  }

  /**
   * Get the value of dealModel
   *
   * @return the value of dealModel
   */
  @Attribute(name = "model")
  public DealModel getDealModel() {
    return dealModel;
  }

  /**
   * Set the value of dealModel
   *
   * @param dealModel the new value of DealModel
   */
  @Attribute(name = "model")
  public void setDealModel(DealModel dealModel) {
    this.dealModel = dealModel;
  }

  /**
   * Set the value of created_at
   *
   * @param newVar the new value of created_at
   */
  @Attribute(name = "createdAt")
  public void setCreated_at(Date newVar) {
    created_at = newVar;
  }

  /**
   * Get the value of created_at
   *
   * @return the value of created_at
   */
  @Attribute(name = "createdAt")
  public Date getCreated_at() {
    return created_at;
  }

  /**
   * Set the value of price
   *
   * @param newVar the new value of price
   */
  @Attribute(name = "price")
  public void setPrice(String newVar) {
    price = newVar;
  }

  /**
   * Get the value of price
   *
   * @return the value of price
   */
  @Attribute(name = "price")
  public String getPrice() {
    return price;
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
            Objects.equals(getRequests(), deal.getRequests()) &&
            Objects.equals(getOwner(), deal.getOwner()) &&
            Objects.equals(getPerformer(), deal.getPerformer()) &&
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
            ", model=" + dealModel +
            ", created_at=" + created_at +
            ", price='" + price + '\'' +
            '}';
  }
}
