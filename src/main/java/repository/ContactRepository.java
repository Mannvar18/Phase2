package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import entity.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact,Integer> {

	@Query("select c from Contact where id=:uid")
	List<Contact> findByContactId(@Param("uid") int id);
}
