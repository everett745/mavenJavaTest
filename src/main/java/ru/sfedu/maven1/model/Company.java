package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import ru.sfedu.maven1.dataConvertors.DealListConvertor;
import ru.sfedu.maven1.dataConvertors.UUIDConvertor;
import ru.sfedu.maven1.dataConvertors.UsersListConvertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Root(name = "Company")
public class Company {

  @CsvCustomBindByName(converter = UUIDConvertor.class)
  private UUID id;

  @CsvCustomBindByName(converter = UsersListConvertor.class)
  private List<User> employees = new ArrayList<User>();

  @CsvCustomBindByName(converter = DealListConvertor.class)
  private List<Deal> deals  = new ArrayList<Deal>();

  public Company () { };

  /**
   * Set the value of id
   * @param newVar the new value of id
   */
  public void setId (UUID newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   * @return the value of id
   */
  public UUID getId () {
    return id;
  }

  @Attribute(name = "id")
  private void setIdXml (String newVar) {
    id = UUID.fromString(newVar);
  }

  @Attribute(name = "id")
  private String getIdXml () {
    return id.toString();
  }

  /**
   * Set the value of employees
   * @param newVar the new value of employees
   */
  @ElementList(name = "employees")
  public void setEmployees (List<User> newVar) {
    employees = newVar;
  }

  /**
   * Get the value of employees
   * @return the value of employees
   */
  @ElementList(name = "employees")
  public List<User> getEmployees () {
    return employees;
  }

  /**
   * Set the value of deals
   * @param newVar the new value of deals
   */
  @ElementList(name = "deals")
  public void setDeals (List<Deal> newVar) {
    deals = newVar;
  }

  /**
   * Get the value of deals
   * @return the value of deals
   */
  @ElementList(name = "deals")
  public List<Deal> getDeals () {
    return deals;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Company company = (Company) o;
    return Objects.equals(getId(), company.getId()) &&
            Objects.equals(getEmployees(), company.getEmployees()) &&
            Objects.equals(getDeals(), company.getDeals());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getEmployees(), getDeals());
  }

  @Override
  public String toString() {
    return "Company{" +
            "id=" + id +
            ", employees=" + employees +
            ", deals=" + deals +
            '}';
  }
}
