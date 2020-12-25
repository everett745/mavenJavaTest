package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import ru.sfedu.maven1.dataConvertors.DealHistoryListConvertor;
import ru.sfedu.maven1.enums.DealStatus;

import java.util.List;
import java.util.Objects;

/**
 * Class PublicDeal
 */
public class PublicDeal extends Deal {

  @CsvBindByName
  private DealStatus currentStatus;

  @CsvCustomBindByName(converter = DealHistoryListConvertor.class)
  private List<DealHistory> history;

  public PublicDeal() {
  }

  /**
   * Set the value of history
   *
   * @param newVar the new value of history
   */
  @ElementList(name = "history")
  public void setHistory(List<DealHistory> newVar) {
    history = newVar;
  }

  /**
   * Get the value of history
   *
   * @return the value of history
   */
  @ElementList(name = "history")
  public List<DealHistory> getHistory() {
    return history;
  }

  /**
   * Get the value of currentStatus
   *
   * @return the value of currentStatus
   */
  @Attribute(name = "currentStatus")
  public DealStatus getCurrentStatus() {
    return currentStatus;
  }

  /**
   * Set the value of history
   *
   * @param currentStatus the new value of currentStatus
   */
  @Attribute(name = "currentStatus")
  public void setCurrentStatus(DealStatus currentStatus) {
    this.currentStatus = currentStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    PublicDeal that = (PublicDeal) o;
    return currentStatus == that.currentStatus &&
            Objects.equals(getHistory(), that.getHistory());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), currentStatus, getHistory());
  }

  @Override
  public String toString() {
    return "PublicDeal{" +
            "id=" + this.getId() +
            ", name='" + this.getName() + '\'' +
            ", description='" + this.getDescription() + '\'' +
            ", address=" + this.getAddress() +
            ", currentStatus=" + currentStatus +
            ", history=" + history +
            ", requests=" + this.getRequests() +
            ", owner=" + this.getOwner() +
            ", performer=" + this.getPerformer() +
            ", dealType=" + this.getDealType() +
            ", object=" + this.getObject() +
            ", model=" + this.getDealModel() +
            ", created_at=" + this.getCreated_at() +
            ", price='" + this.getPrice() + '\'' +
            '}';
  }

}
