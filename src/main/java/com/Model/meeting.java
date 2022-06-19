package com.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class meeting {
    

    @Id
	private int number;
 
	public transient boolean state;

	private String room_state;
    
	private String name;
	 
	private int capacity;
	public boolean isState() {
		return state;
	}

	private String reason_for_booking;

	private String reason_for_disabling;

	public boolean getState() {
		return state;
	}

	public meeting(int number, boolean state, String room_state, String name, int capacity, String reason_for_booking,
			String reason_for_disabling) {
		super();
		this.number = number;
		this.state = state;
		this.room_state = room_state;
		this.name = name;
		this.capacity = capacity;
		this.reason_for_booking = reason_for_booking;
		this.reason_for_disabling = reason_for_disabling;
	}

	
	
}
