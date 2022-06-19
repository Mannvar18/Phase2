package com.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Page")
public class Page {

    public int id;
 
    public String date;
   
    public String email;

    public String name;
 
    public int building_id;

    public int floor_id;
  
    public int workstation_id;


    public String building_name;

    public String floor_name;

    public String deleted="false";
    public int totalrecords;
    // @Override
    // public String toString() {
    //     return "Confirmation{" +
    //             "id=" + id +
    //             ", date=" + date +
    //             ", email='" + email + '\'' +
    //             ", building_id=" + building_id +
    //             ", floor_id=" + floor_id +
    //             ", workstation_id=" + workstation_id +
    //             '}';
    //  }
}