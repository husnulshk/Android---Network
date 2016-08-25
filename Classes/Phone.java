package com.unpam.model; 
 
import org.simpleframework.xml.Attribute; 
import org.simpleframework.xml.Text; 
 
public class Phone { 
  @Attribute 
  private String ext; 
   
  @Text 
  private String text; 
 
  public String getExt() { 
    return ext; 
  } 
 
  public String getText() { 
    return text; 
  } 
  }
  