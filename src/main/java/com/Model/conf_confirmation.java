package com.Model;

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
@Entity
public class conf_confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int id;
    @Column(name = "date")
    public String start_date;
    @Column(name="end_date")
    public String end_date;
    @Column(name="start_time")
    public String start_time;
    @Column(name="end_time")
    public String end_time;
    @Column(name = "email")
    public String email;
    @Column(name = "name")
    public String name;
    @Column(name = "building_id")
    public int building_id;
    @Column(name = "floor_id")
    public int floor_id;
    @Column(name= "conf_room")
    public int conf_room;
    public String bookedBy;
    public String project_name="NA";
    @Column(name = "reason_for_booking")
    public String reason_for_booking;
    @Column(name = "deleted")
    public String deleted="false";

    public conf_confirmation(int i, String s, String s1, String s2, String s3, String s4, String nithya, int i1, int i2, int i3, String s5, String automated_workstation, String reason) {
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
