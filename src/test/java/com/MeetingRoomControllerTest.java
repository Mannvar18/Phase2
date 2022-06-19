//package com;
//import com.Controller.MeetingRooms;
//import com.Dao.TeamMeetingDetails;
//import com.Model.Building;
//import com.Model.Confirmation;
//import com.Model.Employee;
//import com.Model.Floor;
//import com.Model.conf_confirmation;
//import com.Service.ServiceInterface;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpStatus;
//import org.apache.commons.math3.exception.util.ExceptionContext;
//import org.apache.poi.ss.formula.functions.T;
////import org.apache.http.HttpStatus;
//import org.aspectj.lang.annotation.Before;
//import org.hibernate.annotations.AccessType;
//import org.hibernate.jpa.internal.ExceptionMapperLegacyJpaImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.ArrayList;
//import java.util.List;
//public class MeetingRoomControllerTest {
//    
//
//    private MockMvc mockMvc;
//    ObjectMapper om=new ObjectMapper();
//    
//    @InjectMocks
//    MeetingRooms m;
//    
//    @Mock
//    ServiceInterface e;
//    
//    private WebApplicationContext context;
//    
//    @BeforeEach
//    public void setup()
//    {
//    MockitoAnnotations.initMocks(this);
//    this.mockMvc=MockMvcBuilders.standaloneSetup(m).build();
//    }
//
//    @Test
//    public void disableMeetingRoom() throws Exception
//    {
//   
//    ResponseEntity<String> res =new ResponseEntity<String>("Meeting room disabled successfuly",HttpStatus.OK); 
//    Mockito.when((e.updateMeetingRoom(1,"power outage"))).thenReturn(res);
//    mockMvc.perform(MockMvcRequestBuilders.put("/disable/meetingRooms?room_id=1&reason=power outage").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    
//    }
//   
//    @Test
//    public void enableBuildingFloor() throws Exception
//    {
//   
//    ResponseEntity<String> res =new ResponseEntity<String>("Meeting room enable successfuly",HttpStatus.OK); 
//    Mockito.when((e.enableRooms(1))).thenReturn(res);
//    mockMvc.perform(MockMvcRequestBuilders.put("/enable/meetingRooms?room_id=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void saveDetails() throws Exception
//    {
//        conf_confirmation c=new conf_confirmation();
//        String jsonrequest=om.writeValueAsString(c);
//        ResponseEntity<Object> res =new ResponseEntity<Object>("Booking successful",HttpStatus.OK); 
//        Mockito.when((e.saveDetails(c))).thenReturn(res);
//        mockMvc.perform(MockMvcRequestBuilders.post("/v1/meeting-room-for-days").contentType(MediaType.APPLICATION_JSON).content(jsonrequest)).andExpect(status().isOk());
//    
//    }
//
//    @Test
//    public void saveDetailsWithTime() throws Exception
//    {
//        conf_confirmation c=new conf_confirmation();
//        String jsonrequest=om.writeValueAsString(c);
//        ResponseEntity<Object> res=new ResponseEntity<Object>("Booking successful",HttpStatus.OK); 
//        Mockito.when((e.saveSingleDay(c))).thenReturn(res);
//        mockMvc.perform(MockMvcRequestBuilders.post("/v1/meeting-room-with-time").contentType(MediaType.APPLICATION_JSON).content(jsonrequest)).andExpect(status().isOk());
//
//    }
//    @Test
//    public void getBookingDetailsforHr() throws Exception{
//
//
//        conf_confirmation c=new conf_confirmation();
//        Mockito.when((e.getbookedDetails("2022-05-01","2022-05-02","13:00","13:15",1))).thenReturn(c);
//        mockMvc.perform(MockMvcRequestBuilders.get("/booked-details?start_date=2022-05-01&end_date=2022-05-02&start_time=13:00&end_time=13:15&meeting_room=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//    }
//
//
//    @Test
//    public void getBookingDetails() throws Exception{
//      
//        conf_confirmation c=new conf_confirmation();
//        List<conf_confirmation> al=new ArrayList<>();
//        al.add(c);
//        Mockito.when((e.getBookedDetailforDays("2022-05-01","2022-05-02",1))).thenReturn(al);
//        mockMvc.perform(MockMvcRequestBuilders.get("/booked-details-days?start_date=2022-05-01&end_date=2022-05-02&meeting_room=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//        
//    }
//    @Test
//    public void hrBooking() throws Exception{
//
//        ResponseEntity<String> res =new ResponseEntity<String>("Booking overrided and new Booking made",HttpStatus.OK); 
//        Mockito.when((e.updatebookingDetails("01-05-2022","02-05-2022","13:00","13:15","nithiya@gmail.com","client connect",1,"Automated Workstation"))).thenReturn(res);
//        mockMvc.perform(MockMvcRequestBuilders.put("/change-details?start_date=2022-05-01&end_date=2022-05-02&start_time=13:00&end_time=13:15&meeting_room=1&email=nithiya@gmail.com&reason=client connect").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    }
//
//
//    @Test
//    public void getMeetingRoomdetails() throws Exception
//    {
//        Building b=new Building();
//        Floor f=new Floor();
//        List<Floor> fl=new ArrayList<Floor>();
//        fl.add(f);
//        b.set_id(1);
//        b.setName("main");
//        b.setFloors(fl);
//        b.setAvailable_no_of_meeting_rooms(68);
//        b.setTotal_bws(68);
//        b.setTotal_no_of_meeting_rooms(3);
//        b.setAvailable_no_of_meeting_rooms(3);
//        b.setBdstate("enable");
//        List<Building> l=new ArrayList<Building>();
//        l.add(b);
//
//        Mockito.when((e.getmultipleDayDetails("2022-05-01","2022-05-02"))).thenReturn(l);
//        mockMvc.perform(MockMvcRequestBuilders.get("/meeting-room/days?start_date=2022-05-01&end_date=2022-05-02").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    }
//
//  @Test
//  public void getBuildingDetailswithTime() throws Exception{
//
//    Building b=new Building();
//        Floor f=new Floor();
//        List<Floor> fl=new ArrayList<Floor>();
//        fl.add(f);
//        b.set_id(1);
//        b.setName("main");
//        b.setFloors(fl);
//        b.setAvailable_no_of_meeting_rooms(68);
//        b.setTotal_bws(68);
//        b.setTotal_no_of_meeting_rooms(3);
//        b.setAvailable_no_of_meeting_rooms(3);
//        b.setBdstate("enable");
//        List<Building> l=new ArrayList<Building>();
//        l.add(b);
//        Mockito.when((e.getmultipleDayDetailswithTime("2022-05-01","2022-05-02","10:00","10:30"))).thenReturn(l);
//        mockMvc.perform(MockMvcRequestBuilders.get("/meeting-room/days/time?start_date=2022-05-01&end_date=2022-05-02&start_time=10:00&end_time=10:30").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//  }  
//
//  @Test
//  public void saveTeamDetails() throws Exception{
//
//    ResponseEntity<Object> res =new ResponseEntity<Object>("Team booking successful",HttpStatus.OK); 
//    conf_confirmation c=new conf_confirmation();
//
//    Mockito.when((e.makeBookingforTeam(c))).thenReturn(res);
//  //  mockMvc.perform(MockMvcRequestBuilders.post("/meeting-rooms/teams").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//
//  }
//  
//  @Test
//  public void getTeamDetails() throws Exception{
// 
//   List<TeamMeetingDetails> l=new ArrayList<TeamMeetingDetails>();
//   Mockito.when((e.getTeamBookingforMeetingroom("Fitness app","2022-04-10","2022-04-11"))).thenReturn(l);
//   mockMvc.perform(MockMvcRequestBuilders.get("/meeting-rooms/teams?project_name=fitness app&start_date=2022-04-10&end_date=2022-04-11").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
// }
//
// @Test
// public void getSelfDetails() throws Exception{
//
//    List<TeamMeetingDetails> l=new ArrayList<TeamMeetingDetails>();
//    Mockito.when((e. getDetails("nithiyashree.p@nineleaps.com"))).thenReturn(l);
//    mockMvc.perform(MockMvcRequestBuilders.get("/roomdata?email=nithiyashree.p@nineleaps.com").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//
// }
//
////  @Test
////  public void updateBooking() throws Exception{
//  
////    Mockito.
//
////  }
//
//
//@Test
//public void deleteBooking() throws Exception{
//   
//    conf_confirmation c=new conf_confirmation();
//    String jsonrequest=om.writeValueAsString(c);
//    ResponseEntity<String> res =new ResponseEntity<String>("booking removed",HttpStatus.OK); 
//    Mockito.when((e.removeRoomBooking(c))).thenReturn(res);
//    mockMvc.perform(MockMvcRequestBuilders.delete("/meeting-rooms").contentType(MediaType.APPLICATION_JSON).content(jsonrequest)).andExpect(status().isOk());
//
//}
//
//  
//
//
//
//  
//}
//    
//
//
