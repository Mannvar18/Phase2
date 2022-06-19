package com.Controller;


import com.Dao.Page;
import com.Dao.TeamMeetingDetails;
import com.Model.AuthRequest;
import com.Model.Booking;
import com.Model.Building;
//import com.Model.Email;
import com.Model.Confirmation;
import com.Model.Dummy_Confirm;

import com.Model.User;
import com.Model.conf_confirmation;
import com.Repository.ConfirmationRepo;
import com.Repository.EmailRepository;

import com.Repository.conf_confirmationrepo;
import com.Service.ServiceInterface;
import com.Util.jwtUtil;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    public ConfirmationRepo confirmationRepo;

    @Autowired
    public conf_confirmationrepo conf_confirmationrepo;
    
    @Autowired
    private jwtUtil jwtutil;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailRepository emailrepo;

//    List<Booking> list = new ArrayList<>();{
//    Booking book = new Booking(1,21-02-2022,"anmol.mittal@nineleaps.com","nineleaps",2,1,25);
//    list.add(book);}

//
//    @GetMapping("/")
//    public String hello(){
//        return "Hello User";
//    }
//    @GetMapping("/redirect")
//    public String login(){
//        return "Welcome User";
//    }

 

    @Autowired
    public ServiceInterface emailService;


@GetMapping("/find_projects_name") 
    public ResponseEntity<List<String>> find_projects(){
        return ResponseEntity.ok().body(emailService.find_projects_id());
    }
@GetMapping("/email_get_role_new")
    public ResponseEntity<String> getRole(){
       return emailService.findAllnew();
}
@GetMapping("/find_project_members")
    public ResponseEntity<List<User> >get_members(@RequestParam("project-name") String project_name){
        return ResponseEntity.ok().body(emailService.find_members(project_name));
    }

@GetMapping("/employee") //employee/Automed Workstation,2022-03-23
    public List<User> getEmployeeName(@RequestParam String project_name,@RequestParam String sdate,@RequestParam String edate) throws ParseException {
    return emailService.findEmployee(project_name,sdate,edate);

    }
@PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authrequest)throws Exception{
        try{
            //System.out.println(authrequest.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getEmail(),authrequest.getPassword()));
        
        }
        catch(Exception ex){
            return new ResponseEntity<String >("invalid user", HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<String>(jwtutil.generateToken(authrequest.getEmail()),HttpStatus.OK);
    }


   
   
@GetMapping(value="/Pjdata")
public List<Page> getPjData(@RequestParam(name="project_name",required=false) String p2,@RequestParam(name="start_date",required=false) String p3,@RequestParam(name="end_date",required=false) String p4,@RequestParam(name="offset",required=false) Integer o,@RequestParam(name="size",required=false) Integer s){
	if(o!=null && s!=null) {
	return  emailService.getTeamDetails( p2, p3, p4,o,s);
	}
	else {
		return  emailService.getTeamDetailsUpdated( p2, p3, p4);
	}
}
	

    @GetMapping(value="/userdata")
	public List<Page> getUserData(@RequestParam("email") String email, @RequestParam(name="start_date",required=false) String sdate,@RequestParam(name="end_date",required=false) String edate,@RequestParam(name="offset",required=false) Integer a,@RequestParam(name="size",required=false) Integer b) {
		
		
		return emailService.getUserDetails(email,sdate,edate,a,b);
		
	}



//    @GetMapping(value = "/roomdata")
//    public List<conf_confirmation> getData(@RequestParam String email){
//    return emailService.getDetails(email);
//    }

    @GetMapping("/all_rooms")
    public List<conf_confirmation> getAllData(@RequestParam(name="start_date",required=false) String sdate,@RequestParam(name="end_date",required=false) String edate){
    return conf_confirmationrepo.findAllByDate(sdate,edate);
    }

// @PostMapping(value="v1/meeting-room-details-dateAndTime")
// public  ResponseEntity<Object>addMeetingroomWithDateandTime(@RequestBody conf_confirmation c){
// return emailService.saveDateTime(c);



// }


// @PostMapping(value="v1/meeting-room-booking-details-multipleDays-withtime")
// public ResponseEntity<Object>addMeetingroomforMultipleDaysandTime(@RequestParam("end_date") String edate,@RequestBody conf_confirmation c )
// {
//     return emailService.saveMultipledaysTime(edate,c);

// }
// @GetMapping(value="meeting-room-for-single-day")
// public List<Building> getMeetingroomForSingleday(@RequestParam("date") String date)
// {
//     return emailService.getsingleDayDetails(date);
// }

// @GetMapping(value="meeting-room-for-singe-day-with-time")
// public List<Building>getsingleDayDetailswithTime(@RequestParam("date") String date,@RequestParam("start_time")String stime,@RequestParam("end_time")String etime)
// {
//     return emailService.getsingleDayDetailswithTime(date,stime,etime);
// }


@GetMapping("/confirm_data")
public List<Page> showData(@RequestParam(name="start_date",required=false) String sdate,@RequestParam(name="end_date",required=false) String edate,@RequestParam(name="offset",required=false) Integer o,@RequestParam(name="size",required=false) Integer s)
{
 if(o!=null && s!=null) {
	return emailService.data(sdate,edate,o,s);
 }
 else {
	 return emailService.data(sdate,edate);
 }

}

@GetMapping("/on_dateWorkstation")
    public List<Page> onDateData(@RequestParam(name= "date") String date){
    return emailService.onDate(date);
}
//    @GetMapping("/on_dateRoom")
//    public List<conf_confirmation> roomDateData(@RequestParam(name= "date") String date){
//        return emailService.roomDate(date);
//    }

}