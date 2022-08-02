package entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	int id;
	
	String name;
	List<Contact> list=new ArrayList<>();
	
	public User(int id, String name, List<Contact> list) {
		super();
		this.id = id;
		this.name = name;
		this.list = list;
	}

	public User(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public User() {
		super();
	}
	
	
	 
	 
}
