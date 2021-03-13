package ru.sfedu.deals.lab5;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "company")
public class Company implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMPANY_ID", unique = true, nullable = false)
  private String id;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable (name="deal_company",
          joinColumns=@JoinColumn (name="company_id"),
          inverseJoinColumns=@JoinColumn(name="deal_id"))
  private List<Deal> deals;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable (name="employee_company",
          joinColumns=@JoinColumn (name="company_id"),
          inverseJoinColumns=@JoinColumn(name="user_id"))
  private List<User> employees;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Deal> getDeals() {
    return deals;
  }

  public void setDeals(List<Deal> deals) {
    this.deals = deals;
  }

  public List<User> getEmployees() {
    return employees;
  }

  public void setEmployees(List<User> users) {
    this.employees = users;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Company company = (Company) o;
    return Objects.equals(getId(), company.getId()) &&
            Objects.equals(getDeals(), company.getDeals()) &&
            Objects.equals(getEmployees(), company.getEmployees());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getDeals(), getEmployees());
  }


  @Override
  public String toString() {
    return "Company{" +
            "id='" + id + '\'' +
            ", deals=" + deals +
            ", employees=" + employees +
            '}';
  }
}
