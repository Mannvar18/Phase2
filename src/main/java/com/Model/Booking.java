package com.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data

@NoArgsConstructor
@ToString
@Table(name = "confirmation")
public class Booking {

    @Id
    @Column(name = "id")
    public int id;

    @Column(name = "date")
    public int date;

    @Column(name = "email")
    public String email;

   
    @Column(name = "bulding_id")
    public int bulding_id;

    @Column(name = "floor_id")
    public int floor_id;

    @Column(name = "workstation_id")
    public int workstation_id;

    public Booking(int id, int date, String email, String password, int bulding_id, int floor_id, int workstation_id) {
        this.id = id;
        this.date = date;
        this.email = email;
        
        this.bulding_id = bulding_id;
        this.floor_id = floor_id;
        this.workstation_id = workstation_id;
    }
//    @Enumerated(EnumType.STRING)
//    @Column(name = "auth_provider")
//    public AuthenticationProvider auth_provider;

}