package com.Controller;

import java.util.List;

import com.Model.Building;
import com.Model.Confirmation;
import com.Model.Dummy_Confirm;
import com.Model.Floor;
import com.Model.User;
import com.Model.Workstation;
import com.Repository.Buildrepo;
import com.Repository.ConfirmationRepo;
import com.Repository.EmailRepository;
import com.Repository.Floorrepo;
import com.Repository.Workstationrepo;
import com.Service.ServiceImpl;
import com.Service.ServiceInterface;
import com.Service.userDetailsImpl;
import com.exception.AutoworkExceptions;
import com.exception.BackendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WorkstationController {

	@Autowired
	Buildrepo brepo;
	@Autowired
	Floorrepo frepo;
	@Autowired
	Workstationrepo repo;
	@Autowired
	ConfirmationRepo crepo;
	@Autowired
	EmailRepository erepo;

	@Autowired
	ServiceImpl eser;
	
  @Autowired
  ServiceInterface s;

@PutMapping("/Edit")
public ResponseEntity<String> editDetails(@RequestBody Dummy_Confirm conf) {
	s.edit(conf);
	return new ResponseEntity<String>("Workstations updated successfuly",HttpStatus.OK);
    }
	@PutMapping("/Edit2")
	public void editDetails2(@RequestBody Dummy_Confirm conf) {
		eser.edit2(conf);
	}

    @PostMapping("/multiple_confirmation")
    public ResponseEntity<Object> addMultiBooking(@RequestBody Dummy_Confirm dummy_conf )
    {
    // s.confirm(dummy_conf);
     //new ResponseEntity<Object>("Workstations booked successfully",HttpStatus.OK);
	   return  s.confirm(dummy_conf);
    }


	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteBooking(@RequestBody Dummy_Confirm conf)
	{
		return eser.removeBooking(conf);
	}

    @PutMapping("/Disablestate")
	public ResponseEntity<?> DisableWorkstations(@RequestBody List<Integer> l) {
	 return	eser.Disable(l);
		}
	   
	

@ApiOperation("Enabling the workstations by HR")
@PutMapping("/Enablestate")
	public ResponseEntity<?> EnableWorkstations(@RequestBody List<Integer> li ) {

		return eser.Enable(li);
}

@DeleteMapping("/TeamBooking")
public ResponseEntity<Object> deleteBulkBooking(@RequestBody Dummy_Confirm conf )
 {
      return eser.removeBulkBooking(conf);
 }



 @PutMapping(value="overriding/workstation")
 public ResponseEntity<String> changeBookingDetails(@RequestBody Dummy_Confirm c)
 {
   return  eser.updateWorkstationBooking(c);
    //  return new ResponseEntity<String>("new booking made",HttpStatus.OK);
 }


}