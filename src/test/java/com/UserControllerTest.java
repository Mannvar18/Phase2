//package com;
//import com.Controller.UserController;
//import com.Model.AuthRequest;
//import com.Model.Confirmation;
//import com.Model.Employee;
//import com.Service.ServiceInterface;
//import com.Service.userDetailsService;
//import com.Util.jwtFilter;
//import com.Util.jwtUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
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
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import org.springframework.security.authentication.AuthenticationManager;
//import java.util.ArrayList;
//import java.util.List;
//public class UserControllerTest {
//
//
//private MockMvc mockMvc;
//ObjectMapper om=new ObjectMapper();
//
//@InjectMocks
//UserController p;
//
//@Mock
//ServiceInterface e;
//
//
//@Mock
//AuthenticationManager authenticationManager;
//AuthRequest authrequest=new AuthRequest();
//
//
//@Mock
//jwtUtil jwtutil;
//
//@Autowired
//private jwtFilter jwtfilter;
//
//@MockBean
//private userDetailsService userdetailsservice;
//private WebApplicationContext context;
//
//@BeforeEach
//public void setup()
//{
//MockitoAnnotations.initMocks(this);
//this.mockMvc=MockMvcBuilders.standaloneSetup(p).build();
//}
//
//@Test
//public void getRole() throws Exception
//{
//    ResponseEntity<String> res =new ResponseEntity<String>("HR",HttpStatus.OK); 
//    Mockito.when((e.findAllnew())).thenReturn(res);
//    mockMvc.perform(MockMvcRequestBuilders.get("/email_get_role_new").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//}
//
//@Test
//public void getProjecName() throws Exception
//{
//    List<String> pname=new ArrayList<>();
//    pname.add("Automated workstation");
//    pname.add("Fitness app");
//    Mockito.when((e.find_projects_id())).thenReturn(pname);
//    mockMvc.perform(MockMvcRequestBuilders.get("/find_projects_name").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    
//}
//
//@Test
//public void getProjectMembers() throws Exception
//{ 
//
//    Employee e1=new Employee();
//    List<Employee> el=new ArrayList<Employee>();
//    el.add(e1);
//    Mockito.when((e.find_members("fitness app"))).thenReturn(el);
//    mockMvc.perform(MockMvcRequestBuilders.get("/find_project_members?project-name=fitness app").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    
//
//}
//@Test
//public void getEmployees() throws Exception
//{
//       
//    Employee e1=new Employee();
//    List<Employee> el=new ArrayList<Employee>();
//    el.add(e1);
//    Mockito.when((e.findEmployee("fitness app","19-04-2022","22-04-2022"))).thenReturn(el);
//    mockMvc.perform(MockMvcRequestBuilders.get("/employee?project_name=fitness app&sdate=19-04-2022&edate=22-04-2022").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//}
//
//@Test
//public void getTeamDetails() throws Exception
//{
//    Confirmation c=new Confirmation();
//    List<Confirmation> cl=new ArrayList<Confirmation>();
//    cl.add(c);
//    Mockito.when((e.getTeamDetails( "fitness app", "20-04-2022" ,"20-04-2022"))).thenReturn(cl);
//    mockMvc.perform(MockMvcRequestBuilders.get("/Pjdata?project_name=fitness app&start_date=20-04-2022&end_date=20-04-2022").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//    
//
//}
//@Test
//public void getSelfDetails() throws Exception{
//
//    Confirmation c=new Confirmation();
//    List<Confirmation> cl=new ArrayList<Confirmation>();
//    cl.add(c);
//    Mockito.when((e.getUserDetails("fitness app","10-04-2022","18-04-2022"))).thenReturn(cl);
//    mockMvc.perform(MockMvcRequestBuilders.get("/userdata?email=nithiyashree.p@nineleaps.com&start_date=10-04-2022&end_date=18-04-2022").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//}
//
//@Test
//public void TestAuthenticate() throws Exception
//{
//
//	authrequest.setEmail("nithiya@gmail.com");
//	authrequest.setPassword("12342");
//	//String email=
//
//	Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getEmail(),authrequest.getPassword()))).thenReturn(null);
//
//	String jsonRequest=om.writeValueAsString(authrequest);
//	mockMvc.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk());
//
//
//}
//
//@Test
//public void TestAuthenticateException() throws Exception
//{
//
//	authrequest.setEmail("nithiya@gmail.com");
//	authrequest.setPassword("1234");
//
//	Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getEmail(),authrequest.getPassword()))).thenThrow(RuntimeException.class);
//
//	String jsonRequest=om.writeValueAsString(authrequest);
//	mockMvc.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect((status().isNotFound()));
//
//
//}
//
//
//
//
//}
