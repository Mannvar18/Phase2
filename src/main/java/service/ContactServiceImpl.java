package service;

import java.util.List;

import org.springframework.stereotype.Service;

import entity.Contact;
import repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {
	private final ContactRepository contactRepository;
	
	
public ContactServiceImpl(ContactRepository contactRepository) {
		super();
		this.contactRepository = contactRepository;
	}


@Override
public List<Contact> getContacts(int id){
	return contactRepository.findByContactId(id);
}

}
