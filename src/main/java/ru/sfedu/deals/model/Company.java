package ru.sfedu.deals.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import ru.sfedu.deals.dataConvertors.DealListConvertor;
import ru.sfedu.deals.dataConvertors.UsersListConvertor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "COMPANY", schema = "PUBLIC", catalog = "DEALSDB")
@Root(name = "Company")
public class Company {

  @CsvBindByName
  private String id;

  @CsvCustomBindByName(converter = UsersListConvertor.class)
  private List<User> employees = new ArrayList<>();

  @CsvCustomBindByName(converter = DealListConvertor.class)
  private List<Deal> deals = new ArrayList<>();

  public Company() {
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
  @Basic
  @Column(name = "ID", nullable = true, length = 255)
  @Attribute(name = "id")
  public String getId() {
    return id;
  }

  /**
   * Set the value of employees
   *
   * @param newVar the new value of employees
   */
  @ElementList(name = "employees")
  public void setEmployees(List<User> newVar) {
    employees = newVar;
  }

  /**
   * Get the value of employees
   *
   * @return the value of employees
   */
  @Basic
  @Column(name = "EMPLOYEES", nullable = true, length = -1)
  @ElementList(name = "employees")
  public List<User> getEmployees() {
    return employees;
  }

  /**
   * Set the value of deals
   *
   * @param newVar the new value of deals
   */
  @ElementList(name = "deals")
  public void setDeals(List<Deal> newVar) {
    deals = newVar;
  }

  /**
   * Get the value of deals
   *
   * @return the value of deals
   */
  @Basic
  @Column(name = "DEALS", nullable = true, length = -1)
  @ElementList(name = "deals")
  public List<Deal> getDeals() {
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
