package com.lohika.jclub.hazelcast.main;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
  private String name;
  private Integer avg;

  public User() {

  }

  public User(String name, Integer avg) {
    this.name = name;
    this.avg = avg;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAvg() {
    return avg;
  }

  public void setAvg(Integer avg) {
    this.avg = avg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(name, user.name) &&
           Objects.equals(avg, user.avg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, avg);
  }

//  @Override
//  public void writeData(ObjectDataOutput out) throws IOException {
//    out.writeUTF(name);
//    out.writeInt(avg);
//  }
//
//  @Override
//  public void readData(ObjectDataInput in) throws IOException {
//    name = in.readUTF();
//    avg = in.readInt();
//  }

  @Override
  public String toString() {
    return "User{" +
           "name='" + name + '\'' +
           ", avg=" + avg +
           '}';
  }
}
