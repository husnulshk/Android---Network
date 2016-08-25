package com.unpam.model; 
 
import java.util.List; 
 
import org.simpleframework.xml.Element; 
import org.simpleframework.xml.ElementList; 
import org.simpleframework.xml.Root; 
 
@Root 
public class Result { 
  @Element 
  private int count;
  @ElementList 
  private List<Restaurant> data; 
 
  public int getCount() { 
    return count; 
  } 
 
  public List<Restaurant> getData() { 
    return data; 
  }   
   
}