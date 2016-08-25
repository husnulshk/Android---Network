package com.unpam.model; 
 
import org.simpleframework.xml.Element; 
 
public class Restaurant { 
  @Element 
  private int id; 
   
  @Element 
  private String name; 
 
  @Element 
  private String address; 
 
  @Element 
  private Phone phone; 
 
  public int getId() { 
    return id; 
  } 
 
  public String getName() { 
    return name; 
  } 
 
  public String getAddress() { 
    return address; 
  } 
 
  public Phone getPhone() { 
    return phone; 
  } 
   
}