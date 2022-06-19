package com.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "name")
    public  String name;
    @Column(name = "email")
    public String email;
    @Column(name = "role")
    public String role;
    @Column(name="password")
    private String password;
    @Column(name = "project_name")
    public String project_name;
   
    public boolean isbooked;
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }



    public void setRole(String role) {
        this.role = role;
    }

	public User(int id, String name, String email, String role, String password, String project_name,
			boolean isbooked) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.password = password;
		this.project_name = project_name;
		this.isbooked = isbooked;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public boolean isIsbooked() {
		return isbooked;
	}

	public void setIsbooked(boolean isbooked) {
		this.isbooked = isbooked;
	}

  
}
