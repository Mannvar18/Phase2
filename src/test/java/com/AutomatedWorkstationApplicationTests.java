package com;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.Model.Building;
import com.Model.Confirmation;
import com.Model.Dummy_Confirm;
import com.Model.Floor;
import com.Model.User;
import com.Model.Workstation;
import com.Model.conf_confirmation;
import com.Model.meeting;
import com.Repository.Buildrepo;
import com.Repository.ConfirmationRepo;
import com.Repository.EmailRepository;

import com.Repository.Floorrepo;

import com.Repository.Workstationrepo;
import com.Repository.conf_confirmationrepo;
import com.Service.Helper;
import com.Service.ServiceImpl;
import com.Service.userDetailsImpl;
import com.exception.BackendException;
import com.exception.ResourceNotFoundException;
import com.exception.WorkstationNotFoundException;
@RunWith(SpringRunner.class)

@SpringBootTest
class AutomatedWorkstationApplicationTests {

	@MockBean
	public ConfirmationRepo conrepo;
	@MockBean
	public Buildrepo brepo;
	@MockBean
	public Floorrepo frepo;
	@MockBean
	public Workstationrepo wrepo;
	@MockBean
	public EmailRepository erepo;
	@MockBean
	public conf_confirmationrepo confrepo;
	@Autowired
	 public ServiceImpl emailservice;
	@Autowired
	Helper help;
	Optional<User> u1 = Optional.of(new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Employee","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false));
	
	@BeforeEach
	public void onSetUp() {
	User user = new User();
	user=u1.get();
	Authentication auth =new UsernamePasswordAuthenticationToken(user.getEmail(),null);
	SecurityContextHolder.getContext().setAuthentication(auth);
	
	}
	
//	
//	@Test
//	public void RemoveBookingtest() {
//		List<String> list=new ArrayList<>();
//		list.add("manish.neelapu@nineleaps.com");
//		String mail="manish.neelapu@nineleaps.com";
//		String date="2022-04-12";
//		List<String> ldates=new ArrayList<>();
//		ldates.add("2022-04-12");
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Employee","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		List<Integer> list1=new ArrayList<>();
//		list1.add(121);
//		List<String> changedDate=new ArrayList<>();
//		changedDate.add(date);
//		Confirmation C=new Confirmation(1,"2022-04-12","manish.neelapu@nineleaps.com","Manish Reddy",1,2,1,"Main","3rd-Floor","false");
////		List<String> e_mail=new ArrayList<>();
////		e_mail.add("manish.neelapu@nineleaps.com");
//				
//		Dummy_Confirm conf=new Dummy_Confirm(1,"2022-04-12",list,1,12,list1,ldates,"2022-04-12",changedDate);
//		when(conrepo.getWID(mail,date)).thenReturn(1);
//		when(conrepo.getbid(mail, date)).thenReturn(1);
//		when(conrepo.getfid(mail, date)).thenReturn(2);
//		when(erepo.findname(mail)).thenReturn("Manish Reddy");
//		when(conrepo.selectbyfid(1,2,1,date)).thenReturn(C);
//		when(conrepo.selectbyfid(11,22,1,date)==null).thenThrow(WorkstationNotFoundException.class);
//		when(conrepo.getOnDateCount(mail, date)).thenReturn(1);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn(mail);
//		when(erepo.finduser(mail)).thenReturn(U);
//		//when(C.getEmail()).thenReturn(mail);
//		//when(C.getName()).thenReturn("Manish Reddy");
//		emailservice.removeBooking(conf);
//		verify(conrepo,times(1)).deletebyfid(1, 2, 1, date);
//		
//	}
	//completed
//	@Test
//	public void RemoveBulkBookingtest() {
//		List<String> list=new ArrayList<>();
//		list.add("manish.neelapu@nineleaps.com");
//		String mail="manish.neelapu@nineleaps.com";
//		String date="2022-04-12";
//		List<String> ldates=new ArrayList<>();
//		ldates.add("2022-04-12");
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Employee","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		List<Integer> list1=new ArrayList<>();
//		list1.add(221);
//		List<String> changedDate=new ArrayList<>();
//		changedDate.add(date);
//		Confirmation C=new Confirmation(1,"2022-04-12","manish.neelapu@nineleaps.com","Manish Reddy",2,2,1,"SVC","1st-Floor","false");
//				
//		Dummy_Confirm conf=new Dummy_Confirm(1,"2022-04-12",list,2,22,list1,ldates,"2022-04-12",changedDate);
//		when(conrepo.getwid(mail,date)).thenReturn(1);
//		when(conrepo.getbid(mail, date)).thenReturn(2);
//		when(conrepo.getfid(mail, date)).thenReturn(2);
//		when(erepo.findname(mail)).thenReturn("Manish Reddy");
//		when(conrepo.selectbyfid(2,2,1,date)).thenReturn(C);
//		when(conrepo.selectbyfid(1,1,11,date)==null).thenThrow(WorkstationNotFoundException.class);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn(mail);
//		when(erepo.finduser(mail)).thenReturn(U);
//		//when(C.getEmail()).thenReturn(mail);
//		//when(C.getName()).thenReturn("Manish Reddy");
//		emailservice.removeBulkBooking(conf);
//		verify(conrepo,times(1)).deletebyfid(2, 2, 1, date);
//		
//	}
//	COM1T
//	@Test
//	void getUserDataTest() {
//		  Pageable pageable = PageRequest.of(0, 1);
//		String email="manish.neelapu@nineleaps.com";
//		when(conrepo.getByEmailDate(email, "2022-04-04","2022-04-04", pageable)).thenReturn(Stream
//				.of(new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",1,2,1,"Main","3rd-Floor","false")).collect(Collectors.toList()));
//		when(conrepo.getByEmailDateCount(email,"2022-04-04" , "2022-04-04")).thenReturn(1);
//		assertEquals(1,emailservice.getUserDetails(email,"2022-04-04","2022-04-04",0,1).size());
//	}
	//COM
//	@Test
//	void PjdataTest() {
//		List<String> name=new ArrayList<>();
//		 name.add("Manish Reddy");
//		 String mail="manish.neelapu@nineleaps.com";
//		 String date="2022-04-04";
//		 String edate="2022-04-04";
//		 String pjname="Automated Workstation";
//		 when(erepo.findByProject_Name(pjname)).thenReturn(name);
//		when(erepo.findEmail("Manish Reddy")).thenReturn(mail);
//		when(conrepo.getUsingEmail(mail, date,"2022-04-04")).thenReturn(Stream
//				.of(new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",1,2,1,"Main","3rd-Floor","false")).collect(Collectors.toList()));
//		assertEquals(1,emailservice.getTeamDetails(pjname, date,edate,0,1).size());
//	}
	//COM
//	@Test
//	void EnablestateTest() {
//		List<Integer> num=new ArrayList<>();
//		num.add(111);	
//		int wid=111;
//		Boolean t=true;
//		when(wrepo.getWstate2(wid)).thenReturn(1);	
//		emailservice.Enable(num);
//		verify(wrepo,times(1)).unsetW(wid);
//		
//	}
//	
	//COM
//	@Test
//	void Disablestate() {
//		List<Integer> num=new ArrayList<>();
//		num.add(113);		
//		int wid=113;
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","HR","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		Confirmation C=new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",1,1,3,"Main","2nd-Floor","false");
//		when(conrepo.findpersonbyworkstation(1,1,3)).thenReturn(Stream
//				.of(new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",1,1,3,"Main","2nd-Floor","false")).collect(Collectors.toList()));
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//		when(wrepo.getWstate(wid)).thenReturn(1);
//		
//		emailservice.Disable(num);
//		verify(conrepo,times(1)).deleteWEntry(1,1,3);
//		verify(wrepo,times(1)).setwstate(wid);
//	}
	//COM
//	@Test
//	void DisablestateTest2() {
//		List<Integer> num=new ArrayList<>();
//		num.add(1113);		
//		int wid=1113;
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Employee","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(conrepo.findpersonbyworkstation(1,1,13)).thenReturn(Stream
//				.of(new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",1,1,13,"Main","2nd-Floor","false")).collect(Collectors.toList()));
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//		when(wrepo.getWstate(wid)).thenReturn(1);
//		
//		emailservice.Disable(num);
//		verify(conrepo,times(1)).deleteWEntry(1,1,13);
//		verify(wrepo,times(1)).setwstate(wid);
//	}
//	COM
//	@Test
//	public void EditTest() {
//		
//		List<String> list=new ArrayList<>();
//		list.add("manish.neelapu@nineleaps.com");
//		List<Integer> list1=new ArrayList<>();
//		list1.add(113);
//		List<String> ldates=new ArrayList<>();
//		ldates.add("2022-04-07");
//		String date="2022-04-07";
//		String date_end="2022-04-07";
//		String mail="manish.neelapu@nineleaps.com";
//		List<String> changedDate=new ArrayList<>();
//		changedDate.add(date);
//		Dummy_Confirm conf=new Dummy_Confirm(1,"2022-04-07",list,1,11,list1,ldates,"2022-04-07",changedDate);
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Project manager","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(erepo.findname(mail)).thenReturn("Manish Reddy");
//		when(frepo.getFloorname(11)).thenReturn("2nd-Floor");
//		when(brepo.getBuildingname(1)).thenReturn("Main");
//		when(wrepo.getCount(113)).thenReturn(1);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//		emailservice.edit(conf);
//		verify(conrepo,times(1)).bupdate(1,mail,date);
//		verify(conrepo,times(1)).fupdate(1,mail,date);
//		verify(conrepo,times(1)).wupdate(3,mail,date);
//		verify(conrepo,times(1)).bdnameupdate("Main", mail, date);
//		verify(conrepo,times(1)).flnameupdate("2nd-Floor", mail,date);
//		
//	}
//	COM
//	@Test
//	public void findRoleTest() {
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Project manager","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//		emailservice.findRole("manish.neelapu@nineleaps.com");
//		assertEquals('P',emailservice.findRole("manish.neelapu@nineleaps.com").charAt(0));
//	}
//	COM
//	@Test
//	public void findProjectTest() {
//		List<String> list=new ArrayList<>();
//		list.add("Automated Workstation");
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Project manager","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//		when(erepo.findByManager_name("manish.neelapu@nineleaps.com")).thenReturn(list);
//		emailservice.findProject("manish.neelapu@nineleaps.com");
//		assertEquals(1,emailservice.findProject("manish.neelapu@nineleaps.com").size());
//		
//	}
//COM	
//	@Test
//	public void ConfirmTest() {
//		List<String> list=new ArrayList<>();
//		list.add("manish.neelapu@nineleaps.com");
//		List<Integer> list1=new ArrayList<>();
//		list1.add(113);
//		List<String> ldates=new ArrayList<>();
//		ldates.add("2022-04-07");
//		String date="2022-04-07";
//		String date_end="2022-04-07";
//		String mail="manish.neelapu@nineleaps.com";
//		List<String> changedDate=new ArrayList<>();
//		changedDate.add(date);
//		Dummy_Confirm conf=new Dummy_Confirm(1,"2022-04-07",list,1,11,list1,ldates,"2022-04-07",changedDate);
//		Confirmation C=new Confirmation(1,"2022-04-07","manish.neelapu@nineleaps.com","Manish Reddy",1,1,3,"Main","2nd-Floor","false");
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","HR","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(conrepo.findUsingData(date, 1, 1, 3)).thenReturn(0);
//		when(erepo.getRole(mail)).thenReturn("HR");
//		when(brepo.findId(1)).thenReturn("Main");
//		when(frepo.findFId(11)).thenReturn("2nd-Floor");
//		when(erepo.findname(mail)).thenReturn("Manish Reddy");
//		when(conrepo.findDuplicate(mail, date)).thenReturn(0);
//		when(conrepo.checkexisiting(mail, date)).thenReturn(0);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.finduser(mail)).thenReturn(U);
//		emailservice.confirm(conf);
//		verify(conrepo,times(1)).save(C);
//		
//	}
//	COM
//	@Test
//	public void ConfirmTesting2() {
//		List<String> list=new ArrayList<>();
//		list.add("manish.neelapu@nineleaps.com");
//		List<Integer> list1=new ArrayList<>();
//		list1.add(113);
//		List<String> ldates=new ArrayList<>();
//		ldates.add("2022-04-07");
//		String date="2022-04-07";
//		String date_end="2022-04-07";
//		String mail="manish.neelapu@nineleaps.com";
//		List<String> changedDate=new ArrayList<>();
//		changedDate.add(date);
//		Dummy_Confirm conf=new Dummy_Confirm(1,"2022-04-07",list,1,11,list1,ldates,"2022-04-07",changedDate);
//		Confirmation C1=new Confirmation(1,"2022-04-07","manish.neelapu@nineleaps.com","Manish Reddy",1,1,3,"Main","2nd-Floor","false");
//		User U1=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","Employee","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(conrepo.findUsingData(date, 1, 1, 3)).thenReturn(0);
//		when(erepo.getRole(mail)).thenReturn("Employee");
//		when(brepo.findId(1)).thenReturn("Main");
//		when(frepo.findFId(11)).thenReturn("2nd-Floor");
//		when(erepo.findname(mail)).thenReturn("Manish Reddy");
//		when(conrepo.findDuplicate(mail, date)).thenReturn(0);
//		when(conrepo.checkexisiting(mail, date)).thenReturn(0);
//		//when(conrepo.checkexisiting(mail, date)).thenReturn(null);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.finduser(mail)).thenReturn(U1);
//		emailservice.confirm(conf);
//		verify(conrepo,times(1)).save(C1);
//		
//	}
//COM
//	@Test
//	public void ConfirmTesting3() {
//		List<String> list=new ArrayList<>();
//		list.add("manish.neelapu@nineleaps.com");
//		List<Integer> list1=new ArrayList<>();
//		list1.add(113);
//		List<String> ldates=new ArrayList<>();
//		ldates.add("2022-04-07");
//		String date="2022-05-25";
//		String date_end="2022-05-25";
//		String mail="manish.neelapu@nineleaps.com";
//		List<String> changedDate=new ArrayList<>();
//		changedDate.add(date);
//		Dummy_Confirm conf=new Dummy_Confirm(1,"2022-05-25",list,1,11,list1,ldates,"2022-05-25",changedDate);
//		Confirmation C1=new Confirmation(1,"2022-05-25","manish.neelapu@nineleaps.com","Manish Reddy",1,1,3,"Main","2nd-Floor","false");
//		User U1=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","HR","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(conrepo.findUsingData(date, 1, 1, 3)).thenReturn(1);
//		when(conrepo.findDistinctEmails(3,1,1)).thenReturn(list);
//		when(erepo.getRole(mail)).thenReturn("HR");
//		when(brepo.findId(1)).thenReturn("Main");
//		when(frepo.findFId(11)).thenReturn("2nd-Floor");
//		when(erepo.findname(mail)).thenReturn("Manish Reddy");
//		when(conrepo.findDuplicate(mail, date)).thenReturn(0);
//		when(conrepo.checkexisiting(mail, date)).thenReturn(0);
//		//when(conrepo.checkexisiting(mail, date)).thenReturn(null);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.finduser(mail)).thenReturn(U1);
//		emailservice.confirm(conf);
//		verify(conrepo,times(1)).save(C1);
//		verify(conrepo,times(1)).deleteDate(date, 1, 1,3);
//		
//	}
//	COM
//	@Test
//	public void EnableBuildingFloorTest() {
//		int n=1;
//		when(brepo.getBstate(n)).thenReturn(0);
//		emailservice.enablingBuildingFloor(n);
//		verify(brepo,times(1)).setStatetoEnable(n);
//		verify(conrepo,times(1)).returnBEntry(n);
//		verify(confrepo,times(1)).returnBEntry(n);
//	}
//	@Test
//	public void EnableBuildingFloorTest2() {
//		int n=12;
//		when(frepo.getFstate(n)).thenReturn(0);
//		emailservice.enablingBuildingFloor(n);
//		verify(frepo,times(1)).setFstatetoEnable(n);
//		verify(conrepo,times(1)).returnFEntry(1, 2);
//		verify(confrepo,times(1)).returnFEntry(1, 2);
//	}
//	

//	@Test
//	public void DisableBuildingFloorTest() {
//		int b=1;
//		int f=1;
//		String reason="MY WISH";
//		User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","HR","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//		when(conrepo.findpersonbybuilding(b)).thenReturn(Stream
//				.of(new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",b,f,1,"SVC","2nd-Floor","false")).collect(Collectors.toList()));
//		when(confrepo.findpersonbybuilding(b)).thenReturn(Stream
//				.of(new conf_confirmation(1, "2022-04-04", "2022-04-04","12:00", "02:00", "manish.neelapu@nineleaps.com", "Manish Reddy", 1, 2, 123, "manish.neelapu@nineleaps.com", "Automated workstation","reason")).collect(Collectors.toList()));//
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//		when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//		when(brepo.getBstate(b)).thenReturn(1);
//		emailservice.disablingBuildingFloor(b, reason);
//		verify(brepo,times(1)).updateReason(reason, b);
//		verify(conrepo,times(1)).deleteBEntry(b);
//		verify(brepo,times(1)).setState(b);
//		verify(confrepo,times(1)).deleteBEntry(b);
//	}
	
//@Test
//public void DisableBuildingFloorTest2() {
//	int b=2;
//	int f=2;
//	int n=22;
//	String reason="MY WISH";
//	User U=new User(1,"Manish Reddy","manish.neelapu@nineleaps.com","HR","$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy","Automated Workstation",false);
//	
//	when(conrepo.findbypersonbyfloor(b, f)).thenReturn(Stream.of(new Confirmation(1,"2022-04-04","manish.neelapu@nineleaps.com","Manish Reddy",b,f,1,"SVC","2nd-Floor","false")).collect(Collectors.toList()));
//	when(confrepo.findbypersonbyfloor(2, 2)).thenReturn(Stream
//			.of(new conf_confirmation(1, "2022-04-04", "2022-04-04","12:00", "02:00", "manish.neelapu@nineleaps.com", "Manish Reddy", 1, 2, 123, "manish.neelapu@nineleaps.com", "Automated workstation","reason")).collect(Collectors.toList()));
//	Authentication authentication = Mockito.mock(Authentication.class);
//	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//	Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//	SecurityContextHolder.setContext(securityContext);
//	when(authentication.getName()).thenReturn("manish.neelapu@nineleaps.com");
//	when(erepo.findByEmail("manish.neelapu@nineleaps.com")).thenReturn(U);
//	when(frepo.getFstate(n)).thenReturn(1);
//	when(frepo.findDisableCount(2)).thenReturn(1);
//	when(frepo.findCount(2)).thenReturn(2);
//	emailservice.disablingBuildingFloor(n, reason);
//	verify(frepo,times(1)).updateReasonforFloor(reason, n);
//	verify(conrepo,times(1)).deleteFEntry(b, f);
//	verify(frepo,times(1)).setFstate(n);
//	
//	}  
//COM

//@Test
//public void getBuildingForRangeOfDatesTest() {
//	String start_date="2022-04-29";
//	String end_date="2022-04-29";
//	int count=1;
//	List<Integer> list= new ArrayList<>();
//	list.add(1);
//	Workstation W=new Workstation(111,true,"enable","MY WISH");
//	meeting M=new meeting(111,false, "enable", "Meeting Room 1", 10,"work","under construction");
//		
//	List<meeting> meetinglist=new ArrayList<>();
//	meetinglist.add(M);
//	List<Workstation> workstationlist=new ArrayList<>();
//	workstationlist.add(W);
//	Floor F=new Floor(11,8,1,"construction",1,"2nd-Floor",8,"enable",workstationlist,"Automated Workstation",meetinglist);
//	List<Floor> floorlist =new ArrayList<>();
//	floorlist.add(F);
//	Building B=new Building(1,"Main",68,2,2,68,floorlist,"enable","Automated Workstation","MY WISH");
//	List<Building> build=new ArrayList<>();
//	build.add(B);
//	when(brepo.findAll()).thenReturn(build);
//	when(conrepo.findBuild(1, start_date)).thenReturn(count);
//	when(conrepo.findFloor(1,1,start_date)).thenReturn(count);
//	when(conrepo.findWorkstation(1,1,1, start_date)).thenReturn("manish.neelapu@nineleaps.com");
//	when(conrepo.findcountbuilding()).thenReturn(list);
//	when(conrepo.findcountfloor()).thenReturn(list);
//	when(conrepo.findcount(start_date, end_date)).thenReturn(list);
//	assertEquals(build,emailservice.getBuildingForRangeofDates(start_date, end_date));
//
//}
//COM
//@Test
//public void find_building_detailsTest() {
//	String start_date="2022-04-29";
//	String end_date="2022-04-29";
//	int count=1;
//	List<Integer> list= new ArrayList<>();
//	list.add(1);
//	Workstation W=new Workstation(111,true,"enable","MY WISH");
//	meeting M=new meeting(111,false, "enable", "Meeting Room 1", 10,"work","under construction");
//	List<meeting> meetinglist=new ArrayList<>();
//	meetinglist.add(M);
//	List<Workstation> workstationlist=new ArrayList<>();
//	workstationlist.add(W);
//	Floor F=new Floor(11,8,1,"construction",1,"2nd-Floor",8,"enable",workstationlist,"Automated Workstation",meetinglist);
//	List<Floor> floorlist =new ArrayList<>();
//	floorlist.add(F);
//	Building B=new Building(1,"Main",68,2,2,68,floorlist,"enable","Automated Workstation","MY WISH");
//	List<Building> build=new ArrayList<>();
//	build.add(B);
//	when(brepo.findAll()).thenReturn(build);
//	when(conrepo.findBuild(1,start_date)).thenReturn(count);
//	when(conrepo.findFloor(1,1,start_date)).thenReturn(count);
//	when(conrepo.findWorkstation(1,1,1, start_date)).thenReturn("manish.neelapu@nineleaps.com");
//	assertEquals(build,emailservice.find_building_details(start_date));
//}

//
//	 @SuppressWarnings("unchecked")
//		@Test
//		    @ExtendWith(MockitoExtension.class)
//		    @MockitoSettings(strictness = Strictness.LENIENT)
//	public void convertExcelToListTest() throws IOException,FileNotFoundException{
//			 File F = new File( "F:/template.xlsx");
//			
//		 Row row=mock(Row.class);
//
//		 Cell cell=mock(Cell.class);
//		Iterator<Row> iterator=mock(Iterator.class);
//		Iterator<Cell> cells=mock(Iterator.class);
//		FileInputStream is=new 	FileInputStream(F);
//		 XSSFWorkbook workbook =mock(XSSFWorkbook.class);
//		 XSSFSheet sheet=mock(XSSFSheet.class);
//		when(workbook.getSheet("d1")).thenReturn(sheet);
//		when(sheet.iterator()).thenReturn(iterator);
//		//when(sheet.getPhysicalNumberOfRows()).thenReturn(4);
//		when(iterator.hasNext()).thenReturn(true);
//		when(iterator.next()).thenReturn(row);
//		when(row.iterator()).thenReturn(cells);
//		when(cells.hasNext()).thenReturn(true);
//	
//		when(cells.next()).thenReturn(cell);
//		when(erepo.getCountOfRecords("abc@nineleaps.com","Automated Workstation")).thenReturn(0);
//		help.convertExcelToList(is);
//		assertEquals(1,help.convertExcelToList(is).size());
//		 }

//	 @SuppressWarnings("unchecked")
//		@Test
//		    @ExtendWith(MockitoExtension.class)
//		    @MockitoSettings(strictness = Strictness.LENIENT)
//	public void convertExcelToListTest2() throws IOException,FileNotFoundException{
//			 File F = new File( "F:/testing.xlsx");
//		
//		 Row row=mock(Row.class);
//
//		 Cell cell=mock(Cell.class);
//		Iterator<Row> iterator=mock(Iterator.class);
//		Iterator<Cell> cells=mock(Iterator.class);
//		FileInputStream is=new 	FileInputStream(F);
//		 XSSFWorkbook workbook =mock(XSSFWorkbook.class);
//		 XSSFSheet sheet=mock(XSSFSheet.class);
//		when(workbook.getSheet("d1")).thenReturn(sheet);
//		when(sheet.iterator()).thenReturn(iterator);
//		//when(sheet.getPhysicalNumberOfRows()).thenReturn(4);
//		when(iterator.hasNext()).thenReturn(true);
//		when(iterator.next()).thenReturn(row);
//		when(row.iterator()).thenReturn(cells);
//		when(cells.hasNext()).thenReturn(true).thenReturn(false);
//		when(cells.next()).thenReturn(cell);
//		help.convertExcelToList(is);
//		assertEquals(1,help.convertExcelToList(is).size());
//		 }

}


