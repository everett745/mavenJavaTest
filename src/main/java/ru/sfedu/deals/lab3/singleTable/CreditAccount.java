package ru.sfedu.deals.lab3.singleTable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue( "Credit" )
public class CreditAccount extends Account {
  @Column(name = "creditLimit")
  private BigDecimal creditLimit;

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CreditAccount that = (CreditAccount) o;
    return Objects.equals(getCreditLimit(), that.getCreditLimit());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getCreditLimit());
  }

  @Override
  public String toString() {
    return "CreditAccount{" +
            "id=" + super.getId() +
            "owner=" + super.getOwner() +
            "balance=" + super.getBalance() +
            "interestRate=" + super.getInterestRate() +
            "creditLimit=" + creditLimit +
            '}';
  }
}
