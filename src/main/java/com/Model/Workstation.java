package com.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
@Table(name="workstation")
public  class Workstation {

	@Id
	@Column(name="number")
	public int number;
   
	public transient boolean state;
	@Column( name = "workstation_state")
	public String workstation_state;
	@Column(name="reason_for_disabling")
	public String reason_for_disabling;

	public boolean isState() {
		return state;
	}
}