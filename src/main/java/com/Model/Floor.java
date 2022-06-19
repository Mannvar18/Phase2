package com.Model;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name="floor")
public @Data class Floor {
    @Id
   // @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Floor_no")
    public int Floor_no;
    @Column(name="no_of_workstations")
	public int no_of_workstations;
	@Column(name="total_no_of_meeting_rooms")
	public int total_no_of_meeting_rooms;

	@Column(name="reason_for_disabling")
	public String reason_for_disabling;
	@Column(name="available_no_of_meeting_rooms")
    public int available_no_of_meeting_rooms;

	@Column(name = "floor_name")
	public String floor_name;
	@Column(name = "total_no_of_workstations")
	public int total_no_of_workstations;
	@Column(name = "floor_state")
	public String floor_state;
    @OneToMany(targetEntity=Workstation.class,cascade=CascadeType.ALL)
   @JoinColumn(name="workstations_on_floor")
    public List<Workstation> workstations;
	public String projects;

	@OneToMany(targetEntity = meeting.class,cascade=CascadeType.ALL)
	@JoinColumn(name="rooms_on_floor")
	public List<meeting> meetings;
    
    public int getFloor_no() {
		return Floor_no;
	}
	public void setFloor_no(int floor_no) {
		Floor_no = floor_no;
	}
	public int getNo_of_workstations() {
		return no_of_workstations;
	}
	public void setNo_of_workstations(int no_of_workstations) {
		this.no_of_workstations = no_of_workstations;
	}


}
