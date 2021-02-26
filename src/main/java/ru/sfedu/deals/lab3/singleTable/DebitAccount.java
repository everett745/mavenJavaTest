package ru.sfedu.deals.lab3.singleTable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue( "Debit" )
public class DebitAccount extends Account {
  @Column(name = "overdraftFee")
  private BigDecimal overdraftFee;

  public BigDecimal getOverdraftFee() {
    return overdraftFee;
  }
  public void setOverdraftFee(BigDecimal overdraftFee) {
    this.overdraftFee = overdraftFee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    DebitAccount that = (DebitAccount) o;
    return Objects.equals(getOverdraftFee(), that.getOverdraftFee());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getOverdraftFee());
  }

  @Override
  public String toString() {
    return "DebitAccount{" +
            "id=" + super.getId() +
            "owner=" + super.getOwner() +
            "balance=" + super.getBalance() +
            "interestRate=" + super.getInterestRate() +
            "overdraftFee=" + overdraftFee +
            '}';
  }
}
