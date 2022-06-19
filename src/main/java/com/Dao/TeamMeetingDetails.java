package com.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TeamMeetingDetails {
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

    public String buildingName;
    public String floorName;

    public String projectName;

}
