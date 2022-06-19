package com.Dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pagecount {
  
    public int id;
  
    public String start_date;
 
    public String end_date;

    public String start_time;
   
    public String end_time;
 
    public String email;
   
    public String name;

    public int building_id;
  
    public int floor_id;
   
    public int conf_room;
    public String bookedBy;
    public String project_name="NA";

    public String reason_for_booking;

    public String deleted="false";
    
    public int totalrecords;
    public String buildingName;
    public String floorName;
 
}
