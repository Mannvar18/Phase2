//package com;
//
//import com.Controller.WorkstationController;
//import com.Controller.buildingController;
//import com.Model.Building;
//import com.Model.Confirmation;
//import com.Model.Dummy_Confirm;
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
//public class WorkstationControllerTests {
//
//    private MockMvc mockMvc;
//    ObjectMapper om=new ObjectMapper();
//    
//    @InjectMocks
//    WorkstationController p;
//    
//    @Mock
//    ServiceInterface e;
//    
//    private WebApplicationContext context;
//    
//    @BeforeEach
//    public void setup()
////    {
//    MockitoAnnotations.initMocks(this);
//    this.mockMvc=MockMvcBuilders.standaloneSetup(p).build();
//    }
//    
//
//    @Test
//    public void edit() throws Exception
//    {
//        Dummy_Confirm c=new Dummy_Confirm();
//        String jsonrequest=om.writeValueAsString(c);
//        ResponseEntity<String> res=new ResponseEntity<String>("workstation booked successfuly",HttpStatus.OK);
//        Mockito.when((e.edit(c))).thenReturn(res);
//        mockMvc.perform(MockMvcRequestBuilders.put("/Edit").contentType(MediaType.APPLICATION_JSON).content(jsonrequest)).andExpect(status().isOk());
//
//    }
//    
//    @Test
//    public void bookWorkstations() throws Exception{
//
//        Dummy_Confirm c=new Dummy_Confirm();
//        String jsonrequest=om.writeValueAsString(c);
//        ResponseEntity<Object> res=new ResponseEntity<Object>("workstation booked successfuly",HttpStatus.OK);
//        Mockito.when((e.confirm(c))).thenReturn(res);
//        mockMvc.perform(MockMvcRequestBuilders.post("/multiple_confirmation").contentType(MediaType.APPLICATION_JSON).content(jsonrequest)).andExpect(status().isOk());
//
//
//    }
//
//}