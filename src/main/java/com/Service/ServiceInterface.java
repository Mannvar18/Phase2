package com.Service;

import com.Dao.Page;
import com.Dao.Pagecount;
import com.Dao.TeamMeetingDetails;
import com.Model.Building;
import com.Model.Confirmation;
import com.Model.Dummy_Confirm;

import com.Model.User;
import com.Model.conf_confirmation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface ServiceInterface {

    public String findAll(String email);
    User findById(int id);
    String findRole(String email);
    List<String> findProject(String email);
    public ResponseEntity<String> edit(Dummy_Confirm conf);
    public void edit2(Dummy_Confirm conf);
  ResponseEntity<Object> confirm(Dummy_Confirm dummy_conf);
    
    public List<String> find_projects_id();
    public List<User> find_members(String project_name);
    public ResponseEntity<String> findAllnew();
   public List<Building> find_building_details(String date);
    public ResponseEntity<Object> removeBooking(Dummy_Confirm conf);
    
    List<User> findEmployee(String pro , String sdate, String edate);
       
    ResponseEntity<Object> saveDetails( conf_confirmation conf);
    ResponseEntity<Object>saveDateTime(conf_confirmation c);
    ResponseEntity<Object>saveSingleDay(conf_confirmation conf);
    ResponseEntity<Object>saveMultipledaysTime(String edate,conf_confirmation conf);
    ResponseEntity<Object> edit_room(conf_confirmation conf);
    public List<Building> getsingleDayDetails(String date);
    public List<Building> getmultipleDayDetails(String sdate,String edate);
    public List<Building> getBuildingForRangeofDates(String startDate,String endDate,String report ,String stime,String etime);
    public List<Building> getsingleDayDetailswithTime(String date,String stime,String etime);
    public List<Building> getmultipleDayDetailswithTime(String sdate,String edate,String stime,String etime);

   public List<conf_confirmation> getBookedDetailforDays(String sdate,String edate,int mid) ;
    public conf_confirmation getbookedDetails(String sdate,String edate,String stime,String etime,int mid);
    public ResponseEntity<String>  updatebookingDetails(String sdate,String edate,String stime,String etime,String email,String reason,int mid,String pro);
    public void updatebookingDetailsforDays(String sdate,String edate,String email,String reason,int mid);

    public ResponseEntity<?> disablingBuildingFloor(int num,String reason);
    public ResponseEntity<?>enablingBuildingFloor(int num1);
    public List<Page> getTeamDetails(String p2,String p3 , String p4,int o,int s);
    public List<Page> getTeamDetails(String p2,String p3,String p4);
    public List<Page> getTeamDetailsUpdated(String p2,String p3,String p4);
    List<Page> getUserDetails(String email,String sdate, String edate,int o,int s);
  ResponseEntity<?> Disable( List<Integer> l);
    ResponseEntity<?> Enable(List<Integer> li );
      List<TeamMeetingDetails> getDetails(String email);
    public ResponseEntity<String> updateWorkstationBooking(Dummy_Confirm c);
    public ResponseEntity<Object> removeBulkBooking(Dummy_Confirm conf);
    ResponseEntity<String> updateMeetingRoom(int id, String reason);
    ResponseEntity<String> enableRooms(int id);
     public ResponseEntity<String> removeRoomBooking(conf_confirmation c);
  public ResponseEntity<Object> makeBookingforTeam(conf_confirmation c);
    public List<Pagecount>getTeamBookingforMeetingroom(String pname,String sdate,String edate,int o,int s);
    List<Pagecount> getDetails(String email,String sdate,String edate,int o,int s);
    List<Page> data(String sdate, String edate,int o,int s);
    List<Page> data(String sdate, String edate);
    List<Page> onDate(String date);
//    List<conf_confirmation> roomDate(String date);
    
    List<TeamMeetingDetails> roomDate(String date);
}
  
