package com.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "dummy_confirm")
public class Dummy_Confirm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int id;
    @Column(name = "date")
    public String date;
    @Column(name = "email")
    public List<String> email;
    @Column(name = "building_id")
    public int building_id;
    @Column(name = "floor_id")
    public int floor_id;
    @Column(name = "workstation_id")
    public List<Integer> workstation_id;
    public List<String> ldates;
    public String Date_end;
    public List<String> changedDates;
	
    

}