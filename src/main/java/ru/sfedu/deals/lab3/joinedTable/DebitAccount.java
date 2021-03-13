package ru.sfedu.deals.lab3.joinedTable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "DebitAccount")
public class DebitAccount extends Account {
  @Column(name = "overdraftFee", nullable = false)
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
