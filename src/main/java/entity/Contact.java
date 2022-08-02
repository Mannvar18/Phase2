package entity;

public class Contact {
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
