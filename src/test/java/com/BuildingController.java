//package com;
//
//import com.Controller.buildingController;
//import com.Model.Building;
//import com.Model.Confirmation;
//import com.Model.Employee;
//import com.Model.Floor;
//import com.Service.ServiceInterface;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpStatus;
////import org.apache.http.HttpStatus;
//import org.aspectj.lang.annotation.Before;
//import org.hibernate.annotations.AccessType;
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
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.ArrayList;
//import java.util.List;
//public class BuildingController {
//
//    private MockMvc mockMvc;
//    ObjectMapper om=new ObjectMapper();
//    
//    @InjectMocks
//    buildingController p;
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
//    this.mockMvc=MockMvcBuilders.standaloneSetup(p).build();
//    }
//    
//   @Test
//   public void getBuildingDetails() throws Exception{
//    Building b=new Building();
//    
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
//    Mockito.when((e.find_building_details("2022-04-29"))).thenReturn(l);
//    mockMvc.perform(MockMvcRequestBuilders.get("/all_buildings?date=2022-04-29").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//}
//
//@Test
//public void getBuildingforDateRanges() throws Exception
//{
//    Building b=new Building();
//    
//    Floor f=new Floor();
//    List<Floor> fl=new ArrayList<Floor>();
//    fl.add(f);
//    b.set_id(1);
//    b.setName("main");
//    b.setFloors(fl);
//    b.setAvailable_no_of_meeting_rooms(68);
//    b.setTotal_bws(68);
//    b.setTotal_no_of_meeting_rooms(3);
//    b.setAvailable_no_of_meeting_rooms(3);
//    b.setBdstate("enable");
//    List<Building> l=new ArrayList<Building>();
//    l.add(b);
//
//Mockito.when((e.getBuildingForRangeofDates("29-04-2022","30-04-2022","false","12:00:00","15:00::"))).thenReturn(l);
//mockMvc.perform(MockMvcRequestBuilders.get("/details?start_date=29-04-2022&end_date=30-04-2022&report=false&start_time=12:00:00&end_time=15:00:00").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    
//
//
//}
//
////  @Test
//// public void disableBuildingFloor() throws Exception{
////   ResponseEntity<String> res=new ResponseEntity<String>("Disabled successfully",HttpStatus.OK);
////   Mockito.when((e. disablingBuildingFloor(1,"power cut"))).thenReturn(res);
//// mockMvc.perform(MockMvcRequestBuilders.get("/details?start_date=2022-04-29&end_date=2022-05-01").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    
//
//
//
//
//
//    
//}
