package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import ru.sfedu.maven1.dataConvertors.UUIDListConvertor;
import ru.sfedu.maven1.dataConvertors.UUIDConvertor;

import java.util.*;
import java.util.stream.Collectors;

@Root(name = "Queue")
public class Queue {

  @CsvCustomBindByName(converter = UUIDConvertor.class)
  private UUID id = UUID.randomUUID();

  @CsvCustomBindByName(converter = UUIDListConvertor.class)
  private List<UUID> items = new ArrayList<UUID>();

  public Queue () { };

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
  public void setIdXml (String newVar) {
    id = UUID.fromString(newVar);
  }

  @Attribute(name = "id")
  public String getIdXml () {
    return id.toString();
  }

  /**
   * Set the value of items
   * @param newVar the new value of items
   */
  public void setItems (List<UUID> newVar) {
    items = newVar;
  }

  /**
   * Get the value of items
   * @return the value of items
   */
  public List<UUID> getItems () {
    return items;
  }

  @ElementList(name = "items")
  private void setItemsXml (List<String> newVar) {
    items = newVar.stream().map(UUID::fromString).collect(Collectors.toList());
  }

  @ElementList(name = "items")
  private List<String> getItemsXml () {
    return items.stream().map(UUID::toString).collect(Collectors.toList());
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
