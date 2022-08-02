package entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Contact {

	@Id
	int contactId;
	
	Long phone;
	Long pin;
	int id;
	public Contact(int contactId, Long phone, Long pin, int id) {
		super();
		this.contactId = contactId;
		this.phone = phone;
		this.pin = pin;
		this.id = id;
	}
	public Contact() {
		super();
	}

	
	
}
