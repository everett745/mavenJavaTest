package ru.sfedu.deals.lab3.mapSupClass;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@MappedSuperclass
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "OWNER", nullable = false)
  private String owner;
  @Column(name = "BALANCE", nullable = false)
  private BigDecimal balance;
  @Column(name = "INTEREST_RATE", nullable = false)
  private BigDecimal interestRate;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getOwner() {
    return owner;
  }
  public void setOwner(String owner) {
    this.owner = owner;
  }
  public BigDecimal getBalance() {
    return balance;
  }
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
  public BigDecimal getInterestRate() {
    return interestRate;
  }
  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(getId(), account.getId()) &&
            Objects.equals(getOwner(), account.getOwner()) &&
            Objects.equals(getBalance(), account.getBalance()) &&
            Objects.equals(getInterestRate(), account.getInterestRate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getOwner(), getBalance(), getInterestRate());
  }

  @Override
  public String toString() {
    return "Account{" +
            "id=" + id +
            ", owner='" + owner + '\'' +
            ", balance=" + balance +
            ", interestRate=" + interestRate +
            '}';
  }
}
