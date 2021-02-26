package ru.sfedu.deals.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class AddressT {
  @Column(name = "CITY", nullable = false)
  private String city;
  @Column(name = "DISTRICT", nullable = false)
  private String district;
  @Column(name = "REGION", nullable = false)
  private String region;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddressT addressT = (AddressT) o;
    return Objects.equals(getCity(), addressT.getCity()) &&
            Objects.equals(getDistrict(), addressT.getDistrict()) &&
            Objects.equals(getRegion(), addressT.getRegion());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCity(), getDistrict(), getRegion());
  }

  @Override
  public String toString() {
    return "AddressT{" +
            "city='" + city + '\'' +
            ", district='" + district + '\'' +
            ", region='" + region + '\'' +
            '}';
  }
};
