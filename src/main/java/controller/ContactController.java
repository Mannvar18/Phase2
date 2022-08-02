package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.Contact;
import service.ContactService;

@RestController
@RequestMapping("/contact")
public class ContactController {
	
	private final ContactService contactService;

	@Autowired
	public ContactController(ContactService contactService) {
		super();
		this.contactService = contactService;
	}
	
	@GetMapping("/user/{userId}")
	public List<Contact> getContacts(@PathVariable("userId") int id){
		return contactService.getContacts(id);
	}

}
