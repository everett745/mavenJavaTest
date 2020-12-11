package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.maven1.dataConvertors.UUIDArrayConvertor;
import ru.sfedu.maven1.dataConvertors.UUIDConvertor;

import java.util.*;

/**
 * Class Queue
 */
public class Queue {

  //
  // Fields
  //

  @CsvCustomBindByName(converter = UUIDConvertor.class)
  private UUID id = UUID.randomUUID();
  @CsvCustomBindByName(converter = UUIDArrayConvertor.class)
  private List<UUID> items = new ArrayList<UUID>();

  //
  // Constructors
  //
  public Queue () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

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

  //
  // Other methods
  //

}
