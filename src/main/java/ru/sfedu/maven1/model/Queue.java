package ru.sfedu.maven1.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.maven1.dataConvertors.UUIDConvertor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Class Queue
 */
public class Queue {

  //
  // Fields
  //

  @CsvCustomBindByName(converter = UUIDConvertor.class)
  private UUID id = UUID.randomUUID();
  @CsvBindByName
  private long[] items = new long[0];

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
  public void setItems (long[] newVar) {
    items = newVar;
  }

  /**
   * Get the value of items
   * @return the value of items
   */
  public long[] getItems () {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Queue queue = (Queue) o;
    return getId() == queue.getId() &&
            Arrays.equals(getItems(), queue.getItems());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getId());
    result = 31 * result + Arrays.hashCode(getItems());
    return result;
  }

  @Override
  public String toString() {
    return "Queue{" +
            "id=" + id +
            ", items=" + Arrays.toString(items) +
            '}';
  }

  //
  // Other methods
  //

}
