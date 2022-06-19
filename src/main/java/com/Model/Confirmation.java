package com.Model;

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
@Entity
@Table(name = "confirmation")
public class Confirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int id;
    @Column(name = "date")
    public String date;
    @Column(name = "email")
    public String email;
    @Column(name = "name")
    public String name;
    @Column(name = "building_id")
    public int building_id;
    @Column(name = "floor_id")
    public int floor_id;
    @Column(name = "workstation_id")
    public int workstation_id;

    @Column(name = "building_name")
    public String building_name;
    @Column(name = "floor_name")
    public String floor_name;
    @Column(name = "deleted")
    public String deleted="false";


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