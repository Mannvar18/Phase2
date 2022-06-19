//package com;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import java.util.ArrayList;
//import java.util.List;
//import javax.mail.internet.MimeMessage;
//import com.Dao.TeamMeetingDetails;
//import com.Model.Building;
//import com.Model.Employee;
//import com.Model.Floor;
//import com.Model.Project;
//import com.Model.User;
//import com.Model.conf_confirmation;
//import com.Model.meeting;
//import com.Repository.Buildrepo;
//import com.Repository.ConfirmationRepo;
//import com.Repository.EmailRepository;
//import com.Repository.EmployeeRepo;
//import com.Repository.Floorrepo;
//import com.Repository.Workstationrepo;
//import com.Repository.meetingRepo;
//import com.Repository.*;
//import com.Service.ServiceImpl;
//import com.Service.ServiceInterface;
////import com.google.common.base.Optional;
//
//import com.exception.UserNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//public class ServiceTest {
//    
//@Mock
//Buildrepo brepo;
//
//@Mock
//Floorrepo frepo;
//
//@Mock
//Workstationrepo wrepo;
//
//@Mock
//ConfirmationRepo crepo;
//
//@Mock
//EmailRepository urepo;
//
//@Mock
//EmployeeRepo  erepo;
//
//@Mock
//meetingRepo mrepo;
//
//@Mock
//conf_confirmationrepo bookingRepo;
//
//@Mock
//ProjectRepo prepo;
//@Mock
//JavaMailSender mailSender;
// @Mock
//MimeMessage mimeMessage;
//ServiceInterface ser;
//
//
//@BeforeEach
//    public void setup()
//    {
//        MockitoAnnotations.initMocks(this);
//        // this.mockMvc=MockMvcBuilders.standaloneSetup(home).build();
//    }
//
//@BeforeEach
//       public void onSetUp() {
//           User user = new User();
//           user=u1.get();
//           Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),null);
//           SecurityContextHolder.getContext().setAuthentication(auth);
//       }
//
//
// @BeforeEach
//void initUseCase() {
//        ser= new  ServiceImpl(brepo,frepo,wrepo,crepo,urepo,erepo,mrepo,bookingRepo,prepo,mailSender);
//    }
//
//
//
//
//
//
//
//Optional<User> u1=Optional.of(new User(1,"nithiya","nithiyashree.p@nineleaps.com","PProject manager","abc"));
//Optional<User> u2=Optional.of(new User(1,"nithiya","nithiyashree.p@nineleaps.com","employee","abc"));
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    public  void getRole() throws Exception{
//        when(urepo.findByEmail("nithiyashree.p@nineleaps.com")).thenReturn(u1.get());
//        ResponseEntity<String> res =new ResponseEntity<String>("HR",HttpStatus.OK);
//        ResponseEntity<String> al=ser.findAllnew();
//        Assertions.assertEquals(al.getStatusCode(),res.getStatusCode());
//    }
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    public void getRoleFails() throws Exception{
//        UserNotFoundException n= new UserNotFoundException();
//        when(urepo.findByEmail("nithiyashree.p@nineleaps.com")).thenReturn(null);
//        try{
//            ser.findAllnew();
//        }
//        catch(Exception e){
//            Assertions.assertEquals(e.getClass(),  n.getClass());
//        }
//    }
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getProjectName() throws Exception{
//        List<Project> al= new ArrayList<>();
//        List<String> al1=new ArrayList<>();
//        al1.add("Automatedworkstation");
//        Project p=new Project(1, "Automatedworkstation", "Sriram", 1, "sriram@gmail.com");
//        al.add(p);
//        when(prepo.findByEmail("nithiyashree.p@nineleaps.com")).thenReturn(al);
//        when(urepo.finduser("nithiyashree.p@nineleaps.com")).thenReturn(u1.get());
//        when(prepo.findProject()).thenReturn(al1);
//        List<String> n=ser.find_projects_id();
//        Assertions.assertEquals(n.size(), al1.size());
//    }
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    public void getProjectNamepass() throws Exception{
//        List<Project> al= new ArrayList<>();
//        List<String> al1=new ArrayList<>();
//        al1.add("Automatedworkstation");
//        Project p=new Project(1, "Automatedworkstation", "Sriram", 1, "sriram@gmail.com");
//        al.add(p);
//        when(prepo.findByEmail("nithiyashree.p@nineleaps.com")).thenReturn(al);
//        when(urepo.finduser("nithiyashree.p@nineleaps.com")).thenReturn(u1.get());
//        //when(prepo.findProject()).thenReturn(al1);
//        List<String> n=ser.find_projects_id();
//        Assertions.assertEquals(n.size(), al1.size());
//    }
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    public void projectmember() throws Exception{
//        List<Employee> al=new ArrayList<>();
//        Employee e=new Employee(1, "nithya", 1, "Automatedworkstation", true, "nithya@gmail.com");
//        al.add(e);
//        when(erepo.findByProjectname("Automatedworkstation")).thenReturn(al);
//        List<Employee> al1=ser.find_members("Automatedworkstation");
//        Assertions.assertEquals(al.size(),al1.size() );
//    }
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getEmployees() throws Exception{
//        List<Employee> al=new ArrayList<>();
//        Employee e=new Employee(1, "nithya", 1, "Automatedworkstation", true, "nithya@gmail.com");
//        when(erepo.findByProjectName("AutomatedWorkstation")).thenReturn(al);
//        when(crepo.findBooked("nithya@gmail.com","25-04-2022","27-04-2022")).thenReturn(Integer.valueOf("nithya@gmail.com"));
//        Assertions.assertEquals(al.size(), ser.findEmployee("AutomatedWorkstation","25-04-2022","27-04-2022").size());
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void disablmeetingRoom() throws Exception
//    {
//    Mockito.doNothing()
//    .when(mailSender).send((MimeMessage) any());
//    Mockito.doReturn(mimeMessage)
//    .when(mailSender).createMimeMessage();
//    Mockito.doNothing().when(mrepo).disableRoom(123);
//    conf_confirmation c= new conf_confirmation(1, "22-04-2022", "24-04-2022","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 1, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findpersonbymeetingroom(1,2,3)).thenReturn(al);
//    User u1=(new User(1,"nithiya","nithiyashree.p@nineleaps.com","HR","abc"));
//    when(urepo.findByEmail("nithiyashree.p@nineleaps.com")).thenReturn(u1);
//    Mockito.doNothing().when(bookingRepo).deletePerson("nithya@gmail.com", 1, 2, 3);
//    ResponseEntity<String> a=ser.updateMeetingRoom(123,"power shortage");
//    Assertions.assertEquals(a.getStatusCode(), HttpStatus.ACCEPTED);
//
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void saveDetails() throws Exception{
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    Object obj= ser.saveDetails(c);
//    ResponseEntity<Object>r=new ResponseEntity<Object>("Booking successful",HttpStatus.OK);
//    Assertions.assertEquals(obj, r);
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void savesingleday() throws Exception{
//    when(urepo.findname("nithya@gmail.com")).thenReturn("nithya");
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    Object obj= ser.saveSingleDay(c);
//    ResponseEntity<Object>r=new ResponseEntity<Object>("booking successful",HttpStatus.OK);
//    Assertions.assertEquals(obj, r);
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getbookedDetails() throws Exception{
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    when(bookingRepo.findUserby("2022-04-22","2022-04-24","12:00","02:00",1,2,3)).thenReturn(c);
//    conf_confirmation c1=ser.getbookedDetails("2022-04-22","2022-04-24","12:00","02:00",123);
//    Assertions.assertEquals(c1.email, "nithya@gmail.com");
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getBookedDetailforDays() throws Exception{
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAllUserby("2022-04-22","2022-04-24",3)).thenReturn(al);
//    List<conf_confirmation> cl=ser.getBookedDetailforDays("2022-04-22","2022-04-24",123);
//    Assertions.assertEquals(cl.size(), 1);
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void updatebookingDetails() throws Exception{
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findUserby("2022-04-22","2022-04-24","12:00","02:00",1,2,3)).thenReturn(c);
//    Mockito.doNothing()
//    .when(mailSender).send((MimeMessage) any());
//    Mockito.doReturn(mimeMessage)
//    .when(mailSender).createMimeMessage();
//    User u1=(new User(1,"nithiya","nithiyashree.p@nineleaps.com","HR","abc"));
//    when(urepo.finduser("nithiyashree.p@nineleaps.com")).thenReturn(u1);
//    when(urepo.findname("nithiyashree.p@nineleaps.com")).thenReturn("nithya");
//    Mockito.doNothing().when(bookingRepo).updateUser("nithiyashree.p@nineleaps.com", "nithya","client connect", 1, 2, 3, "22-04-2022", "24-04-2022","12:00", "02:00","Fitness App");
//    ResponseEntity<String> n=ser.updatebookingDetails("22-04-2022","24-04-2022","12:00","02:00","nithiyashree.p@nineleaps.com","client connect",123,"Fitness App");
//    Assertions.assertEquals(n.getStatusCode(),HttpStatus.OK);
//   }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getmultipleDayDetails() throws Exception{
//    Building build= new Building();
//    Floor f= new Floor();
//    List<Floor> fl= new ArrayList<>();
//    fl.add(f);
//    f.setAvailable_no_of_meeting_rooms(2);
//    f.setFloor_name("first");
//    f.setFloor_no(1);
//    f.setFloor_state("enable");
//    f.setNo_of_workstations(30);
//    List<meeting>ml=new ArrayList<>();
//    meeting m=new meeting();
//    m.setCapacity(10);
//    m.setName("firts");
//    m.setNumber(1);
//    m.setState(false);
//    m.setWstate("enable");
//    ml.add(m);
//    f.setMeetings(ml);
//    build.setBdstate("enable");
//    build.setName("main");
//    build.setNo_of_workstations(40);
//    build.setReason_for_disabling_building(null);
//    build.set_id(1);
//    build.setAvailable_no_of_meeting_rooms(2);
//    build.setTotal_bws(40);
//    build.setFloors(fl);
//    List<Building> bl=new ArrayList<>();
//    bl.add(build);
//    when(brepo.findAll()).thenReturn(bl);
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAll()).thenReturn(al);
//    List<Building> b=ser.getmultipleDayDetails("2022-04-22", "2022-04-24");
//    Assertions.assertEquals(b.size(), 1);
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getmultipleDayDetailselsepart() throws Exception{
//    Building build= new Building();
//    Floor f= new Floor();
//    List<Floor> fl= new ArrayList<>();
//    fl.add(f);
//    f.setAvailable_no_of_meeting_rooms(2);
//    f.setFloor_name("first");
//    f.setFloor_no(1);
//    f.setFloor_state("enable");
//    f.setNo_of_workstations(30);
//    List<meeting>ml=new ArrayList<>();
//    meeting m=new meeting();
//    m.setCapacity(10);
//    m.setName("firts");
//    m.setNumber(1);
//    m.setState(false);
//    m.setWstate("enable");
//    ml.add(m);
//    f.setMeetings(ml);
//    build.setBdstate("disable");
//    build.setName("main");
//    build.setNo_of_workstations(40);
//    build.setReason_for_disabling_building(null);
//    build.set_id(1);
//    build.setAvailable_no_of_meeting_rooms(2);
//    build.setTotal_bws(40);
//    build.setFloors(fl);
//    List<Building> bl=new ArrayList<>();
//    bl.add(build);
//    when(brepo.findAll()).thenReturn(bl);
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAll()).thenReturn(al);
//    List<Building> b=ser.getmultipleDayDetails("2022-04-22", "2022-04-24");
//    Assertions.assertEquals(b.size(), 1);
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getmultipleDayDetailswithTime() throws Exception{
//    Building build= new Building();
//    Floor f= new Floor();
//    List<Floor> fl= new ArrayList<>();
//    fl.add(f);
//    f.setAvailable_no_of_meeting_rooms(2);
//    f.setFloor_name("first");
//    f.setFloor_no(1);
//    f.setFloor_state("enable");
//    f.setNo_of_workstations(30);
//    List<meeting>ml=new ArrayList<>();
//    meeting m=new meeting();
//    m.setCapacity(10);
//    m.setName("firts");
//    m.setNumber(1);
//    m.setState(false);
//    m.setWstate("enable");
//    ml.add(m);
//    f.setMeetings(ml);
//    build.setBdstate("enable");
//    build.setName("main");
//    build.setNo_of_workstations(40);
//    build.setReason_for_disabling_building(null);
//    build.set_id(1);
//    build.setAvailable_no_of_meeting_rooms(2);
//    build.setTotal_bws(40);
//    build.setFloors(fl);
//    List<Building> bl=new ArrayList<>();
//    bl.add(build);
//    when(brepo.findAll()).thenReturn(bl);
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAll()).thenReturn(al);
//    List<Building> b=ser.getmultipleDayDetailswithTime("2022-04-22", "2022-04-24","12:00", "13:00");
//    Assertions.assertEquals(b.size(), 1);
//
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getmultipleDayDetailswithTimeelsepart() throws Exception{
//    Building build= new Building();
//    Floor f= new Floor();
//    List<Floor> fl= new ArrayList<>();
//    fl.add(f);
//    f.setAvailable_no_of_meeting_rooms(2);
//    f.setFloor_name("first");
//    f.setFloor_no(1);
//    f.setFloor_state("enable");
//    f.setNo_of_workstations(30);
//    List<meeting>ml=new ArrayList<>();
//    meeting m=new meeting();
//    m.setCapacity(10);
//    m.setName("firts");
//    m.setNumber(1);
//    m.setState(false);
//    m.setWstate("enable");
//    ml.add(m);
//    f.setMeetings(ml);
//    build.setBdstate("disable");
//    build.setName("main");
//    build.setNo_of_workstations(40);
//    build.setReason_for_disabling_building(null);
//    build.set_id(1);
//    build.setAvailable_no_of_meeting_rooms(2);
//    build.setTotal_bws(40);
//    build.setFloors(fl);
//    List<Building> bl=new ArrayList<>();
//    bl.add(build);
//    when(brepo.findAll()).thenReturn(bl);
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAll()).thenReturn(al);
//    List<Building> b=ser.getmultipleDayDetailswithTime("2022-04-22", "2022-04-24","12:00", "13:00");
//    Assertions.assertEquals(b.size(), 1);
//
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void enablemeetingroom() throws Exception{
//        ResponseEntity<String>s = ser.enableRooms(1);
//        ResponseEntity<String> n=new ResponseEntity<String>("Meeting room enabled successfuly",HttpStatus.OK);
//    Assertions.assertEquals(HttpStatus.OK,s.getStatusCode());
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getTeamBookingforMeetingroom() throws Exception{
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAllUsers()).thenReturn(al);
//    when(brepo.getBuildingname(1)).thenReturn("main");
//    List<TeamMeetingDetails> l=ser.getTeamBookingforMeetingroom(null,null,null);
//    Assertions.assertEquals(l.size(), 1);
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getTeamBookingforMeetingroomelsePart() throws Exception{
//    conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//    when(bookingRepo.findAllUsers()).thenReturn(al);
//    when(brepo.getBuildingname(1)).thenReturn("main");
//    List<TeamMeetingDetails> l=ser.getTeamBookingforMeetingroom(null,"2022-04-22","2022-04-24");
//    Assertions.assertEquals(l.size(), 1);
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void getSelfDetails() throws Exception{
//        conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//    List<conf_confirmation> al= new ArrayList<>();
//    al.add(c);
//        when((bookingRepo.findByEmail("nithya@gmail.com"))).thenReturn(al);
//        when(brepo.getBuildingname(1)).thenReturn("Main");
//        List<TeamMeetingDetails> l=ser.getDetails("nithya@gmail.com");
//        Assertions.assertEquals(l.size(),1);
//
//    }
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void edit_room() throws Exception{
//        conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//        String ss= String.valueOf(ser.edit_room(c));
//       Assertions.assertEquals("updated", "updated");
//
//
//    }
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void removeRoomBooking() throws Exception{
//        conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//        User u1=(new User(1,"nithiya","nithiyashree.p@nineleaps.com","Project manager","abc"));
//     when(urepo.finduser("nithiyashree.p@nineleaps.com")).thenReturn(u1);
//     List<String> s=new ArrayList<>();
//     s.add("nithya");
//     when(erepo.findEmails("Automated workstation")).thenReturn(s);
//     when(urepo.findname("nithya")).thenReturn("nithya");
//     Mockito.doNothing()
//    .when(mailSender).send((MimeMessage) any());
//    Mockito.doReturn(mimeMessage)
//    .when(mailSender).createMimeMessage();
//    ResponseEntity<String> s1=ser.removeRoomBooking(c);
//    Assertions.assertEquals(HttpStatus.OK, s1.getStatusCode());
//
//
//
//    }
//
//
//
//    @Test
//    @ExtendWith(MockitoExtension.class)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    public void removeRoomBookingelse() throws Exception{
//        conf_confirmation c= new conf_confirmation(1, "2022-04-22", "2022-04-24","12:00", "02:00", "nithya@gmail.com", "nithya", 1, 2, 123, "nithya@gmail.com", "Automated workstation","reason");
//        User u1=(new User(1,"nithiya","nithiyashree.p@nineleaps.com","employee","abc"));
//     when(urepo.finduser("nithiyashree.p@nineleaps.com")).thenReturn(u1);
//     List<String> s=new ArrayList<>();
//     s.add("nithya");
//     when(erepo.findEmails("Automated workstation")).thenReturn(s);
//     when(urepo.findname("nithya")).thenReturn("nithya");
//     Mockito.doNothing()
//    .when(mailSender).send((MimeMessage) any());
//    Mockito.doReturn(mimeMessage)
//    .when(mailSender).createMimeMessage();
//    ResponseEntity<String> s1=ser.removeRoomBooking(c);
//    Assertions.assertEquals(HttpStatus.OK, s1.getStatusCode());
//
//    }
//
//
//
//
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
