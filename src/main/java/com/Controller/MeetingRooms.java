package com.Controller;

import java.util.List;

import com.Dao.Pagecount;
import com.Dao.TeamMeetingDetails;
import com.Model.Building;
import com.Model.conf_confirmation;
import com.Repository.conf_confirmationrepo;
import com.Service.ServiceInterface;
import com.exception.BackendException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MeetingRooms {

@Autowired
public ServiceInterface emailService;

@Autowired
private conf_confirmationrepo cnfrepo;

@PostMapping(value="v1/meeting-room-for-days")
 public  ResponseEntity<Object>addMeetingroom(@RequestBody conf_confirmation conf)
{
    return emailService.saveDetails(conf);


}
@PostMapping(value="v1/meeting-room-with-time")
public ResponseEntity<Object>addMeetingroomforSingleday(@RequestBody conf_confirmation c)
{
    return emailService.saveSingleDay(c);
}
@GetMapping(value="booked-details")
public conf_confirmation getbookedDetails(@RequestParam("start_date") String sdate,@RequestParam("end_date") String edate ,@RequestParam("start_time") String stime,@RequestParam("end_time")String etime,@RequestParam("meeting_room")int mid)
{
 return emailService.getbookedDetails(sdate,edate,stime,etime,mid);
}

 @GetMapping(value="booked-details-days")
 public List<conf_confirmation> getBookeddetailsforDays(@RequestParam("start_date") String sdate,@RequestParam("end_date") String edate,@RequestParam("meeting_room") int mid)
 {
     return emailService.getBookedDetailforDays(sdate,edate,mid);
 }


 @PutMapping(value="change-details")
 public void changeBookingDetails(@RequestParam("start_date") String sdate,@RequestParam("end_date") String edate ,@RequestParam("start_time") String stime,@RequestParam("end_time")String etime,@RequestParam("meeting_room")int mid,@RequestParam("email") String email,@RequestParam("reason") String reason,@RequestParam("project_name") String pro)
 {
     emailService.updatebookingDetails(sdate, edate, stime, etime,email,reason,mid,pro);
 }

 @DeleteMapping(value="change-details-for-days")
 public void changeBookedetailsDays(@RequestParam("start_date") String sdate,@RequestParam("end_date") String edate,@RequestParam("meeting_room")int mid,@RequestParam("email") String email,@RequestParam("reason") String reason )
 {
     emailService.updatebookingDetailsforDays(sdate, edate,email,reason,mid);
 }

 @GetMapping("/meeting-room/days")
public List<Building>getmultipleDayDetails(@RequestParam("start_date")String startDate,@RequestParam("end_date")String endDate)
{
     return emailService.getmultipleDayDetails(startDate,endDate);
}
@GetMapping(value="meeting-room/days/time")
public List<Building>getmultipleDaywithtime(@RequestParam("start_date")String sdate,@RequestParam("end_date")String edate,@RequestParam("start_time")String stime,@RequestParam("end_time")String etime)
{
return emailService.getmultipleDayDetailswithTime(sdate,edate,stime,etime);

}


@PutMapping("disable/meetingRooms")
public ResponseEntity<String> disableMeetingRooms(@RequestParam("room_id") int id,@RequestParam("reason") String reason)
{
   emailService.updateMeetingRoom(id,reason);
   return new ResponseEntity<String>("Meeting room disabled",HttpStatus.OK);

}


@PutMapping("enable/meetingRooms")
public ResponseEntity<String> enableMeetingRooms(@RequestParam("room_id") int id)
{
    emailService.enableRooms(id);
    return new ResponseEntity<>("Meeting room enabled successfuly ",HttpStatus.OK);
}


@PostMapping("meeting-rooms/teams")
public ResponseEntity<Object> meetingRoomforTeams(@RequestBody conf_confirmation c)
{	
	
        emailService.makeBookingforTeam(c);
        return  new ResponseEntity<Object>("Meeting room for teams",HttpStatus.OK);

}

    @GetMapping(value = "/roomdata")
    public List<Pagecount> getData(@RequestParam String email,@RequestParam(name="start_date",required=false) String sdate,@RequestParam(name="end_date",required=false) String edate,@RequestParam(name="offset",required=false) Integer o,@RequestParam(name="size",required=false) Integer s){
        return emailService.getDetails(email,sdate,edate,o,s);
    }

    @PutMapping("/edit_room")
    public ResponseEntity<String> editRoom(@RequestBody conf_confirmation change){
        emailService.edit_room(change);
        return new  ResponseEntity<String>("Update successful",HttpStatus.OK);


    }

    @GetMapping("meeting-rooms/teams")
    public List<Pagecount> getBookingdetailsForTeam(@RequestParam(required = false)String project_name,@RequestParam(required = false)String start_date,@RequestParam(required=false) String end_date,@RequestParam(name="offset",required=false) Integer o,@RequestParam(name="size",required=false) Integer s)
    {
        return emailService.getTeamBookingforMeetingroom(project_name,start_date,end_date,o,s);
    }
    @DeleteMapping(value="meeting-rooms")
    public ResponseEntity<String>removeBooking(@RequestBody conf_confirmation c)
    {
        emailService.removeRoomBooking(c);
        return new ResponseEntity<String>("booking removed",HttpStatus.OK);
    }
    
    @GetMapping("/on_dateRoom")
    public List<TeamMeetingDetails> roomDateData(@RequestParam(name= "date") String date){
        return emailService.roomDate(date);
    }

}
