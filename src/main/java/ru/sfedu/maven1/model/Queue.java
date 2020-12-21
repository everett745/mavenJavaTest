package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import ru.sfedu.maven1.dataConvertors.UUIDListConvertor;

import java.util.*;

@Root(name = "Queue")
public class Queue {

  @CsvBindByName
  private String id = UUID.randomUUID().toString();

  @CsvCustomBindByName(converter = UUIDListConvertor.class)
  private List<String> items = new ArrayList<>();

  public Queue () { };

  /**
   * Set the value of id
   * @param newVar the new value of id
   */
  @Attribute(name = "id")
  public void setId (String newVar) {
    id = newVar;
  }

  /**
   * Get the value of id
   * @return the value of id
   */
  @Attribute(name = "id")
  public String getId () {
    return id;
  }

  /**
   * Set the value of items
   * @param newVar the new value of items
   */
  @ElementList(name = "items")
  public void setItems (List<String> newVar) {
    items = newVar;
  }

  /**
   * Get the value of items
   * @return the value of items
   */
  @ElementList(name = "items")
  public List<String> getItems () {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Queue queue = (Queue) o;
    return getId() == queue.getId() &&
            List.of(getItems()).equals(queue.getItems());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getId());
    result = 31 * result + List.of(getItems()).hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Queue{" +
            "id=" + id +
            ", items=" + items.toString() +
            '}';
  }

}
