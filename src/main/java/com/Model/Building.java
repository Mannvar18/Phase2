package com.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="building")
public class Building {

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="_id")
	public int _id;
	@Column(name="name")
	public String name;
	@Column(name="no_of_workstations")
	public int no_of_workstations;
    @Column(name="total_no_of_meeting_rooms")
	public int total_no_of_meeting_rooms;

	@Column(name="available_no_of_meeting_rooms")
	public int available_no_of_meeting_rooms;
	@Column(name = "total_no_of_workstations")
	public int total_no_of_workstations;
	@OneToMany(targetEntity=Floor.class,cascade=CascadeType.ALL)
	@JoinColumn(name="Building_id")
	public List<Floor> floors;
	@Column(name = "building_state")
	public String building_state;
//	@Column(name = "projects")
	public String projects;
	public String reason_for_disabling_building;

	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNo_of_workstations() {
		return no_of_workstations;
	}
	public void setNo_of_workstations(int no_of_workstations) {
		this.no_of_workstations = no_of_workstations;
	}


	@Override
	public String toString() {
		return "Building [_id=" + _id + ", name=" + name + ", no_of_workstations=" + no_of_workstations + "]";
	}

	

}

