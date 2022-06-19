package com.Service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.exception.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.Dao.Page;
import com.Dao.Pagecount;
import com.Dao.TeamMeetingDetails;
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
import com.Repository.meetingRepo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
//import org.springframework.messaging.MessagingException;
//import static com.Model.User.email;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ServiceImpl implements ServiceInterface {
    @Autowired
    EmailRepository emailRepository;

    @Autowired
    ConfirmationRepo confirmationRepo;

    @Autowired
    ConfirmationRepo crepo;
    @Autowired
    Buildrepo brepo;
    @Autowired
    Floorrepo frepo;
    @Autowired
    Workstationrepo repo;
    @Autowired
    Workstationrepo wrepo;

    @Autowired
    EmailRepository erepo;
    @Autowired
    meetingRepo mrepo;
    @Autowired
    conf_confirmationrepo meetingRepo;
  
    @Autowired
    private JavaMailSender mailSender;

    public ServiceImpl(Buildrepo brepo, Floorrepo frepo, Workstationrepo wrepo, ConfirmationRepo crepo, EmailRepository emailRepository, meetingRepo mrepo, conf_confirmationrepo cnfRepo, JavaMailSender mailSender) {
        this.brepo = brepo;
        this.frepo = frepo;
        this.wrepo = wrepo;
        this.crepo = crepo;
        this.emailRepository = emailRepository;
        this.erepo = emailRepository;
       
        this.mrepo = mrepo;
        this.meetingRepo = cnfRepo;
 
        this.mailSender = mailSender;
    }
    LocalDate localDate = LocalDate.now();
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String current_date = localDate.format(formatter);

    @Override
    public String findAll(String email) {
        User u = this.emailRepository.findByEmail(email);
        if (u == null) {
            throw new ResourceNotFoundException("User Not Found");
        } else {
//
            Confirmation confirm = new Confirmation();
            confirm.setEmail(email);
            confirmationRepo.save(confirm);
            return u.getRole();
        }
    }

    @Override
    public String findRole(String email) {
        User user = this.emailRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User Not Found");
        } else {
//
            return user.getRole();
        }
    }
    public void edit2(@RequestBody Dummy_Confirm conf) {
        String[] str=new String[100];
        int[] wnum=new int[100];
        String date,date2;
        int[] flag=new int[10];
        System.out.println("\n starting:"+conf.getChangedDates().get(0));
        int len=conf.getLdates().size();
        for(int i=0;i<len;i++) {
            if(conf.getLdates().get(i).equalsIgnoreCase(conf.getChangedDates().get(i))) {
                continue;
            }
            else {
                flag[0]=1;
            }
        }

        if(flag[0]==1) {
            System.out.println("\n There is a change in dates");
            for(int dt=0;dt<conf.getLdates().size();dt++) {
                date=conf.getLdates().get(dt);
                for(int h=0;h<conf.getEmail().size();h++) {
                    str[h]=conf.getEmail().get(h);
                    wnum[h]=conf.getWorkstation_id().get(h);
                    if(crepo.getOnDateCount(str[h], date)==0) {
                        System.out.println("\n There is no entry  in database");
                        continue;
                    }
                    date2=conf.getChangedDates().get(dt);
                    System.out.println("\n mail:"+str[h]);
                    System.out.println("\n wnum:"+wnum[h]);
                    System.out.println("\n date:"+date);
                    int m=wnum[h];
                    int num=m;
                    int i;
                    String s= Integer.toString(m);
                    int l=s.length();
                    int[] arr=new int[l];
                    if(l==3){
                        for (i = 0; i < l-1; i++) {
                            num = num / 10;
                            arr[i] = num;
                        }
                    }
                    else{
                        num=num/10;
                        for (i = 0; i < l-2; i++) {
                            num = num / 10;
                            arr[i] = num;
                        }
                    }
                    int flnum=arr[0];
                    int bdnum=arr[1];
                    System.out.println("building " +bdnum+ "and floor :"+flnum);
                    String flname=frepo.getFloorname(flnum);
                    String bdname=brepo.getBuildingname(bdnum);
                    System.out.println("\n building name " +bdname+ " and floor name :"+flname);
                    int m1=0;
                    if(wnum[h]<1000) {
                        m1 = wnum[h] % 10;
                    }
                    else{
                        m1 = wnum[h] % 100;
                    }
                    System.out.println("\n wid is:"+m1);
                    int fln2=flnum%10;
                    System.out.println("\n new floor is:"+fln2);
                    crepo.newUpdate(bdnum, bdname, flname, fln2,m1,date2,str[h],date);
                }
            }

        }
        else {
            System.out.println("\n There is a change in dates");
            for(int dt=0;dt<conf.getLdates().size();dt++) {
                date=conf.getLdates().get(dt);
                for(int h=0;h<conf.getEmail().size();h++) {
                    str[h]=conf.getEmail().get(h);
                    wnum[h]=conf.getWorkstation_id().get(h);
                    String name=erepo.findname(str[h]);
                    System.out.println("\n name:"+name);
                    System.out.println("\n mail:"+str[h]);
                    System.out.println("\n wnum:"+wnum[h]);
                    System.out.println("\n date:"+date);
                    int m=wnum[h];
                    int num=m;
                    int i;
                    String s= Integer.toString(m);
                    int l=s.length();
                    int[] arr=new int[l];
                    if(l==3){
                        for (i = 0; i < l-1; i++) {
                            num = num / 10;
                            arr[i] = num;
                        }
                    }
                    else{
                        num=num/10;
                        for (i = 0; i < l-2; i++) {
                            num = num / 10;
                            arr[i] = num;
                        }
                    }
                    int flnum=arr[0];
                    int bdnum=arr[1];
                    System.out.println("building " +bdnum+ "and floor :"+flnum);
                    String flname=frepo.getFloorname(flnum);
                    String bdname=brepo.getBuildingname(bdnum);
                    System.out.println("\n building name " +bdname+ " and floor name :"+flname);
                    int m1=0;
                    if(wnum[h]<1000) {
                        m1 = wnum[h] % 10;
                    }
                    else{
                        m1 = wnum[h] % 100;
                    }
                    System.out.println("\n wid is:"+m1);
                    int fln2=flnum%10;
                    System.out.println("\n new floor is:"+fln2);
                    crepo.bupdate(bdnum,str[h],date);
                    crepo.fupdate(fln2,str[h],date);
                    crepo.wupdate(m1,str[h],date);
                    crepo.bdnameupdate(bdname,str[h],date);
                    crepo.flnameupdate(flname,str[h],date);
                }

            }
        }
    }

    @Override
    public User findById(int id) {
        Optional<User> WorkStation = this.emailRepository.findById(id);

        if (WorkStation.isPresent()) {
            return WorkStation.get();
        } else {
            throw new ResourceNotFoundException("Record not found with id : " + id);
        }
    }

    @Override
    public List<String> findProject(String email) {

        User user = this.emailRepository.findByEmail(email);
        int user_id = user.getId();
        List<String> pro = this.emailRepository.findByManager_name(email);
        return pro;
    }



    @Override
    public List<User> findEmployee(String pro, String sdate, String edate) { //Automated Workstation,2022-03-24
        System.out.println(pro);
        System.out.println(sdate);
        System.out.println(edate);
        List<User> emp = this.emailRepository.findByProjectName(pro);
        System.out.print(emp);
        for (int j = 0; j < emp.size(); j++) {
            int count = this.confirmationRepo.findBooked(emp.get(j).getEmail(), sdate, edate);
            if (count > 0) {
                emp.get(j).setIsbooked(true);
            }
        }
        return emp;
    }

    @Override
    public List<String> find_projects_id() {
    	System.out.println("\n BEFORE AUTHENTICATE");
        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String email = obj.getName();
        System.out.println("\n email is:"+email);
        List<String> al = emailRepository.findByEmails(email);
        List<String> al1 = new ArrayList<String>();
        System.out.println("\n BEFORE CUSER");
        User cuser = erepo.finduser(email);
        System.out.println("\n AFTER CUSER");
        if (cuser.getRole().equalsIgnoreCase("HR")) {
            return (List<String>) (emailRepository.findProject());
        }
//        al.forEach((temp) -> {
//            al1.add(temp.getProject_name());
//        });
        //System.out.println(al1);
        return al;
        		
    }

    @Override
    public List<User> find_members(String project_name) {
        List<User> al = emailRepository.findByProjectName(project_name);
        List<String> al1 = new ArrayList<String>();
        al.forEach((temp) -> {
            al1.add(temp.getName());
        });
        System.out.print(al1);
        return al;


    }

    public ResponseEntity<String> findAllnew() {
        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String email = obj.getName();


        User user = emailRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundException();
        String U = user.getRole();
        return new ResponseEntity<String>(U, HttpStatus.OK);
    }

    @Override
    public List<Building> find_building_details(String date) {


        List<Building> build = (List<Building>) this.brepo.findAll();
        for (int i = 0; i < build.size(); i++) {
            int count_build = this.crepo.findBuild(build.get(i).get_id(), date);
            //			System.out.println(build.get(i).getBdstate().equals("enable"));
            if (build.get(i).getBuilding_state().equals("enable")) {
                build.get(i).setNo_of_workstations(build.get(i).getTotal_no_of_workstations() - count_build);
            } else {
                build.get(i).setNo_of_workstations(0);
                //				confirmationRepo.deleteBEntry((build.get(i).get_id()));
            }
            List<Floor> floor = build.get(i).getFloors();
            for (int j = 0; j < floor.size(); j++) {
                int count_floor = this.crepo.findFloor(build.get(i).get_id(), (floor.get(j).getFloor_no() % 10), date);
                if (floor.get(j).getFloor_state().equals("enable")) {
                    floor.get(j).setNo_of_workstations(floor.get(j).getTotal_no_of_workstations() - count_floor);
                } else {
                    floor.get(j).setNo_of_workstations(0);
                    //					this.confirmationRepo.deleteFEntry((floor.get(j).getFloor_no())%10);
                    build.get(i).setNo_of_workstations(build.get(i).getNo_of_workstations() - floor.get(j).getTotal_no_of_workstations());
                }
                List<Workstation> work = floor.get(j).getWorkstations();
                for (int k = 0; k < work.size(); k++) {
                    int id = work.get(k).getNumber();
                    if (id < 1000) {
                        id = id % 10;
                    } else {
                        id = id % 100;
                    }
                    String w_email = this.crepo.findWorkstation(build.get(i).get_id(), (floor.get(j).getFloor_no() % 10), id, date);
                    //					System.out.println(w_id);
                    if (w_email != null) {
                        work.get(k).setState(true);
                    }
                }
            }
        }
        return (List<Building>) brepo.findAll();
    }
//    @Override
//    public ResponseEntity<Object> confirm(Dummy_Confirm dummy_conf) {
//
//        String n = "";
//        if (dummy_conf.getEmail() == null || dummy_conf.getDate() == null ||
//                dummy_conf.getBuilding_id() == 0 || dummy_conf.getFloor_id() == 0 ||
//                dummy_conf.getWorkstation_id() == null) throw new DataNotFoundException();
//
//        String status = "";
//        LocalDate startDate = LocalDate.parse(dummy_conf.getDate(), formatter);
//        LocalDate endDate = LocalDate.parse(dummy_conf.getDate_end(),
//                formatter);
//        System.out.println(startDate);
//        System.out.println("end date is :" + endDate);
//        List<String> email = new ArrayList<>();
//        String var = "";
//        endDate = endDate.plusDays(1);
//        for (LocalDate
//             date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
//
//            var = formatter.format(date);
//            System.out.println("\n end date inside :" + endDate);
//            System.out.println("\n date is :" + var);
//int w1;
//
//            for (int i = 0; i < dummy_conf.getEmail().size(); i++) {
//                if (dummy_conf.getWorkstation_id().get(i) < 1000) {
//
//                    w1= dummy_conf.getWorkstation_id().get(i) % 10;
//                } else {
//
//                    w1=dummy_conf.getWorkstation_id().get(i) % 100;
//                }
//                Authentication obj2 = SecurityContextHolder.getContext().getAuthentication();
//                String mail2 = obj2.getName();
//                String crole=erepo.getRole(mail2);
//                int count = crepo.findUsingData(var,dummy_conf.getBuilding_id(),dummy_conf.getFloor_id()%10,w1);
//                if(count!=0 && crole.equalsIgnoreCase("HR")){
//                    Authentication obj = SecurityContextHolder.getContext().getAuthentication();
//                    String mail = obj.getName();
//                    User cuser=erepo.finduser(mail);
//                    //  String name=erepo.findname(cuser.getEmail());
//                    LocalDate localstartDate = LocalDate.parse(dummy_conf.getDate(),formatter);
//                    LocalDate localendDate = LocalDate.parse(dummy_conf.getDate_end(),formatter);
//                    String end=formatter.format(localendDate);
//                    localendDate=localendDate.plusDays(1);
//                    int f1=0,b1=0,wr=0;
//                    if(localendDate.compareTo(localstartDate)<0)
//                    {
//                        throw new InvalidDateException();
//                    }
//                    String sdate="";
//                    String edate="";
//                    String start=formatter.format(localstartDate);
//                    for(int k=0;k<dummy_conf.getWorkstation_id().size();k++)
//                    {
//                        sdate = formatter.format(localstartDate);
//                        edate=formatter.format(localendDate);
//                        //2122
//                        String s=Integer.toString(dummy_conf.getWorkstation_id().get(k));
//                        char b=s.charAt(0);
//                        b1=Character.getNumericValue(b);
//                        char f=s.charAt(1);
//                        f1=Character.getNumericValue(f);
//                        //   System.out.print(c);
//                        String w=s.substring(2,s.length());
//                        w1=Integer.parseInt(w);
//                        List<String> emails=crepo.findDistinctEmails(w1,f1,b1);
//                        for(int j=0;j<emails.size();j++)
//                        {
//                            String name=erepo.findname(emails.get(j)) ;
//                            try {
//                                sendEmailtouserWhenOverridingWorkstation (emails.get(j),name , b1,f1 ,w1,dummy_conf.getDate(),dummy_conf.getDate_end(),cuser.getEmail());
//                            } catch (Exception e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    crepo.deleteDate(var,dummy_conf.getBuilding_id(),dummy_conf.getFloor_id()%10,w1);
//                }
//                String name = dummy_conf.getEmail().get(i);
//                System.out.println("name is:" + name);
//                status = dummy_conf.getEmail().get(i);
//                System.out.println("status is:" + status);
//
//                String role = erepo.getRole(status);
//                System.out.println("role is:" + role);
//
//                if (role.equalsIgnoreCase("HR")) {
//                    System.out.println("\n inside hr");
//                    System.out.println(dummy_conf.getWorkstation_id().size());
//                    int dummy_size = dummy_conf.getWorkstation_id().size();
//
//                    for (int j = 0; j < dummy_conf.getWorkstation_id().size(); j++) {
//                        Confirmation c1 = new Confirmation();
//                        int val = crepo.findDuplicate(status,var);
//                        if(val!=0){
//                            crepo.deleteDuplicate(status,var);
//                        }
//                        Confirmation cn = crepo.checkexisiting(status, var);
//                        if (cn != null) throw new UserAlreadyExistsException();
//                        System.out.println("i val is" + i);
//
//                        System.out.println("email:" + dummy_conf.getEmail().get(i));
//                        c1.setEmail(dummy_conf.getEmail().get(i));
//                        c1.setDate(var);
//                        c1.setBuilding_id(dummy_conf.getBuilding_id());
//                        int build_id = dummy_conf.getBuilding_id();
//                        String build = this.brepo.findId(build_id);
//                        c1.setBuilding_name(build);
//                        c1.setFloor_id(dummy_conf.getFloor_id() % 10);
//                        int floor_id = dummy_conf.getFloor_id();
//                        String floor = this.frepo.findFId(floor_id);
//                        c1.setFloor_name(floor);
//
//                        String nm = dummy_conf.getEmail().get(i);
//                        n = this.erepo.findname(nm);
//                        System.out.println("name is:" + n);
//                        c1.setName(n);
//
//                        if (dummy_conf.getWorkstation_id().get(j) < 1000) {
//
//                            c1.setWorkstation_id(dummy_conf.getWorkstation_id().get(j) % 10);
//                        } else {
//
//                            c1.setWorkstation_id(dummy_conf.getWorkstation_id().get(j) % 100);
//                        }
//                        confirmationRepo.save(c1);
//                    }
//
//                } else {
//                    //email.add(dummy_conf.getEmail().get(i));
//                    Confirmation c = new Confirmation();
//                    int val = crepo.findDuplicate(status,var);
//                    if(val!=0){
//                        crepo.deleteDuplicate(status,var);
//                    }
//                    Confirmation cn = crepo.checkexisiting(status, var);
//                    if (cn != null) throw new UserAlreadyExistsException();
//                    c.setId(dummy_conf.getId());
//                    c.setDate(var);
//                    c.setEmail(dummy_conf.getEmail().get(i));
//                    email.add(dummy_conf.getEmail().get(i));
//                    c.setBuilding_id(dummy_conf.getBuilding_id());
//                    int build_id = dummy_conf.getBuilding_id();
//                    String build = this.brepo.findId(build_id);
//                    c.setBuilding_name(build);
//                    c.setFloor_id(dummy_conf.getFloor_id() % 10);
//                    int floor_id = dummy_conf.getFloor_id();
//                    String floor = this.frepo.findFId(floor_id);
//                    c.setFloor_name(floor);
//
//                    String nm = dummy_conf.getEmail().get(i);
//                    n = this.erepo.findname(nm);
//                    System.out.println("name is:" + n);
//                    c.setName(n);
//
//                    if (dummy_conf.getWorkstation_id().get(i) < 1000) {
//                        c.setWorkstation_id(dummy_conf.getWorkstation_id().get(i) %
//                                10);
//                    } else {
//                        c.setWorkstation_id(dummy_conf.getWorkstation_id().get(i) %
//                                100);
//                    }
//                    confirmationRepo.save(c);
//                }
//            }
//        }
//
//
//        Object obj =
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String mail = ((userDetailsImpl) obj).getUsername();
//        User cuser = erepo.finduser(mail);
//        if (cuser.getRole().equalsIgnoreCase("Project manager") || cuser.getRole().equalsIgnoreCase("HR")) {
//            for (int i = 0; i < email.size(); i++) {
//                String n1 = erepo.findname(email.get(i));
//                try {
//                    sendEmailtouser1(email.get(i), n1,
//                            dummy_conf.getWorkstation_id().get(i), dummy_conf.getDate(), dummy_conf.getDate_end(), mail);
//                } catch (Exception e) {
//
//                }
//            }
//        }
//        return new ResponseEntity<>("Booking successful", HttpStatus.OK);
//    }
    
//    
//    @Override
//    public ResponseEntity<Object> confirm(Dummy_Confirm dummy_conf) {
//
//        String n = "";
//        if (dummy_conf.getEmail() == null || dummy_conf.getDate() == null ||
//                dummy_conf.getBuilding_id() == 0 || dummy_conf.getFloor_id() == 0 ||
//                dummy_conf.getWorkstation_id() == null) throw new DataNotFoundException();
//
//        String status = "";
//        LocalDate startDate = LocalDate.parse(dummy_conf.getDate(), formatter);
//        LocalDate endDate = LocalDate.parse(dummy_conf.getDate_end(),
//                formatter);
//        System.out.println(startDate);
//        System.out.println("end date is :" + endDate);
//        List<String> email = new ArrayList<>();
//        String var = "";
//        endDate = endDate.plusDays(1);
//        for (LocalDate
//             date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
//
//            var = formatter.format(date);
//            System.out.println("\n end date inside :" + endDate);
//            System.out.println("\n date is :" + var);
//int w1;
//
//            for (int i = 0; i < dummy_conf.getEmail().size(); i++) {
//                if (dummy_conf.getWorkstation_id().get(i) < 1000) {
//
//                    w1= dummy_conf.getWorkstation_id().get(i) % 10;
//                } else {
//
//                    w1=dummy_conf.getWorkstation_id().get(i) % 100;
//                }
//                System.out.println("\n 111111111111111111111:"+var+"\n"+dummy_conf.getBuilding_id()+"\n"+dummy_conf.getFloor_id()%10+"\n "+w1);
//                int count = crepo.findUsingData(var,dummy_conf.getBuilding_id(),dummy_conf.getFloor_id()%10,w1);
//               
//                System.out.println("\n The Count is:"+count);
//                if(count!=0){
//                    Authentication obj = SecurityContextHolder.getContext().getAuthentication();
//                    String mail = obj.getName();
//                    User cuser=erepo.finduser(mail);
//                    //  String name=erepo.findname(cuser.getEmail());
//                    LocalDate localstartDate = LocalDate.parse(dummy_conf.getDate(),formatter);
//                    LocalDate localendDate = LocalDate.parse(dummy_conf.getDate_end(),formatter);
//                    String end=formatter.format(localendDate);
//                    localendDate=localendDate.plusDays(1);
//                    int f1=0,b1=0,wr=0;
//                    if(localendDate.compareTo(localstartDate)<0)
//                    {
//                        throw new InvalidDateException();
//                    }
//                    String sdate="";
//                    String edate="";
//                    String start=formatter.format(localstartDate);
//                    for(int k=0;k<dummy_conf.getWorkstation_id().size();k++)
//                    {
//                        sdate = formatter.format(localstartDate);
//                        edate=formatter.format(localendDate);
//                        //2122
//                        String s=Integer.toString(dummy_conf.getWorkstation_id().get(k));
//                        char b=s.charAt(0);
//                        b1=Character.getNumericValue(b);
//                        char f=s.charAt(1);
//                        f1=Character.getNumericValue(f);
//                        //   System.out.print(c);
//                        String w=s.substring(2,s.length());
//                        w1=Integer.parseInt(w);
//                        List<String> emails=crepo.findDistinctEmails(w1,f1,b1);
//                        for(int j=0;j<emails.size();j++)
//                        {
//                            String name=erepo.findname(emails.get(j)) ;
//                            try {
//                                sendEmailtouserWhenOverridingWorkstation (emails.get(j),name , b1,f1 ,w1,dummy_conf.getDate(),dummy_conf.getDate_end(),cuser.getEmail());
//                            } catch (Exception e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    crepo.deleteDate(var,dummy_conf.getBuilding_id(),dummy_conf.getFloor_id()%10,w1);
//                }
//                String name = dummy_conf.getEmail().get(i);
//                System.out.println("name is:" + name);
//                status = dummy_conf.getEmail().get(i);
//                System.out.println("status is:" + status);
//
//                String role = erepo.getRole(status);
//                System.out.println("role is:" + role);
//
//                if (role.equalsIgnoreCase("HR")) {
//                    System.out.println("\n inside hr");
//                    System.out.println(dummy_conf.getWorkstation_id().size());
//                    int dummy_size = dummy_conf.getWorkstation_id().size();
//
//                    for (int j = 0; j < dummy_conf.getWorkstation_id().size(); j++) {
//                        Confirmation c1 = new Confirmation();
//                        int val = crepo.findDuplicate(status,var);
//                        if(val!=0){
//                            crepo.deleteDuplicate(status,var);
//                        }
//                        Confirmation cn = crepo.checkexisiting(status, var);
//                        if (cn != null) throw new UserAlreadyExistsException();
//                        System.out.println("i val is" + i);
//
//                        System.out.println("email:" + dummy_conf.getEmail().get(i));
//                        c1.setEmail(dummy_conf.getEmail().get(i));
//                        c1.setDate(var);
//                        c1.setBuilding_id(dummy_conf.getBuilding_id());
//                        int build_id = dummy_conf.getBuilding_id();
//                        String build = this.brepo.findId(build_id);
//                        c1.setBuilding_name(build);
//                        c1.setFloor_id(dummy_conf.getFloor_id() % 10);
//                        int floor_id = dummy_conf.getFloor_id();
//                        String floor = this.frepo.findFId(floor_id);
//                        c1.setFloor_name(floor);
//
//                        String nm = dummy_conf.getEmail().get(i);
//                        n = this.erepo.findname(nm);
//                        System.out.println("name is:" + n);
//                        c1.setName(n);
//
//                        if (dummy_conf.getWorkstation_id().get(j) < 1000) {
//
//                            c1.setWorkstation_id(dummy_conf.getWorkstation_id().get(j) % 10);
//                        } else {
//
//                            c1.setWorkstation_id(dummy_conf.getWorkstation_id().get(j) % 100);
//                        }
//                        confirmationRepo.save(c1);
//                    }
//
//                } else {
//                    //email.add(dummy_conf.getEmail().get(i));
//                    Confirmation c = new Confirmation();
//                    int val = crepo.findDuplicate(status,var);
//                    if(val!=0){
//                        crepo.deleteDuplicate(status,var);
//                    }
//                    Confirmation cn = crepo.checkexisiting(status, var);
//                    if (cn != null) throw new UserAlreadyExistsException();
//                    c.setId(dummy_conf.getId());
//                    c.setDate(var);
//                    c.setEmail(dummy_conf.getEmail().get(i));
//                    email.add(dummy_conf.getEmail().get(i));
//                    c.setBuilding_id(dummy_conf.getBuilding_id());
//                    int build_id = dummy_conf.getBuilding_id();
//                    String build = this.brepo.findId(build_id);
//                    c.setBuilding_name(build);
//                    c.setFloor_id(dummy_conf.getFloor_id() % 10);
//                    int floor_id = dummy_conf.getFloor_id();
//                    String floor = this.frepo.findFId(floor_id);
//                    c.setFloor_name(floor);
//
//                    String nm = dummy_conf.getEmail().get(i);
//                    n = this.erepo.findname(nm);
//                    System.out.println("name is:" + n);
//                    c.setName(n);
//
//                    if (dummy_conf.getWorkstation_id().get(i) < 1000) {
//                        c.setWorkstation_id(dummy_conf.getWorkstation_id().get(i) %
//                                10);
//                    } else {
//                        c.setWorkstation_id(dummy_conf.getWorkstation_id().get(i) %
//                                100);
//                    }
//                    confirmationRepo.save(c);
//                }
//            }
//        }
//
//
//        Object obj =
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String mail = ((userDetailsImpl) obj).getUsername();
//        User cuser = erepo.finduser(mail);
//        if (cuser.getRole().equalsIgnoreCase("Project manager") || cuser.getRole().equalsIgnoreCase("HR")) {
//            for (int i = 0; i < email.size(); i++) {
//                String n1 = erepo.findname(email.get(i));
//                try {
//                    sendEmailtouser1(email.get(i), n1,
//                            dummy_conf.getWorkstation_id().get(i), dummy_conf.getDate(), dummy_conf.getDate_end(), mail);
//                } catch (Exception e) {
//
//                }
//            }
//        }
//        return new ResponseEntity<>("Booking successful", HttpStatus.OK);
//    }
//    

    
  
    
    public ResponseEntity<Object> confirm(Dummy_Confirm dummy_conf)
    {
    	 Date fdate;
     	Date fdate2;

     	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");    
    	
    	String n = "";
            if (dummy_conf.getEmail() == null || dummy_conf.getDate() == null ||
                    dummy_conf.getBuilding_id() == 0 || dummy_conf.getFloor_id() == 0 ||
                    dummy_conf.getWorkstation_id() == null)
                throw new DataNotFoundException();
            String status = "";
            int w1;
            LocalDate startDate = LocalDate.parse(dummy_conf.getDate(), formatter);
            LocalDate endDate = LocalDate.parse(dummy_conf.getDate_end(),formatter);
            System.out.println("end date is :" + endDate);
            List<String> email = new ArrayList<>();
            String var = "";
            endDate = endDate.plusDays(1);
            int times=0;
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                var = formatter.format(date);
                System.out.println("\n end date inside :" + endDate);
                System.out.println("\n date is :" + var);
             // times++;
                for (int i = 0; i < dummy_conf.getEmail().size(); i++) {
                    if (dummy_conf.getWorkstation_id().get(i) < 1000) {
                        w1= dummy_conf.getWorkstation_id().get(i) % 10;
                    } else {
                        w1=dummy_conf.getWorkstation_id().get(i) % 100;
                    }
                    int count = crepo.findUsingData(var,dummy_conf.getBuilding_id(),dummy_conf.getFloor_id()%10,w1);
                    System.out.println("\n Count Value is:"+count);
                    if(count!=0){
                        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
                        String mail = obj.getName();
                        User cuser=erepo.finduser(mail);
                        System.out.println("\n The Role inside overide block:"+cuser.role);
                        
                        if(cuser.role.equalsIgnoreCase("HR")||cuser.role.equalsIgnoreCase("project manager"))
                        {
                        	System.out.println("\n overrided because role is:"+cuser.role);
                        //  String name=erepo.findname(cuser.getEmail());
                        LocalDate localstartDate = LocalDate.parse(dummy_conf.getDate(),formatter);
                        LocalDate localendDate = LocalDate.parse(dummy_conf.getDate_end(),formatter);
                        String end=formatter.format(localendDate);
                        localendDate=localendDate.plusDays(1);
                        int f1=0,b1=0,wr=0;
                        if(localendDate.compareTo(localstartDate)<0)
                        {
                            throw new InvalidDateException();
                        }
                        String sdate="";
                        String edate="";
                        String start=formatter.format(localstartDate);
                        for(int k=0;k<dummy_conf.getWorkstation_id().size();k++)
                        {
                            sdate = formatter.format(localstartDate);
                            edate=formatter.format(localendDate);
                            //2122
                            String s=Integer.toString(dummy_conf.getWorkstation_id().get(k));
                            char b=s.charAt(0);
                            b1=Character.getNumericValue(b);
                            char f=s.charAt(1);
                            f1=Character.getNumericValue(f);
                            String w=s.substring(2,s.length());
                            w1=Integer.parseInt(w);
                            List<Confirmation> emails=crepo.findBookings(w1,f1,b1,var);
                            System.out.println("emailssss"+emails);
                            for(int j=0;j<emails.size();j++)
                            {
                                String name=erepo.findname(emails.get(j).getEmail()) ;
                                try {
                                    if(j==0){
                                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(emails.get(j).getDate());
                                    	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(dummy_conf.getDate_end());
                                        String finalDate=formates2.format(fdate);
                                        String finalDate2=formates2.format(fdate2);
                                    sendEmailtouserWhenOverridingWorkstation (emails.get(j).getEmail(),name , b1,f1 ,w1,finalDate,finalDate2,cuser.getEmail());
                                    System.out.println("dddddddddddddd"+j);
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    System.out.println(e);
                                    e.printStackTrace();
                                }
                            }
                        }
                        crepo.deleteDate(var,dummy_conf.getBuilding_id(),dummy_conf.getFloor_id()%10,w1);
                    }
                }
               // for (int l = 0; l< dummy_conf.getEmail().size(); l++) {
                    String name = dummy_conf.getEmail().get(i);
                    System.out.println("name is:" + name);
                    status = dummy_conf.getEmail().get(i);
                    System.out.println("status is:" + status);
                    // }
                    String role = erepo.getRole(status);
                    System.out.println("role is:" + role);
                    if (role.equalsIgnoreCase("HR")) {
                        System.out.println("\n inside hr");
                        System.out.println(dummy_conf.getWorkstation_id().size());
                        int dummy_size = dummy_conf.getWorkstation_id().size();
                        for (int j = 0; j < dummy_conf.getWorkstation_id().size(); j++) {
                            Confirmation c1 = new Confirmation();
                            Confirmation cn = crepo.checkexisiting1(status, var);
                            if (cn != null) {
                                throw new UserAlreadyExistsException();
                            }
                            System.out.println("i val is" + i);
                            System.out.println("email:" + dummy_conf.getEmail().get(i));
                            c1.setEmail(dummy_conf.getEmail().get(i));
                            email.add(dummy_conf.getEmail().get(i));
                            c1.setDate(var);
                            c1.setBuilding_id(dummy_conf.getBuilding_id());
                            int build_id = dummy_conf.getBuilding_id();
                            String build = this.brepo.findId(build_id);
                            c1.setBuilding_name(build);
                            c1.setFloor_id(dummy_conf.getFloor_id() % 10);
                            int floor_id = dummy_conf.getFloor_id();
                            String floor = this.frepo.findFId(floor_id);
                            c1.setFloor_name(floor);
                            String nm = dummy_conf.getEmail().get(i);
                            n = this.erepo.findname(nm);
                            System.out.println("name is:" + n);
                            c1.setName(n);
                            if (dummy_conf.getWorkstation_id().get(j) < 1000) {
                                c1.setWorkstation_id(dummy_conf.getWorkstation_id().get(j) % 10);
                            } else {
                                c1.setWorkstation_id(dummy_conf.getWorkstation_id().get(j) % 100);
                            }
                            confirmationRepo.save(c1);
                        }
                    } else {
                        // email.add(dummy_conf.getEmail().get(i));
                        Confirmation c = new Confirmation();
                        Confirmation cn = crepo.checkexisiting1(status, var);
                        if (cn != null)
                            throw new UserAlreadyExistsException();
                        c.setId(dummy_conf.getId());
                        c.setDate(var);
                        c.setEmail(dummy_conf.getEmail().get(i));
                        email.add(dummy_conf.getEmail().get(i));
                        c.setBuilding_id(dummy_conf.getBuilding_id());
                        int build_id = dummy_conf.getBuilding_id();
                        String build = this.brepo.findId(build_id);
                        c.setBuilding_name(build);
                        c.setFloor_id(dummy_conf.getFloor_id() % 10);
                        int floor_id = dummy_conf.getFloor_id();
                        String floor = this.frepo.findFId(floor_id);
                        c.setFloor_name(floor);
                        String nm = dummy_conf.getEmail().get(i);
                        n = this.erepo.findname(nm);
                        System.out.println("name is:" + n);
                        c.setName(n);
                        if (dummy_conf.getWorkstation_id().get(i) < 1000) {
                            c.setWorkstation_id(dummy_conf.getWorkstation_id().get(i) %
                                    10);
                        } else {
                            c.setWorkstation_id(dummy_conf.getWorkstation_id().get(i) %
                                    100);
                        }
                        confirmationRepo.save(c);
                    }
               // }
            }
          }
            Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String mail = ((userDetailsImpl) obj).getUsername();
            User cuser = erepo.finduser(mail);
            System.out.println(cuser.getRole());
            if (cuser.getRole().equalsIgnoreCase("Project manager") || cuser.getRole().equalsIgnoreCase("HR")) {
                for (int k = 0; k < email.size(); k++) {
                    System.out.print("huu");
                    String n1 = erepo.findname(email.get(k));
                    try
                    {
                        //if(times==1)
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse( dummy_conf.getDate());
                    	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(dummy_conf.getDate_end());
                        String finalDate=formates2.format(fdate);
                        String finalDate2=formates2.format(fdate2);
                        sendEmailtouser1(email.get(k), n1,
                                dummy_conf.getWorkstation_id().get(k),finalDate, finalDate2,
                                mail);
                    } catch (Exception e) {
                    }
                }
            }
        
        return new ResponseEntity<>("Booking successful", HttpStatus.OK);
    }
    
    
    
    
    
    
    public ResponseEntity<String> updateMeetingRoom(int id, String reason) {
        Date fdate;
    	Date fdate2;

    	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        mrepo.disableRoom(id);
        mrepo.disableRoomSetReason(id, reason);

        String n1 = Integer.toString(id);
        char a = n1.charAt(0);
        int b = Character.getNumericValue(a);

        String nf = Integer.toString(id);
        char f = n1.charAt(1);
        int c = Character.getNumericValue(f);

        char r = n1.charAt(2);
        int room = Character.getNumericValue(r);
        //   char
        List<conf_confirmation> al = meetingRepo.findpersonbymeetingroom(b, c, room);

        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String mail = obj.getName();
        User cu = erepo.findByEmail(mail);
        if (cu.getRole().equalsIgnoreCase("HR")) {

            for (int i = 0; i < al.size(); i++) {
                try {
                	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(i).getStart_date());
                	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(i).getEnd_date());
                    String finalDate=formates2.format(fdate);
                    String finalDate2=formates2.format(fdate2);
                    sendEmailtouserforDisablingMeetingroom(al.get(i).getEmail(), al.get(i).getName(), al.get(i).getBuilding_id(), al.get(i).getFloor_id(), al.get(i).getConf_room(), finalDate, finalDate2, mail, reason);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < al.size(); i++) {
            meetingRepo.deletePerson(al.get(i).getEmail(), al.get(i).getBuilding_id(), al.get(i).getFloor_id(), al.get(i).getConf_room());
        }
        return null;
    }



    @Override
    public ResponseEntity<Object> removeBooking(Dummy_Confirm conf) {
        String[] str = new String[100];
        int[] wnum = new int[100];
        int m1, bdn, fln1;
        String date;
        System.out.println("\n starting:" + conf.getLdates().get(0));

        for (int dt = 0; dt < conf.getLdates().size(); dt++) {
            date = conf.getLdates().get(dt);

            for (int h = 0; h < conf.getEmail().size(); h++) {
                str[h] = conf.getEmail().get(h);
                if (crepo.getOnDateCount(str[h], date) == 0) {
                    continue;
                }
                //System.out.println("\n size of wst \t"+conf.getWorkstation_id().size()+"email size:"+conf.getEmail().size());
//     String date=conf.getDate();
                m1 = crepo.getWID(str[h], date);
                bdn = crepo.getbid(str[h], date);
                fln1 = crepo.getfid(str[h], date);
                String name = erepo.findname(str[h]);
                System.out.println("\n name:" + name);
                System.out.println("\n mail:" + str[h]);
                System.out.println("\n wnum:" + m1 + " bdn:" + bdn + " fln1:" + fln1);
                System.out.println("\n date:" + date);
                if (crepo.selectbyfid(bdn, fln1, m1, date) == null) throw new WorkstationNotFoundException();
                Authentication obj = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("\n object is:" + obj);
                String mail = (obj).getName();
                System.out.println("\n mail is:" + mail);
                User cuser = erepo.finduser(mail);
                System.out.println("\n user is:" + cuser.getEmail());
                Confirmation C = crepo.selectbyfid(bdn, fln1, m1, date);
                if (cuser.getRole().equalsIgnoreCase("project manager") || cuser.getRole().equalsIgnoreCase("HR")) {
                    String e = C.getEmail();//this will get the email of the deleted data
                    System.out.println("\n e:" + e);
                    String na = C.getName();
                    try {
                        sendEmailtouser2(e, na, bdn, fln1, m1, date, mail);
                        System.out.println("entered try ");
                    } catch (Exception e1) {
                        System.out.print("mail not sent");
                    }
                }
//        System.out.println(crepo.selectbyfid(bdn,fln1,m1,date));
                // System.out.println("dleeeee"+crepo.deletebyfid(bid, fid, wid, date));
                crepo.deletebyfid(bdn, fln1, m1, date);
            }
        }
        return new ResponseEntity<>("Workstation is deleted successfully", HttpStatus.OK);
    }


    @Override
    public List<Building> getsingleDayDetails(String date) {
        List<Building> build = (List<Building>) this.brepo.findAll();

        List<conf_confirmation> meet = (List<conf_confirmation>) this.meetingRepo.findAll();
        List<meeting> mr = new ArrayList<meeting>();
        List<Floor> floor = new ArrayList<Floor>();

        for (int i = 0; i < build.size(); i++) {


            int n = build.get(i).getTotal_no_of_meeting_rooms();

            build.get(i).setAvailable_no_of_meeting_rooms(n);
            floor = build.get(i).getFloors();
            for (int m = 0; m < floor.size(); m++) {
                mr = floor.get(m).getMeetings();
                for (int j = 0; j < mr.size(); j++) {
                    mr.get(j).setState(false);
                }
                floor.get(m).setAvailable_no_of_meeting_rooms(floor.get(m).getTotal_no_of_meeting_rooms());
            }


        }

        for (int i = 0; i < meet.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(meet.get(i).getStart_date());
                Date d2 = sdf.parse(meet.get(i).getEnd_date());
                Date d3 = sdf.parse(date);
                Boolean b = !(d3.before(d1) || d3.after(d2));
                if (b == true) {
                    System.out.println("within if");
                    int bid = meet.get(i).getBuilding_id();
                    int fid = meet.get(i).getFloor_id();
                    int mid = meet.get(i).getConf_room();
                    String fs = Integer.toString(bid) + Integer.toString(fid);
                    int f = Integer.parseInt(fs);

                    System.out.println(bid);
                    //System.out.println(fid);
                    System.out.println(fs);
                    bid = bid - 1;
                    fid = fid - 1;
                    mid = mid - 1;
                    if (build.get(bid).getBuilding_state().equals("enable")) {
                        System.out.println("hiiiiiiiiiiiiiiiiii");
                        build.get(bid).setAvailable_no_of_meeting_rooms(build.get(bid).getAvailable_no_of_meeting_rooms() - 1);
                    } else {
                        build.get(bid).setAvailable_no_of_meeting_rooms(0);

                    }


                    if (build.get(bid).getFloors().get(fid).getFloor_state().equals("enable"))
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(build.get(bid).getFloors().get(fid).getAvailable_no_of_meeting_rooms() - 1);

                    else {
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(0);
                    }

                    build.get(bid).getFloors().get(fid).getMeetings().get(mid).setState(true);

                } else {
                    System.out.println("not within");
                }
            } catch (Exception e) {

            }
        }

        return build;

    }


    @Override
    public List<Building> getsingleDayDetailswithTime(String date, String stime, String etime) {
        List<Building> build = (List<Building>) this.brepo.findAll();

        List<conf_confirmation> meet = (List<conf_confirmation>) this.meetingRepo.findAll();
        List<meeting> mr = new ArrayList<meeting>();
        List<Floor> floor = new ArrayList<Floor>();

        for (int i = 0; i < build.size(); i++) {


            int n = build.get(i).getTotal_no_of_meeting_rooms();

            build.get(i).setAvailable_no_of_meeting_rooms(n);
            floor = build.get(i).getFloors();
            for (int m = 0; m < floor.size(); m++) {
                mr = floor.get(m).getMeetings();
                for (int j = 0; j < mr.size(); j++) {
                    mr.get(j).setState(false);
                }
                floor.get(m).setAvailable_no_of_meeting_rooms(floor.get(m).getTotal_no_of_meeting_rooms());
            }


        }

        for (int i = 0; i < meet.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(meet.get(i).getStart_date());
                Date d2 = sdf.parse(meet.get(i).getEnd_date());
                Date d3 = sdf.parse(date);
                Boolean b = !(d3.before(d1) || d3.after(d2));
                Boolean b1 = TimerangesCollideornot(meet.get(i).getStart_time(), meet.get(i).getEnd_time(), stime, etime);
                System.out.println(b);
                System.out.println(b1);
                if (b == true && b1 == true) {
                    System.out.println("within if");
                    int bid = meet.get(i).getBuilding_id();
                    int fid = meet.get(i).getFloor_id();
                    int mid = meet.get(i).getConf_room();
                    String fs = Integer.toString(bid) + Integer.toString(fid);
                    int f = Integer.parseInt(fs);

                    System.out.println(bid);
                    //System.out.println(fid);
                    System.out.println(fs);
                    bid = bid - 1;
                    fid = fid - 1;
                    mid = mid - 1;
                    if (build.get(bid).getBuilding_state().equals("enable")) {
                        System.out.println("hiiiiiiiiiiiiiiiiii");
                        build.get(bid).setAvailable_no_of_meeting_rooms(build.get(bid).getAvailable_no_of_meeting_rooms() - 1);
                    } else {
                        build.get(bid).setAvailable_no_of_meeting_rooms(0);

                    }


                    if (build.get(bid).getFloors().get(fid).getFloor_state().equals("enable"))
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(build.get(bid).getFloors().get(fid).getAvailable_no_of_meeting_rooms() - 1);

                    else {
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(0);
                    }
                    build.get(bid).getFloors().get(fid).getMeetings().get(mid).setState(true);

                } else {
                    System.out.println("not within");
                }
            } catch (Exception e) {

            }
        }

        return build;

    }

@Override
public List<Page> data(String sdate, String edate,int o,int s){
	Date fdate;
	try {
    Authentication obj = SecurityContextHolder.getContext().getAuthentication();
    String mail = obj.getName();
    List<Confirmation> a = crepo.findAllByDate(sdate, edate);
    List<Confirmation> b = new ArrayList<>();
    SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
    for(int j=0;j<a.size();j++) {
        if (!a.get(j).getEmail().equals(mail)){
            b.add(a.get(j));
        }
    }
    System.out.println("\n B VALUE:"+a.size()+"\n The A records"+a);
    int recordPerPage=s;
    int skipCount=(o)*recordPerPage;
    List<Confirmation> conf=b.stream().skip(skipCount).limit(recordPerPage).collect(Collectors.toList());
   // Page<Confirmation> page=new Page<>(2,b.size(),conf);
    System.out.println("\n conf VALUE:"+conf);
    List<Page> P=new ArrayList<>();
    for(int i=0;i<conf.size();i++) {
    	Page pg=new Page();
    	pg.setBuilding_id(conf.get(i).getBuilding_id());
    	pg.setBuilding_name(conf.get(i).getBuilding_name());
    	pg.setFloor_id(conf.get(i).getFloor_id());
    	pg.setFloor_name(conf.get(i).getFloor_name());
    	pg.setId(conf.get(i).getId());
    	 fdate=new SimpleDateFormat("yyyy-MM-dd").parse(b.get(i).getDate());
	        String finalDate=formates2.format(fdate);
	        System.out.println("\n The Date :"+b.get(i).getDate()+"\n The Changed Date:"+finalDate);         
	        pg.setDate(finalDate);
    	pg.setEmail(conf.get(i).getEmail());
    	pg.setWorkstation_id(conf.get(i).getWorkstation_id());
    	pg.setName(conf.get(i).getName());
    	pg.setTotalrecords(b.size());
    	P.add(pg);
    	}
    return P;
} catch (ParseException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return null;
}


@Override
public List<Page> data(String sdate, String edate){
	Date fdate;
	try {
    Authentication obj = SecurityContextHolder.getContext().getAuthentication();
    String mail = obj.getName();
    List<Confirmation> a = crepo.findAllByDate(sdate, edate);
    List<Confirmation> b = new ArrayList<>();
    SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
    
    for(int j=0;j<a.size();j++) {
        if (!a.get(j).getEmail().equals(mail)){
            b.add(a.get(j));
        }
    }
    System.out.println("\n B VALUE:"+a.size()+"\n The A records"+a);
    List<Page> P=new ArrayList<>();
    for(int i=0;i<b.size();i++) {
    	Page pg=new Page();
    	pg.setBuilding_id(b.get(i).getBuilding_id());
    	pg.setBuilding_name(b.get(i).getBuilding_name());
    	pg.setFloor_id(b.get(i).getFloor_id());
    	pg.setFloor_name(b.get(i).getFloor_name());
    	pg.setId(b.get(i).getId());
    	 fdate=new SimpleDateFormat("yyyy-MM-dd").parse(b.get(i).getDate());
	        String finalDate=formates2.format(fdate);
	        System.out.println("\n The Date :"+b.get(i).getDate()+"\n The Changed Date:"+finalDate);         
	        pg.setDate(finalDate);
    	pg.setEmail(b.get(i).getEmail());
    	pg.setWorkstation_id(b.get(i).getWorkstation_id());
    	pg.setName(b.get(i).getName());
    	pg.setTotalrecords(b.size());
    	P.add(pg);
    	}
    return P;
} catch (ParseException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return null;
}



    @Override
    public List<Building> getmultipleDayDetails(String sdate, String edate) {
        List<Building> build = (List<Building>) this.brepo.findAll();

        List<conf_confirmation> meet = (List<conf_confirmation>) this.meetingRepo.findAll();
        List<meeting> mr = new ArrayList<meeting>();
        List<Floor> floor = new ArrayList<Floor>();

        for (int i = 0; i < build.size(); i++) {


            int n = build.get(i).getTotal_no_of_meeting_rooms();

            build.get(i).setAvailable_no_of_meeting_rooms(n);
            floor = build.get(i).getFloors();
            for (int m = 0; m < floor.size(); m++) {
                mr = floor.get(m).getMeetings();
                for (int j = 0; j < mr.size(); j++) {
                    mr.get(j).setState(false);
                }
                floor.get(m).setAvailable_no_of_meeting_rooms(floor.get(m).getTotal_no_of_meeting_rooms());
            }


        }

        for (int i = 0; i < meet.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date d1 = sdf.parse(meet.get(i).getStart_date());
                Date d2 = sdf.parse(meet.get(i).getEnd_date());
                Date d3 = sdf.parse(sdate);
                Date d4 = sdf.parse(edate);
                Boolean b = checkdatescollide(meet.get(i).getStart_date(), meet.get(i).getEnd_date(), sdate, edate);
                if (b == true) {
                    System.out.println("within if");
                    int bid = meet.get(i).getBuilding_id();
                    int fid = meet.get(i).getFloor_id();
                    int mid = meet.get(i).getConf_room();
                    String fs = Integer.toString(bid) + Integer.toString(fid);
                    int f = Integer.parseInt(fs);

                    System.out.println(bid);
                    //System.out.println(fid);
                    System.out.println(fs);
                    bid = bid - 1;
                    fid = fid - 1;
                    mid = mid - 1;
                    //System.out.println(build.get(bid).getFloors().get(fid).getMeetings().get(mid).getState());
                    if (build.get(bid).getBuilding_state().equals("enable") && build.get(bid).getFloors().get(fid).getMeetings().get(mid).getState() == false) {
                        System.out.print("hiiiiiiiiiiiiiiii");
                        build.get(bid).setAvailable_no_of_meeting_rooms(build.get(bid).getAvailable_no_of_meeting_rooms() - 1);

                    } else if (build.get(bid).getBuilding_state().equals("disable")) {
                        System.out.print("hii");
                        build.get(bid).setAvailable_no_of_meeting_rooms(0);

                    }


                    if (build.get(bid).getFloors().get(fid).getFloor_state().equals("enable") && build.get(bid).getFloors().get(fid).getMeetings().get(mid).getState() == false) {
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(build.get(bid).getFloors().get(fid).getAvailable_no_of_meeting_rooms() - 1);

                    } else if (build.get(bid).getFloors().get(fid).getFloor_state().equals("disable")) {
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(0);
                    }

                    build.get(bid).getFloors().get(fid).getMeetings().get(mid).setState(true);
                    build.get(bid).getFloors().get(fid).getMeetings().get(mid).setReason_for_booking(meet.get(i).getReason_for_booking());
                } else {
                    System.out.println("not within");
                }

            } catch (InvalidDateException e) {
                throw new InvalidDateException();
            } catch (Exception e) {

            }
        }

        return build;

    }


    @Override
    public List<Building> getmultipleDayDetailswithTime(String sdate, String edate, String stime, String etime) {
        List<Building> build = (List<Building>) this.brepo.findAll();

        List<conf_confirmation> meet = (List<conf_confirmation>) this.meetingRepo.findAll();
        List<meeting> mr = new ArrayList<meeting>();
        List<Floor> floor = new ArrayList<Floor>();

        boolean b1 = true;
        for (int i = 0; i < build.size(); i++) {


            int n = build.get(i).getTotal_no_of_meeting_rooms();

            build.get(i).setAvailable_no_of_meeting_rooms(n);
            floor = build.get(i).getFloors();
            for (int m = 0; m < floor.size(); m++) {
                mr = floor.get(m).getMeetings();
                for (int j = 0; j < mr.size(); j++) {
                    mr.get(j).setState(false);
                }
                floor.get(m).setAvailable_no_of_meeting_rooms(floor.get(m).getTotal_no_of_meeting_rooms());
            }


        }

        for (int i = 0; i < meet.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(meet.get(i).getStart_date());
                Date d2 = sdf.parse(meet.get(i).getEnd_date());
                Date d3 = sdf.parse(sdate);
                Date d4 = sdf.parse(edate);
                System.out.println("after tryyyyyyyyyyyyyyyyyyyyyyyyyy");
                Boolean b = checkdatescollide(meet.get(i).getStart_date(), meet.get(i).getEnd_date(), sdate, edate);
                try {
                    b1 = TimerangesCollideornot(meet.get(i).getStart_time(), meet.get(i).getEnd_time(), stime, etime);
                } catch (InvalidTimeException e) {
                    System.out.println("WIthin catch");
                    throw new InvalidDateException();
                }
                System.out.println("bb");

                if (b == true && b1 == true) {
                    System.out.println("within if");
                    int bid = meet.get(i).getBuilding_id();
                    int fid = meet.get(i).getFloor_id();
                    int mid = meet.get(i).getConf_room();
                    String fs = Integer.toString(bid) + Integer.toString(fid);
                    int f = Integer.parseInt(fs);

                    System.out.println(bid);
                    //System.out.println(fid);
                    System.out.println(fs);
                    bid = bid - 1;
                    fid = fid - 1;
                    mid = mid - 1;
                    if (build.get(bid).getBuilding_state().equals("enable") && build.get(bid).getFloors().get(fid).getMeetings().get(mid).getState() == false) {
                        System.out.println("hiiiiiiiiiiiiiiiiii");
                        build.get(bid).setAvailable_no_of_meeting_rooms(build.get(bid).getAvailable_no_of_meeting_rooms() - 1);
                    } else if (build.get(bid).getBuilding_state().equals("disable")) {
                        build.get(bid).setAvailable_no_of_meeting_rooms(0);

                    }


                    if (build.get(bid).getFloors().get(fid).getFloor_state().equals("enable") && build.get(bid).getFloors().get(fid).getMeetings().get(mid).getState() == false)
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(build.get(bid).getFloors().get(fid).getAvailable_no_of_meeting_rooms() - 1);

                    else if (build.get(bid).getFloors().get(fid).getFloor_state().equals("disable")) {
                        build.get(bid).getFloors().get(fid).setAvailable_no_of_meeting_rooms(0);
                    }

                    build.get(bid).getFloors().get(fid).getMeetings().get(mid).setState(true);
                    build.get(bid).getFloors().get(fid).getMeetings().get(mid).setReason_for_booking(meet.get(i).getReason_for_booking());

                } else {
                    System.out.println("not within");
                }

            } catch (InvalidDateException e) {
                throw new InvalidDateException();
            } catch (Exception e) {

            }
        }

        return build;

    }


    @Override
    public ResponseEntity<Object> saveDateTime(conf_confirmation c) {
        conf_confirmation cn = new conf_confirmation();
        // cn.setDate(c.getDate());
        cn.setEmail(c.getEmail());

        cn.setBuilding_id(c.getBuilding_id());

        cn.setFloor_id(c.getFloor_id());
        cn.setConf_room(c.getConf_room());
        cn.setStart_time(c.getStart_time());
        cn.setEnd_time(c.getEnd_time());

        meetingRepo.save(cn);

        System.out.println("obj  " + c);
        return null;


    }
    Comparator<TeamMeetingDetails> reverseTeamMeetingDetails = (c1, c2) -> {
        return c2.getStart_date().compareTo(c1.getStart_date()) & c2.getStart_time().compareTo(c1.getStart_time());
    };
    public ResponseEntity<Object> saveSingleDay(conf_confirmation conf) {
        conf_confirmation cn = new conf_confirmation();
        
        List<meeting> l = mrepo.findAll();
        System.out.println(conf);
        String s = Integer.toString(conf.getConf_room());
        String sdate=conf.getStart_date();
        String stime=conf.getStart_time();
        String etime=conf.getEnd_time();
        Object obj2 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail2 = ((userDetailsImpl) obj2).getUsername();
       // User cuser = erepo.finduser(mail2);
        String curole=erepo.getRole(mail2);
        int confRecords=meetingRepo.getConfRecords2(mail2,stime,etime,sdate);
        System.out.println("\n The Count of records:"+confRecords);
        if((curole.equalsIgnoreCase("Employee") || curole.equalsIgnoreCase("Project manager")) && confRecords > 0) {
        	System.out.println("INSIDE THE NEW IF BLOCK in meeting with time");
        	throw new BookingAlreadyExists();
        }
        String s2 = Integer.toString(conf.getConf_room());
        char bd = s2.charAt(0);
        int bid = Character.getNumericValue(bd);

        char fn = s2.charAt(1);
       int fid = Character.getNumericValue(fn);
        char mrn = s2.charAt(2);
       int mrd = Character.getNumericValue(mrn);
       System.out.println("\n sdate"+sdate+" stime:"+stime+" etime:"+etime+" bid:"+bid+" fid"+fid+" mrd"+mrd);
        int records=meetingRepo.getRecords(sdate, stime, etime,bid,fid,mrd);
        
        if(records>0) {
        	System.out.println("\n INSIDE make Booking for Team with Time inc");
        	throw new BookingAlreadyExists();
        }
        char b = s.charAt(0);
        int bl = Character.getNumericValue(b);
        // cn.setEmail(emails.get(i));
        cn.setBuilding_id(bl);

        char f = s.charAt(1);
        int fl = Character.getNumericValue(f);
        cn.setFloor_id(fl);

        char m = s.charAt(2);
        int mr = Character.getNumericValue(m);
        cn.setConf_room(mr);

        cn.setEnd_time(conf.getEnd_time());
        cn.setStart_time(conf.getStart_time());
        cn.setStart_date(conf.getStart_date());
        cn.setEnd_date(conf.getEnd_date());
        cn.setEmail(conf.getEmail());
        cn.setReason_for_booking(conf.getReason_for_booking());
        String name = erepo.findname(conf.getEmail());

        System.out.println(name);
        cn.setName(name);
        cn.setBookedBy(name);
        System.out.println(cn);
        System.out.println(conf.getReason_for_booking());
        System.out.println(cn.getReason_for_booking());
        meetingRepo.save(cn);

//        for (int i = 0; i < l.size(); i++) {
//            if (conf.getConf_room() == l.get(i).getNumber()) {
//                    l.get(i).setReason_for_booking(conf.getReason_for_booking());
////                mrepo.updateReason(conf.getConf_room(), conf.getReason_for_booking());
//            }
//        }
        return new ResponseEntity<Object>("booking successful", HttpStatus.OK);

    }

    public ResponseEntity<Object> saveDetails(conf_confirmation conf) {


        conf_confirmation c = new conf_confirmation();
        String s2 = Integer.toString(conf.getConf_room());
        char bd = s2.charAt(0);
        int bdid = Character.getNumericValue(bd);

        char fn = s2.charAt(1);
       int flid = Character.getNumericValue(fn);
        char mrn = s2.charAt(2);
       int mrd = Character.getNumericValue(mrn);
        //  sdate = formatter.format(date);
        List<meeting> l = mrepo.findAll();
        System.out.println(conf);
        String s = Integer.toString(conf.getConf_room());
        char b1 = s.charAt(0);
        int bu = Character.getNumericValue(b1);
        c.setBuilding_id(bu);
        String sdate=conf.getStart_date();
        String stime=conf.getStart_time();
        String etime=conf.getEnd_time();
        int records=meetingRepo.getRecords(sdate, stime, etime,bdid,flid,mrd);
        if(records>0) {
        	System.out.println("\n INSIDE make Booking for Team with Time inc");
        	throw new BookingAlreadyExists();
        }
        char b2 = s.charAt(1);
        int fl = Character.getNumericValue(b2);
        c.setFloor_id(fl);
        int bid = conf.getBuilding_id();
        String build = this.brepo.findId(bid);
        // c.setBuilding_name(build);

        int fid = conf.getFloor_id();
        String floor = this.frepo.findFId(fid);
        //c.setFloor_name(floor);
        // c.setFloor_id(conf.getFloor_id());
        //  c.setDate(conf.getDate());
        // c.setFloor_id(fl);

        c.setEmail(conf.getEmail());

        char w1 = s.charAt(2);
        int wr = Character.getNumericValue(w1);
        c.setConf_room(wr);

        c.setStart_date(conf.getStart_date());
        c.setEnd_date(conf.getEnd_date());
        String name = erepo.findname(conf.getEmail());
        System.out.println(name);
        c.setName(name);
        //edate=formatter.format(localendDate);
        // System.out.println(sdate);
        // c.setDate(sdate);
        c.setStart_time("00:01:00");
        c.setEnd_time("23:59:00");
        c.setReason_for_booking(conf.getReason_for_booking());
        meetingRepo.save(c);
        return null;
    }

    public ResponseEntity<Object> edit_room(conf_confirmation conf) {

        String email = conf.getEmail();
        int b_id = conf.getConf_room() / 100;
        int f_id = (conf.getConf_room() / 10) % 10;
        int room_id = conf.getConf_room() % 10;
            meetingRepo.update(email, b_id, f_id, room_id, conf.getStart_date(), conf.getEnd_date(), conf.getStart_time(), conf.getEnd_time());
List<meeting> l = new ArrayList<>();
        System.out.println(l);

        for (int i = 0; i < l.size(); i++) {
            meeting m = new meeting();
            if (conf.getConf_room() == l.get(i).getNumber()) {

                mrepo.updateReason(conf.getConf_room(), conf.getReason_for_booking());
            }

        }

        return new ResponseEntity<Object>("Booking successful", HttpStatus.OK);

    }

    public ResponseEntity<Object> saveMultipledaysTime(String edate, conf_confirmation conf) {

        // LocalDate localstartDate = LocalDate.parse(conf.getDate(), formatter);
        LocalDate localendDate = LocalDate.parse(edate, formatter);
        String sdate = "";
        localendDate = localendDate.plusDays(1);
        // for (LocalDate date = localstartDate;date.isBefore(localendDate); date = date.plusDays(1))
        {
            conf_confirmation c = new conf_confirmation();
            // sdate = formatter.format(date);

            c.setBuilding_id(conf.getBuilding_id());
            int bid = conf.getBuilding_id();
            String build = this.brepo.findId(bid);
            // c.setBuilding_name(build);

            int fid = conf.getFloor_id();
            String floor = this.frepo.findFId(fid);
            //c.setFloor_name(floor);
            c.setFloor_id(conf.getFloor_id());
            //  c.setDate(conf.getDate());
            c.setEmail(conf.getEmail());
            c.setConf_room(conf.getConf_room());
            c.setStart_time(conf.getStart_time());
            //edate=formatter.format(localendDate);
            c.setEnd_time(conf.getEnd_time());
            System.out.println(sdate);
            //c.setDate(sdate);
            meetingRepo.save(c);
        }
        return null;

    }

    @Override
    public conf_confirmation getbookedDetails(String sdate, String edate, String stime, String etime, int mid) {

        String s = Integer.toString(mid);
        char b = s.charAt(0);
        char f = s.charAt(1);
        char m = s.charAt(2);

        int bid = Character.getNumericValue(b);
        int fid = Character.getNumericValue(f);
        int room = Character.getNumericValue(m);
        conf_confirmation c = meetingRepo.findUserby(sdate, edate, stime, etime, bid, fid, room);
        System.out.println(c);
        return c;

    }

    @Override
    public List<conf_confirmation> getBookedDetailforDays(String sdate, String edate, int mid) {
        String s = Integer.toString(mid);
        char b = s.charAt(0);
        char f = s.charAt(1);
        char m = s.charAt(2);

        int bid = Character.getNumericValue(b);
        int fid = Character.getNumericValue(f);
        int room = Character.getNumericValue(m);
        List<conf_confirmation> c = meetingRepo.findAllUserby(sdate, edate, room);
        System.out.println(c);
        return c;

    }

    @Override
    public void updatebookingDetailsforDays(String sdate, String edate, String email, String reason, int mid) {


        String s = Integer.toString(mid);
        char b = s.charAt(0);
        char f = s.charAt(1);
        char m = s.charAt(2);
        int bid = Character.getNumericValue(b);
        int fid = Character.getNumericValue(f);
        int room = Character.getNumericValue(m);
        conf_confirmation cn = new conf_confirmation();

        List<conf_confirmation> c = meetingRepo.findAllUserby(sdate, edate, room);
        System.out.println(c);
        for (int i = 0; i < c.size(); i++) {
            try {
                sendEmailtouser5(c.get(i).getEmail(), c.get(i).getName(), c.get(i).getBuilding_id(), c.get(i).getFloor_id(), c.get(i).getConf_room(), c.get(i).getStart_date(), c.get(i).getEnd_date(), c.get(i).getStart_time(), c.get(i).getEnd_time(), email);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        for (int j = 0; j < c.size(); j++) {
            System.out.println(cn);
            meetingRepo.deleteUser(c.get(j).getEmail(), c.get(j).getName(), c.get(j).getBuilding_id(), c.get(j).getFloor_id(), c.get(j).getConf_room(), c.get(j).getStart_date(), c.get(j).getEnd_date(), c.get(j).getStart_time(), c.get(j).getEnd_time());
        }
        for (int j = 0; j < c.size(); j++) {
            System.out.println(cn);
            meetingRepo.deleteUser(c.get(j).getEmail(), c.get(j).getName(), c.get(j).getBuilding_id(), c.get(j).getFloor_id(), c.get(j).getConf_room(), c.get(j).getStart_date(), c.get(j).getEnd_date(), c.get(j).getStart_time(), c.get(j).getEnd_time());
        }
        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String mail = obj.getName();
        User cuser = erepo.finduser(mail);
        String name = erepo.findname(cuser.getEmail());
        cn.setEmail(cuser.getEmail());
        cn.setName(name);
        cn.setStart_date(sdate);
        cn.setEnd_date(edate);
        cn.setStart_time("00:01");
        cn.setEnd_time("23:59");
        cn.setBuilding_id(bid);
        cn.setFloor_id(fid);
        cn.setConf_room(room);
        cn.setReason_for_booking(reason);
        meetingRepo.save(cn);
        List<meeting> l = mrepo.findAll();
        System.out.println(cn);
        String r = Integer.toString(cn.getConf_room());
        String bn = Integer.toString(cn.getBuilding_id());
        String fn = Integer.toString(cn.getFloor_id());
        String conf_room = bn + fn + r;
        int cnf = Integer.parseInt(conf_room);
    }


    @Override
    public ResponseEntity<String> updatebookingDetails(String sdate, String edate, String stime, String etime, String email, String reason, int mid,String pro) {
        List<meeting> ml = mrepo.findAll();
        String s = Integer.toString(mid);
        char b = s.charAt(0);
        char f = s.charAt(1);
        char m = s.charAt(2);
        int bid = Character.getNumericValue(b);
        int fid = Character.getNumericValue(f);
        int room = Character.getNumericValue(m);
        Date fdate;
        Date fdate2;
       int bl=0,fl=0,mr=0;
        SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        conf_confirmation c = meetingRepo.findUserby(sdate, edate, stime, etime, bid, fid, room);
        // System.out.println(c);
        // String s1 = Integer.toString(c.getConf_room());
        // char b1 = s1.charAt(0);
        // bl = Character.getNumericValue(b1);
        // char f1 = s1.charAt(1);
        // fl = Character.getNumericValue(f1);
        // char m1 = s1.charAt(2);
        // mr = Character.getNumericValue(m1);
        try {
            sendEmailtouser5(c.getEmail(), c.getName(), c.getBuilding_id(), c.getFloor_id(), c.getConf_room(), c.getStart_date(), c.getEnd_date(), c.getStart_time(), c.getEnd_time(), email);
        } catch (Exception e) {
            System.out.println(e);
        }
        //
        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String mail = obj.getName();
        User cuser = erepo.finduser(mail);
        String name = erepo.findname(cuser.getEmail());
//        for(int i=0;i<ml.size();i++){
//            if(ml.get(i).getNumber()==mid){
//                ml.get(i).setReason_for_booking(reason);
//            }
//        }
        if(pro.equals("null")){
            pro="-";
            String book="-";
            System.out.println("1111111111111111111111111111111111"+pro);
            meetingRepo.updateUser(email, name, reason, bid, fid, room, sdate, edate, stime, etime,pro,book);
        }
        else{
            System.out.println("222222222222222222222222222222222222"+pro);
            List<String> emails= erepo.findEmails(pro);
            for(int i=0;i<emails.size();i++)
            {
                System.out.print("withinnn");
                try{
                    fdate=new SimpleDateFormat("yyyy-MM-dd").parse(c.getStart_date());
                    fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(c.getEnd_date());
                    String finalDate=formates2.format(fdate);
                    String finalDate2=formates2.format(fdate2);
                    String n1=erepo.findname(emails.get(i));
                    sendEmailtouserforMeetingroom(emails.get(i), n1, finalDate, finalDate2, c.getStart_time(), c.getEnd_time(), mail, c.getBuilding_id(),c.getFloor_id(), c.getConf_room());
            }catch(Exception e)
            {
                throw new InvalidDateException();
            }
        }
            meetingRepo.updateUser(email, name, reason, bid, fid, room, sdate, edate, stime, etime,pro,cuser.getName());
        }
        return new ResponseEntity<String>("new booking made", HttpStatus.OK);
    }

    //    boolean b1= TimerangesCollideornot(meet.get(i).getStart_time(),meet.get(i).getEnd_time(),stime,etime);
    public List<Building> getBuildingForRangeofDates(String startDate, String endDate, String report, String stime, String etime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // String date = "16/08/2016";

        //convert String to LocalDate
        LocalDate localstartDate = LocalDate.parse(startDate, formatter);
        LocalDate localendDate = LocalDate.parse(endDate, formatter);
        String end = formatter.format(localendDate);
        localendDate = localendDate.plusDays(1);
        List<Building> build = (List<Building>) this.brepo.findAll();
        LocalDate currentDate = LocalDate.now();
        // String date = "16/08/2016";
        //convert String to LocalDate
        List<Confirmation> conf=(List<Confirmation>) this.crepo.findAll();
        for(int i=0;i<conf.size();i++){
     // List< Confirmation> c=crepo.checkexisitingBooking(conf.get(i).getDate());
      LocalDate l = LocalDate.parse(conf.get(i).getDate(), formatter);
      String bookingDate=formatter.format(l);
      if (l.isBefore(currentDate)) {
          System.out.println(l);
          crepo.removeOldDateBookings(bookingDate);
}
}
        List<conf_confirmation> meet = (List<conf_confirmation>) this.meetingRepo.findAll();
        List<Floor> floor = new ArrayList<Floor>();
        List<Workstation> work = new ArrayList<>();
        List<meeting> mr;
        String sdate = "";
        String edate = "";
        String start = formatter.format(localstartDate);
        int count;
        List<String> r_email;

        for (int i = 0; i < build.size(); i++) {


            int n = build.get(i).getTotal_no_of_workstations();

            build.get(i).setNo_of_workstations(n);
            int b = build.get(i).getTotal_no_of_meeting_rooms();

            build.get(i).setAvailable_no_of_meeting_rooms(b);
            floor = build.get(i).getFloors();
            for (int j = 0; j < floor.size(); j++) {
                floor.get(j).setAvailable_no_of_meeting_rooms(floor.get(j).getTotal_no_of_meeting_rooms());
                floor.get(j).setNo_of_workstations(floor.get(j).getTotal_no_of_workstations());
            }
            boolean status = true;
            for (LocalDate date = localstartDate; date.isBefore(localendDate); date = date.plusDays(1)) {
                sdate = formatter.format(date);
                edate = formatter.format(localendDate);


                if (build.get(i).getBuilding_state().equals("enable")) {
//              build.get(i).setNo_of_workstations(build.get(i).getNo_of_workstations() - count_build);
                    if (build.get(i).getNo_of_workstations() == 0 && build.get(i).getAvailable_no_of_meeting_rooms() == 0) {
                        build.get(i).setBuilding_state("disable");
                    } else {
                        build.get(i).setBuilding_state("enable");
                    }
                } else {
                    if(report.equals("false"))
                    build.get(i).setNo_of_workstations(0);
                    build.get(i).setAvailable_no_of_meeting_rooms(0);
                    //				confirmationRepo.deleteBEntry((build.get(i).get_id()));
                }
                for (int m = 0; m < floor.size(); m++) {
                    if (floor.get(m).getFloor_state().equals("enable")) {
                        if (floor.get(m).getNo_of_workstations() == 0 && floor.get(m).getAvailable_no_of_meeting_rooms() == 0) {
                            floor.get(m).setFloor_state("disable");
                        } else {
                            floor.get(m).setFloor_state("enable");
                        }

                    } else {
                        if (report.equals("false")){
                            floor.get(m).setNo_of_workstations(0);
                        floor.get(m).setAvailable_no_of_meeting_rooms(0);
//					this.confirmationRepo.deleteFEntry((floor.get(j).getFloor_no())%10);
                        if (build.get(i).getNo_of_workstations() > 0) {
                            if (status) {
                                System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                build.get(i).setNo_of_workstations(build.get(i).getNo_of_workstations() - floor.get(m).getTotal_no_of_workstations());
//                                build.get(i).setAvailable_no_of_meeting_rooms(build.get(i).getAvailable_no_of_meeting_rooms() - floor.get(m).getTotal_no_of_meeting_rooms());
                            }
                        }
                        if(build.get(i).getAvailable_no_of_meeting_rooms() > 0){
                            if (status) {
                                build.get(i).setAvailable_no_of_meeting_rooms(build.get(i).getAvailable_no_of_meeting_rooms() - floor.get(m).getTotal_no_of_meeting_rooms());
                            }
                        }
                    }
                    }

                    mr = floor.get(m).getMeetings();
                    work = floor.get(m).getWorkstations();
                    if (status) {
                        for (int t = 0; t < mr.size(); t++) {
                            int id = mr.get(t).getNumber() % 10;
                            if (stime == null || etime == null) {
                                r_email = meetingRepo.findRoomOnTime(build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id, sdate, endDate);
                            } else {
                                r_email = meetingRepo.findRoom(build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id, sdate, endDate, stime, etime);
                            }
                            System.out.println(r_email);
                            if (r_email.size()>0) {
                                mr.get(t).setReason_for_booking(r_email.get(0));
                                boolean check = mr.get(t).getState();
                                System.out.println(check);
                                if (check == false) {
//                                    String reason = com.Repository.meetingRepo.findReason()
//                                    mr.get(t).setReason_for_booking();
                                    mr.get(t).setState(true);
                                    if (report.equals("false")) {
                                        count = meetingRepo.countAll(sdate, endDate, build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id);
                                        System.out.println(count);
                                    } else {
                                        count = meetingRepo.countForReport(sdate, endDate, build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id);
                                    }
                                    if (count > 0) {
                                        build.get(i).setAvailable_no_of_meeting_rooms(build.get(i).getAvailable_no_of_meeting_rooms() - 1);
                                        floor.get(m).setAvailable_no_of_meeting_rooms(floor.get(m).getAvailable_no_of_meeting_rooms() - 1);
                                    }
                                }
                            }
                            if (report.equals("false")) {
                                if (status) {
                                    if (mr.get(t).getRoom_state().equals("disable") && floor.get(m).getFloor_state().equalsIgnoreCase("enable")) {
                                        if(build.get(i).getAvailable_no_of_meeting_rooms()>0) {
                                            build.get(i).setAvailable_no_of_meeting_rooms(build.get(i).getAvailable_no_of_meeting_rooms() - 1);
                                        }
                                        if(floor.get(m).getAvailable_no_of_meeting_rooms()>0) {
                                            floor.get(m).setAvailable_no_of_meeting_rooms(floor.get(m).getAvailable_no_of_meeting_rooms() - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (int k = 0; k < work.size(); k++) {
                        int id = work.get(k).getNumber();
                        if (id < 1000) {
                            id = id % 10;
                        } else {
                            id = id % 100;
                        }
                        String w_email="";
//            System.out.println("/////////////////////////////////////////////////////////////////" + sdate);
                        if(report.equals("true")){
                            w_email = this.crepo.findForTrue(build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id, sdate);

                        }
                        else{
                            w_email = this.crepo.findWorkstation(build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id, sdate);

                        }
                        //					System.out.println(w_id);
                        if (w_email != null) {
                            String pname =  emailRepository.findProjectByEmail(w_email);
                            String pstring = build.get(i).getProjects();
                            String fstring = floor.get(m).getProjects();
                            String pro = "";
                            String pro2 = "";
                            if (pstring != null) {
                                List<String> pblist = new ArrayList<>(Arrays.asList(pstring.split(",")));

                                if (!pblist.contains(pname)&&pname!=null) {
                                    pblist.add(pname);
                                    for (int x = 0; x < pblist.size(); x++) {
                                        pro = pstring + "," + pblist.get(x);
                                    }
                                    build.get(i).setProjects(pro);
                                }
                            }
                            if (fstring != null) {
                                List<String> pflist = new ArrayList<>(Arrays.asList(fstring.split(",")));

                                if (!pflist.contains(pname)&&pname!=null) {
                                    pflist.add(pname);
                                    for (int x = 0; x < pflist.size(); x++) {
                                        pro2 = fstring + "," + pflist.get(x);
                                    }
                                    floor.get(m).setProjects(pro2);
                                }
                            } else {
                                pstring = pname;
                                build.get(i).setProjects(pstring);
                                fstring = pname;
                                floor.get(m).setProjects(fstring);
                            }
                            boolean check = work.get(k).isState();
                            if (check == false) {
                                work.get(k).setState(true);
                                if (report.equals("false")) {
                                    count = crepo.countAll(sdate, build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id);
                                } else {
                                    count = crepo.countForReport(sdate, build.get(i).get_id(), (floor.get(m).getFloor_no() % 10), id);
                                }
                                if (count > 0) {
                                    build.get(i).setNo_of_workstations(build.get(i).getNo_of_workstations() - 1);
                                    floor.get(m).setNo_of_workstations(floor.get(m).getNo_of_workstations() - 1);
                                }
                            }
                        }
                        if (status) {
                            if (report.equals("false")) {
                                if (work.get(k).getWorkstation_state().equals("disable")) {
                                    if(build.get(i).getNo_of_workstations()>0){
                                    build.get(i).setNo_of_workstations(build.get(i).getNo_of_workstations() - 1);}
                                    if(floor.get(m).getNo_of_workstations()>0){
                                    floor.get(m).setNo_of_workstations(floor.get(m).getNo_of_workstations() - 1);}
                                }
                            }
                        }
                    }
                }

                status = false;
            }
        }
        return build;


    }

        Comparator<Confirmation> reverseComparator = (c1, c2) -> {
        return c2.getDate().compareTo(c1.getDate());
    };
    Comparator<TeamMeetingDetails> reverseComparator2 = (c1, c2) -> {
        return c2.getStart_date().compareTo(c1.getStart_date());
    };
    public ResponseEntity<String> enableRooms(int id) {
        mrepo.enableMeetingRooms(id);
        return new ResponseEntity<String>("Meeting room enabled successfuly", HttpStatus.OK);
    }


    public ResponseEntity<?> disablingBuildingFloor(int num, String reason) {
        int n = num;
        Date fdate;
    	Date fdate2;

    	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (num < 1 || num > 99) {
                throw new BackendException("901", "should be in range 1-99");
            }
        } catch (BackendException e) {
            AutoworkExceptions ae = new AutoworkExceptions(e.getErrCode(), e.getErrMsg());
            return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            AutoworkExceptions ae = new AutoworkExceptions("902", "Something went wrong in the Disable api");
            return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
        }

        if (n > 0 && n < 10) {
            brepo.updateReason(reason, n);
            List<Confirmation> al = crepo.findpersonbybuilding(n);
            List<conf_confirmation> ml=meetingRepo.findpersonbybuilding(n);
            Authentication obj = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("\n object is:" + obj);
            String mail = obj.getName();
            User cu = erepo.findByEmail(mail);
            System.out.println("3333333333333333333333333333333333333333333333");
            
            if (cu.getRole().equalsIgnoreCase("Project manager") || cu.getRole().equalsIgnoreCase("HR")) {
                System.out.print(reason);
                System.out.println("2222222222222222222222222222222222222222222222222222222222222");

                for (int i = 0; i < al.size(); i++) {
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(i).getDate());
                        String finalDate=formates2.format(fdate);
                        System.out.println("11111111111111111111111111111111111111111111111111111");
                        sendEmailtouser(al.get(i).getEmail(), al.get(i).getName(), al.get(i).getBuilding_id(), al.get(i).getFloor_id(), al.get(i).getWorkstation_id(), finalDate, mail, reason);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println(e);
                    }
                }
                for (int i = 0; i < ml.size(); i++) {
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(ml.get(i).getStart_date());
                    	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(ml.get(i).getEnd_date());
                        String finalDate=formates2.format(fdate);
                        String finalDate2=formates2.format(fdate2);
                        System.out.println("11111111111111111111111111111111111111111111111111111");
                        sendEmailtouserforDisablingMeetingroom(ml.get(i).getEmail(), ml.get(i).getName(), ml.get(i).getBuilding_id(), ml.get(i).getFloor_id(), ml.get(i).getConf_room(), finalDate, finalDate2, mail, reason);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println(e);
                    }
                }
            }
        }

        if (n > 10 && n < 100) {
            frepo.updateReasonforFloor(reason, n);

            String n1 = Integer.toString(n);
            char a = n1.charAt(0);
            int b = Character.getNumericValue(a);

            String nf = Integer.toString(n);
            char f = n1.charAt(1);
            int c = Character.getNumericValue(f);

            List<Confirmation> al = crepo.findbypersonbyfloor(b, c);
            List<conf_confirmation> ml=meetingRepo.findbypersonbyfloor(b, c);

            Authentication obj = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("\n object is:" + obj);
            String mail = obj.getName();
            User cu = erepo.findByEmail(mail);
            if (cu.getRole().equalsIgnoreCase("HR") || cu.getRole().equalsIgnoreCase("Project manager")) {

                for (int i = 0; i < al.size(); i++) {
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(i).getDate());
                        String finalDate=formates2.format(fdate);
                        sendEmailtouser(al.get(i).getEmail(), al.get(i).getName(), al.get(i).getBuilding_id(), al.get(i).getFloor_id(), al.get(i).getWorkstation_id(), finalDate, mail, reason);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < ml.size(); i++) {
                    try {
                        System.out.println("11111111111111111111111111111111111111111111111111111");
                        fdate=new SimpleDateFormat("yyyy-MM-dd").parse(ml.get(i).getStart_date());
                    	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(ml.get(i).getEnd_date());
                        String finalDate=formates2.format(fdate);
                        String finalDate2=formates2.format(fdate2);
                        sendEmailtouserforDisablingMeetingroom(ml.get(i).getEmail(), ml.get(i).getName(), ml.get(i).getBuilding_id(), ml.get(i).getFloor_id(), ml.get(i).getConf_room(), finalDate, finalDate2, mail, reason);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println(e);
                    }
                }
            }
        }

        if (n > 0 && n < 10) {
            int p = brepo.getBstate(n);
            if (p == 1) {
                crepo.deleteBEntry(num);
                meetingRepo.deleteBEntry(num);
                brepo.setState(n);
            } else {
                System.out.println("The Building is in Disable state");
            }


        }
        if (n > 10 && n < 100) {
            int q = frepo.getFstate(n);
            if (q == 1) {
                confirmationRepo.deleteFEntry(num / 10, num % 10);
                meetingRepo.deleteFEntry(num/10, num%10);
//				confirmationRepo.deleteWEntry(num/100, (num/10)%10, num%10);
                frepo.setFstate(n);
                List<Building> build = this.brepo.findUsingId(n / 10);
                int countDisable = 0;
                int count = 0;
                countDisable = frepo.findDisableCount(n / 10);
                System.out.println("\n This is after CountDisable:"+countDisable);
                count = frepo.findCount(n / 10);
                System.out.println("\n This is after Count:"+count);
                if (countDisable == count) {
                    brepo.setState(build.get(0).get_id());
                }


            } else {
                System.out.println("The floor is in Disable state");
            }

        }
        return new ResponseEntity<>("disabled successfully", HttpStatus.OK);
    }

    
    @Override
    public List<TeamMeetingDetails> roomDate(String date){
    	Date fdate;
    	Date fdate2;
    
    	try {
    		System.out.println("\n The Date is:"+date);
    		Authentication obj = SecurityContextHolder.getContext().getAuthentication();
    String mail = obj.getName();
    System.out.println(mail);
	List<TeamMeetingDetails> list = new ArrayList<>();
    User user = erepo.finduser(mail);
    System.out.println("\n The User value is:"+user);
    LocalDate localendDate = LocalDate.parse(date, formatter);
    localendDate = localendDate.plusDays(1);
    String end = formatter.format(localendDate);
    List<conf_confirmation> conf = new ArrayList<>();
    String pro = user.getProject_name();
    System.out.println("The Pro value is:"+pro);
    LocalTime time = java.time.LocalTime.now();
//    LocalTime change = LocalTime.parse("19:34:50.63");
    String time2= time.toString();
    System.out.println("LocalTime : " + time2);
//    System.out.println(time);
    //need to edit removed project in onDate and ned to edit if conditon
   System.out.println("\n The Date is:"+date+" The end date is:"+end+"\n The mails is:"+mail+" The Project is:"+pro);
   conf =meetingRepo.onDate(date,end,mail,pro,time2);
 
    //List<TeamMeetingDetails> list = new ArrayList<>();
        System.out.println("The Conf List Size:"+conf.size());
        SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
    for(int i=0;i<conf.size();i++){
        TeamMeetingDetails details = new TeamMeetingDetails();
        fdate=new SimpleDateFormat("yyyy-MM-dd").parse(conf.get(i).getStart_date());
        String finalDate=formates2.format(fdate);
        System.out.println("\n The Date :"+conf.get(i).getStart_date()+"\n The Changed Date:"+finalDate);
        details.setStart_date(finalDate);
        fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(conf.get(i).getEnd_date());
        String finalDate2=formates2.format(fdate2);
        System.out.println("\n The Date :"+conf.get(i).getEnd_date()+"\n The Changed Date:"+finalDate2);
        details.setEnd_date(finalDate2);
        details.setStart_time(conf.get(i).getStart_time());
        details.setEnd_time(conf.get(i).getEnd_time());
        details.setEmail(conf.get(i).getEmail());
        details.setName(conf.get(i).getName());
        details.setBuilding_id(conf.get(i).getBuilding_id());
        details.setFloor_id(conf.get(i).getFloor_id());
        details.setConf_room(conf.get(i).getConf_room());
        details.setBookedBy(conf.get(i).getBookedBy());
        details.setProjectName(conf.get(i).getProject_name());
        String bname = brepo.getBuildingname(conf.get(i).getBuilding_id());
        String fname = frepo.getFloorname((conf.get(i).getBuilding_id()*10)+conf.get(i).getFloor_id());
        details.setBuildingName(bname);
        details.setFloorName(fname);
        list.add(details);
    }
    Collections.sort(list, reverseTeamMeetingDetails);
    Collections.reverse(list);
    System.out.println("\n Reached the end the list size is"+list.size());
    return list;
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
}
    
    
    public ResponseEntity<?> enablingBuildingFloor(int num1) {

        int n = num1;
        try {
            if (num1 < 1 || num1 > 99) {
                throw new BackendException("903", "should be in range 1-99");
            }
        } catch (BackendException e) {
            AutoworkExceptions ae = new AutoworkExceptions(e.getErrCode(), e.getErrMsg());
            return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            AutoworkExceptions ae = new AutoworkExceptions("904", "Something went wrong in the Enable api");
            return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
        }


        if (n > 0 && n < 10) {
            int p = brepo.getBstate(n);
            if (p == 0) {
              //  crepo.returnBEntry(num1);
               // meetingRepo.returnBEntry(num1);
                brepo.setStatetoEnable(n);
            } else {
                System.out.println("The Building is in Enable state");
            }
        }
        if (n > 10 && n < 100) {
            int q = frepo.getFstate(n);
            if (q == 0) {
             //   crepo.returnFEntry(num1 / 10, num1 % 10);
              //  meetingRepo.returnFEntry(num1 / 10, num1 % 10);
                frepo.setFstatetoEnable(n);
            } else {
                System.out.println("The Floor is in Enable state");
            }
        }
        return null;
    }


    public List<Page> getTeamDetails(String p2, String p3,String p4,int o,int s) {
        String pjname = p2;
        String start_date = p3;
        LocalDate sdate = LocalDate.parse(start_date, formatter);
        String end_date = p4;
        LocalDate edate = LocalDate.parse(end_date, formatter);
        Date fdate;
        try {
        System.out.println("\n pjname" + p2);
        
        SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        List<String> name = emailRepository.findByProject_Name(pjname);
        String[] names = new String[20];
        for (int j = 0; j < name.size(); j++) {
            names[j] = name.get(j);
            System.out.println(names[j]);
        }
        List<String> email = new ArrayList<>();
        for (int k = 0; k < name.size(); k++) {
            email.add(emailRepository.findEmail(names[k]));
        }
        System.out.println(email);
        List<Confirmation> conf = new ArrayList<Confirmation>();
        for (int k = 0; k < email.size(); k++) {
            conf.addAll(confirmationRepo.getUsingEmail(email.get(k), start_date, end_date));
        }
        Collections.sort(conf, reverseComparator);
        Collections.reverse(conf);
      int recordPerPage=s;
      int skipCount=(o)*recordPerPage;
      List<Confirmation> conf1=conf.stream().skip(skipCount).limit(recordPerPage).collect(Collectors.toList());
     // Page<Confirmation> page=new Page<>(2,b.size(),conf);
      System.out.println("\n conf VALUE:"+conf1);
      List<Page> P=new ArrayList<>();
      for(int i=0;i<conf1.size();i++) {
      	Page pg=new Page();
      	pg.setBuilding_id(conf1.get(i).getBuilding_id());
      	pg.setBuilding_name(conf1.get(i).getBuilding_name());
      	pg.setFloor_id(conf1.get(i).getFloor_id());
      	pg.setFloor_name(conf1.get(i).getFloor_name());
      	pg.setId(conf1.get(i).getId());
      	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(conf1.get(i).getDate());
        String finalDate=formates2.format(fdate);
        System.out.println("\n The Date :"+conf.get(i).getDate()+"\n The Changed Date:"+finalDate);         
        pg.setDate(finalDate);
      	pg.setEmail(conf1.get(i).getEmail());
      	pg.setWorkstation_id(conf1.get(i).getWorkstation_id());
      	pg.setName(conf1.get(i).getName());
      	pg.setTotalrecords(conf.size());
      	P.add(pg);
      	}
      return P;
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    
    public List<Page> getTeamDetails(String p2, String p3,String p4) {
        String pjname = p2;
        String start_date = p3;
        LocalDate sdate = LocalDate.parse(start_date, formatter);
        String end_date = p4;
        LocalDate edate = LocalDate.parse(end_date, formatter);
        System.out.println("\n pjname" + p2);
        List<String> name = emailRepository.findByProject_Name(pjname);
        String[] names = new String[20];
        for (int j = 0; j < name.size(); j++) {
            names[j] = name.get(j);
            System.out.println(names[j]);
        }
        List<String> email = new ArrayList<>();
        for (int k = 0; k < name.size(); k++) {
            email.add(emailRepository.findEmail(names[k]));
        }
        System.out.println(email);
        List<Confirmation> conf = new ArrayList<Confirmation>();
        for (int k = 0; k < email.size(); k++) {
            conf.addAll(confirmationRepo.getUsingEmail(email.get(k), start_date, end_date));
        }
        Collections.sort(conf, reverseComparator);
        Collections.reverse(conf);
     
        List<Page> P=new ArrayList<>();
        for(int i=0;i<conf.size();i++) {
        	Page pg=new Page();
        	pg.setBuilding_id(conf.get(i).getBuilding_id());
        	pg.setBuilding_name(conf.get(i).getBuilding_name());
        	pg.setFloor_id(conf.get(i).getFloor_id());
        	pg.setFloor_name(conf.get(i).getFloor_name());
        	pg.setId(conf.get(i).getId());
        	pg.setDate(conf.get(i).getDate());
        	pg.setEmail(conf.get(i).getEmail());
        	pg.setWorkstation_id(conf.get(i).getWorkstation_id());
        	pg.setName(conf.get(i).getName());
        	pg.setTotalrecords(conf.size());
        	P.add(pg);
        	}
        return P;
    }
    
    
    
    public List<Page> getTeamDetailsUpdated(String p2, String p3,String p4) {

    	String pjname = p2;
        String start_date = p3;
        String end_date = p4;
        String sDate1="2022-10-22";
        Date date1;
        Date date2;
        Date fdate;
		try {
			
			 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
       
        System.out.println("\n pjname" + p2);
        List<String> name = emailRepository.findByProject_Name(pjname);
        String[] names = new String[20];
        for (int j = 0; j < name.size(); j++) {
            names[j] = name.get(j);
            System.out.println(names[j]);
        }
        List<String> email = new ArrayList<>();
        for (int k = 0; k < name.size(); k++) {
            email.add(emailRepository.findEmail(names[k]));
        }
        System.out.println(email);
        List<Confirmation> conf = new ArrayList<Confirmation>();
        for (int k = 0; k < email.size(); k++) {
            conf.addAll(confirmationRepo.getUsingEmail(email.get(k), start_date, end_date));
        }
        Collections.sort(conf, reverseComparator);
        Collections.reverse(conf);
     
        List<Page> P=new ArrayList<>();
        for(int i=0;i<conf.size();i++) {
        	Page pg=new Page();
        	pg.setBuilding_id(conf.get(i).getBuilding_id());
        	pg.setBuilding_name(conf.get(i).getBuilding_name());
        	pg.setFloor_id(conf.get(i).getFloor_id());
        	pg.setFloor_name(conf.get(i).getFloor_name());
        	pg.setId(conf.get(i).getId());
        	 fdate=new SimpleDateFormat("yyyy-MM-dd").parse(conf.get(i).getDate());
		        String finalDate=formates2.format(fdate);
		        System.out.println("\n The Date :"+conf.get(i).getDate()+"\n The Changed Date:"+finalDate);         
		        pg.setDate(finalDate);
        	pg.setEmail(conf.get(i).getEmail());
        	pg.setWorkstation_id(conf.get(i).getWorkstation_id());
        	pg.setName(conf.get(i).getName());
        	pg.setTotalrecords(conf.size());
        	P.add(pg);
        	}
        return P;
        
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    
    
    
    
    
    
    
    public List<Page> getUserDetails(String email, String sdate, String edate,int a,int b) {
    	int records;
    	Date fdate;
    	try {
    	Pageable paging=PageRequest.of(a, b);
        List<Confirmation> conf = new ArrayList<>();
        System.out.println("\n the Dates are:"+sdate+"The end date:"+edate);
		 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
  
        if (sdate == null && edate == null) {
        	records=confirmationRepo.getByEmailCount(email);
            conf = confirmationRepo.getByEmail(email,paging);
        } else {
        	 records=confirmationRepo.getByEmailDateCount(email, sdate, edate);
            conf = confirmationRepo.getByEmailDate(email, sdate, edate,paging);
        }
        Collections.sort(conf, reverseComparator);
        Collections.reverse(conf);
        List<Page> P=new ArrayList<>();
       
        for(int i=0;i<conf.size();i++) {
        	Page pg=new Page();
        	pg.setBuilding_id(conf.get(i).getBuilding_id());
        	pg.setBuilding_name(conf.get(i).getBuilding_name());
        	pg.setFloor_id(conf.get(i).getFloor_id());
        	pg.setFloor_name(conf.get(i).getFloor_name());
        	pg.setId(conf.get(i).getId());
        	 fdate=new SimpleDateFormat("yyyy-MM-dd").parse(conf.get(i).getDate());
		        String finalDate=formates2.format(fdate);
		        System.out.println("\n The Date :"+conf.get(i).getDate()+"\n The Changed Date:"+finalDate);         
		    pg.setDate(finalDate);
        	pg.setEmail(conf.get(i).getEmail());
        	pg.setWorkstation_id(conf.get(i).getWorkstation_id());
        	pg.setName(conf.get(i).getName());
        	pg.setTotalrecords(records);
        	P.add(pg);
        	}
        return P;
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

    }

    Comparator<conf_confirmation> reverseConf_comparator = (c1, c2) -> {
        return c2.getStart_date().compareTo(c1.getStart_date());
    };

    public List<Pagecount> getDetails(String email, String sdate, String edate,int o,int s) {
    	Date fdate;
    	Date fdate2;
    	try {
        List<conf_confirmation> conf = new ArrayList<>();
        if (sdate == null && edate == null) {
            conf = meetingRepo.findByEmail(email);
        } else {
            conf = meetingRepo.findByEmailDate(email, sdate, edate);
        }
        Collections.sort(conf, reverseConf_comparator);

		 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        Collections.reverse(conf);
        System.out.println("\n conf value is:"+conf+"\n conf size"+conf.size());
        List<TeamMeetingDetails> al = new ArrayList<TeamMeetingDetails>();
        for (int i = 0; i < conf.size(); i++) {
            if (conf.get(i).getProject_name().equalsIgnoreCase("NULL") ||  conf.get(i).getProject_name().equalsIgnoreCase("-")||  conf.get(i).getProject_name().equalsIgnoreCase("NA")) {
                System.out.println("\n INSIDE IF FOR TIMES");
            	String bname = brepo.getBuildingname(conf.get(i).getBuilding_id());

                int fl = conf.get(i).getFloor_id();
                String fl1 = Integer.toString(fl);

                int bl = conf.get(i).getBuilding_id();
                String bl1 = Integer.toString(bl);
                String floor = bl1 + fl1;

                int fno = Integer.parseInt(floor);

                String fname = frepo.getFloorname(fno);
                TeamMeetingDetails t = new TeamMeetingDetails();
                t.setId(conf.get(i).getId());
                t.setBuilding_id(conf.get(i).getBuilding_id());
                t.setBuildingName(bname);
                t.setFloorName(fname);
                t.setEmail(conf.get(i).getEmail());
                t.setFloor_id(conf.get(i).getFloor_id());
                t.setConf_room(conf.get(i).getConf_room());
                t.setStart_date(conf.get(i).getStart_date());
                t.setBookedBy(conf.get(i).getBookedBy());
                t.setEnd_date(conf.get(i).getEnd_date());
                t.setStart_time(conf.get(i).getStart_time());
                t.setEnd_time(conf.get(i).getEnd_time());
                t.setName(conf.get(i).getName());
               
                al.add(t);
              
            }
        }
        System.out.println(al);
        int recordPerPage=s;
        int skipCount=(o)*recordPerPage;
        List<TeamMeetingDetails> lists=al.stream().skip(skipCount).limit(recordPerPage).collect(Collectors.toList());
       
        System.out.println("\n conf VALUE:"+lists);
       // return lists;
        List<Pagecount> P=new ArrayList<>();
        for(int h=0;h<lists.size();h++) {
        	Pagecount pg=new Pagecount();
        	String bname=lists.get(h).getBuildingName();
            pg.setId(lists.get(h).getId());
            pg.setBuilding_id(lists.get(h).getBuilding_id());
            pg.setBuildingName(bname);
            pg.setFloorName(lists.get(h).getFloorName());
            pg.setFloor_id(lists.get(h).getFloor_id());
            pg.setConf_room(lists.get(h).getConf_room());
            fdate=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getStart_date());
	        String finalDate=formates2.format(fdate);
	        System.out.println("\n The Date :"+lists.get(h).getStart_date()+"\n The Changed Date:"+finalDate);  
            pg.setStart_date(finalDate);
            pg.setBookedBy(lists.get(h).getBookedBy());
            fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getEnd_date());
	        String finalDate2=formates2.format(fdate2);
	        System.out.println("\n The Date :"+lists.get(h).getEnd_date()+"\n The Changed Date:"+finalDate2); 
            pg.setEnd_date(finalDate2);
            pg.setStart_time(lists.get(h).getStart_time());
            pg.setEnd_time(lists.get(h).getEnd_time());
            pg.setName(lists.get(h).getName());
        	pg.setTotalrecords(al.size());
        	
        	pg.setEmail(lists.get(h).getEmail());      	
        	P.add(pg);       
        	}
        return P;
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

    }
        public List<TeamMeetingDetails> getDetails (String email){


            List<conf_confirmation> conf = meetingRepo.findByEmail(email);
            Collections.sort(conf, reverseConf_comparator);

            Collections.reverse(conf);
            List<TeamMeetingDetails> al = new ArrayList<TeamMeetingDetails>();
            for (int i = 0; i < conf.size(); i++) {
                if (conf.get(i).getProject_name() == null) {
                    String bname = brepo.getBuildingname(conf.get(i).getBuilding_id());

                    int fl = conf.get(i).getFloor_id();
                    String fl1 = Integer.toString(fl);

                    int bl = conf.get(i).getBuilding_id();
                    String bl1 = Integer.toString(fl);
                    String floor = bl1 + fl1;

                    int fno = Integer.parseInt(floor);

                    String fname = frepo.getFloorname(fno);
                    TeamMeetingDetails t = new TeamMeetingDetails();
                    t.setId(conf.get(i).getId());
                    t.setBuilding_id(conf.get(i).getBuilding_id());
                    t.setBuildingName(bname);
                    t.setFloorName(fname);
                    t.setEmail(conf.get(i).getEmail());
                    t.setFloor_id(conf.get(i).getFloor_id());
                    t.setConf_room(conf.get(i).getConf_room());
                    t.setStart_date(conf.get(i).getStart_date());
                    t.setBookedBy(conf.get(i).getBookedBy());
                    t.setEnd_date(conf.get(i).getEnd_date());
                    t.setStart_time(conf.get(i).getStart_time());
                    t.setEnd_time(conf.get(i).getEnd_time());
                    t.setName(conf.get(i).getName());
                    al.add(t);
                }
            }
            return al;
        }
    public ResponseEntity<String> edit(@RequestBody Dummy_Confirm conf) {
        //  if(wmodel==null || wmodel.state=="false")throw new WorkStationNotAvailable();
    	  Date fdate;
        	Date fdate2;

        	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        String[] str = new String[100];
        int[] wnum = new int[100];
        String dt1 = conf.getDate();
        String dt2 = conf.getDate_end();
        LocalDate std = LocalDate.parse(dt1, formatter);
        LocalDate ed = LocalDate.parse(dt2, formatter);
        String date;
        ed = ed.plusDays(1);
        System.out.println("\n starting:" + std + " ending date is:" + ed);

        for (LocalDate D = std; D.isBefore(ed); D = D.plusDays(1)) {
            date = formatter.format(D);
            for (int h = 0; h < conf.getEmail().size(); h++) {
                str[h] = conf.getEmail().get(h);
                wnum[h] = conf.getWorkstation_id().get(h);
                String name = erepo.findname(str[h]);
                System.out.println("\n name:" + name);
                System.out.println("\n mail:" + str[h]);
                System.out.println("\n wnum:" + wnum[h]);
                System.out.println("\n date:" + date);
                int m = wnum[h];
                int num = m;
                int i;
                String s = Integer.toString(m);
                int l = s.length();
                int[] arr = new int[l];
                if (l == 3) {
                    for (i = 0; i < l - 1; i++) {
                        num = num / 10;
                        arr[i] = num;
                    }
                } else {
                    num = num / 10;
                    for (i = 0; i < l - 2; i++) {
                        num = num / 10;
                        arr[i] = num;
                    }
                }
                int flnum = arr[0];
                int bdnum = arr[1];
                System.out.println("building " + bdnum + "and floor :" + flnum);
                String flname = frepo.getFloorname(flnum);
                String bdname = brepo.getBuildingname(bdnum);
                System.out.println("\n building name " + bdname + " and floor name :" + flname);
                int n = repo.getCount(m);
                //2
                if (n == 1) {
                    System.out.println("This workstation is free");
                } else {
                    System.out.println("This workstation is busy");
                }
                int m1 = 0;
                if (wnum[h] < 1000) {
                    m1 = wnum[h] % 10;
                } else {
                    m1 = wnum[h] % 100;
                }
                System.out.println("\n wid is:" + m1);
                int fln2 = flnum % 10;
                System.out.println("\n new floor is:" + fln2);
                crepo.bupdate(bdnum, str[h], date);
                crepo.fupdate(fln2, str[h], date);
                crepo.wupdate(m1, str[h], date);
                crepo.bdnameupdate(bdname, str[h], date);
                crepo.flnameupdate(flname, str[h], date);
                Authentication obj = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("\n object is:" + obj);
                String mail = (obj).getName();
                System.out.println("\n pj manager email:" + mail);
                User cuser = erepo.findByEmail(mail);
                System.out.println("\n cuser data:" + cuser);
                if (cuser.getRole().equalsIgnoreCase("Project manager")) {
                    System.out.println("inside if");
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
                        String finalDate=formates2.format(fdate);
                        System.out.println("inside try");
                        System.out.println("\n email:" + str[h] + " name :" + name + "building:" + bdnum + "floor:" + fln2 + "wst num :" + m1 + "date:" + date + " pjmail:" + mail);
                        sendEmailtouser3(str[h], name, bdnum, fln2, m1, finalDate, mail);
                        System.out.println("entered try ");
                    } catch (Exception e) {
                        System.out.println("exception:" + e);
                    }
                }
            }
        }

        return new ResponseEntity<String>("Workstation updated successfuly", HttpStatus.OK);
    }

    public ResponseEntity<?> Enable(List<Integer> li) {

        for (int i = 0; i < li.size(); i++) {
            int q = li.get(i);
            try {
                if (q <= 99) {
                    throw new BackendException("913", "Give a Correct Workstation id to Enable");
                }
            } catch (BackendException e) {
                AutoworkExceptions ae = new AutoworkExceptions(e.getErrCode(), e.getErrMsg());
                return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
            }
            int p = repo.getWstate2(q);
            System.out.println("state:" + p);
            try {
                if (p == 0) {
                    throw new BackendException("907", "The Workstation is in Enable state");
                } else {
                    if (q < 1000) {
                        crepo.returnWEntry(q / 100, (q / 10) % 10, q % 10);
                    } else {
                        crepo.returnWEntry(q / 1000, (q / 100) % 10, q % 100);
                    }
                    repo.unsetW(q);
                }
            } catch (BackendException e) {
                AutoworkExceptions ae = new AutoworkExceptions(e.getErrCode(), e.getErrMsg());
                return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                AutoworkExceptions ae = new AutoworkExceptions("908", "Something went wrong in the EnaableState api");
                return new ResponseEntity<AutoworkExceptions>(ae, HttpStatus.BAD_REQUEST);
            }

        }

        return null;
    }

    public ResponseEntity<?> Disable(List<Integer> l) {
    	  Date fdate;
      	Date fdate2;

      	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        for (int j = 0; j < l.size(); j++) {
            String n1 = Integer.toString(l.get(j));
            char a = n1.charAt(0);
            int bu = Character.getNumericValue(a);
            String nf = Integer.toString(l.get(j));
            char f = n1.charAt(1);
            int c = Character.getNumericValue(f);
            String w = nf.substring(2, nf.length());
            int wn = Integer.parseInt(w);
            List<Confirmation> al = crepo.findpersonbyworkstation(bu, c, wn);
            Authentication obj = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("\n object is:" + obj);
            String mail = (obj).getName();
            User cu = erepo.findByEmail(mail);
            if (cu.getRole().equalsIgnoreCase("HR")) {
                for (int i = 0; i < al.size(); i++) {
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(i).getDate());
                        String finalDate=formates2.format(fdate);
                        sendEmailtouserwithoutReason(al.get(i).getEmail(), al.get(i).getName(), al.get(i).getBuilding_id(), al.get(i).getFloor_id(), al.get(i).getWorkstation_id(), finalDate, mail);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        for (int i = 0; i < l.size(); i++) {
            int b = l.get(i);
            int p = repo.getWstate(b);
            System.out.println("state:" + p);
            if (p == 1) {
                if (b < 1000) {
                    crepo.deleteWEntry(b / 100, (b / 10) % 10, b % 10);
                    repo.setwstate(b);
                } else {
                    crepo.deleteWEntry(b / 1000, (b / 100) % 10, b % 100);
                    repo.setwstate(b);
                }
            } else {
                System.out.println("\n The System is in Disable state");
            }
        }
        return null;
    }


    public ResponseEntity<String> removeRoomBooking(conf_confirmation c) {
    	 Date fdate;
       	Date fdate2;

       	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        int bl = 0, fl = 0, mr = 0;
        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String mail = (obj).getName();
        User cuser = erepo.finduser(mail);
        System.out.println("\n The project nameis:"+ c.getProject_name());
        if (cuser.getRole().equalsIgnoreCase("project manager")||cuser.getRole().equalsIgnoreCase("HR")) {

            if (c.start_time != null && c.end_time != null) {
            	System.out.println("Inside IF BLOCK OF DELETE");
                meetingRepo.deleteUser1(c.getEmail(), c.getName(), c.getBuilding_id(), c.getFloor_id(), c.getConf_room(), c.getStart_date(), c.getEnd_date(), c.getStart_time(), c.getEnd_time(), c.getProject_name());

                System.out.println("hiii");
                List<String> l = emailRepository.findEmails(c.getProject_name());
                for (int i = 0; i < l.size(); i++) {

                    String n1 = erepo.findname(l.get(i));
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(c.getStart_date());
                    	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(c.getEnd_date());
                        String finalDate=formates2.format(fdate);
                        String finalDate2=formates2.format(fdate2);
                        sendEmailtouserforCancellingMeetingroom(l.get(i), n1, finalDate, finalDate2, c.getStart_time(), c.getEnd_time(), mail, c.getBuilding_id(), c.getFloor_id(), c.getConf_room());

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            } else {
                meetingRepo.deleteUser2(c.getEmail(), c.getName(), c.getBuilding_id(), c.getFloor_id(), c.getConf_room(), c.getStart_date(), c.getEnd_date(), c.getProject_name());


                List<String> l =emailRepository.findEmails(c.getProject_name());
                for (int i = 0; i < l.size(); i++) {

                    String n1 = erepo.findname(l.get(i));
                    try {
                    	fdate=new SimpleDateFormat("yyyy-MM-dd").parse(c.getStart_date());
                    	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(c.getEnd_date());
                        String finalDate=formates2.format(fdate);
                        String finalDate2=formates2.format(fdate2);
                        sendEmailtouserforCancellingMeetingroomWithoutTime(l.get(i), n1, finalDate,finalDate2, mail, c.getBuilding_id(), c.getFloor_id(), c.getConf_room());

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }
        }
        else {
            System.out.println("emplo");
            if (c.getStart_time() != null && c.getEnd_time() != null) {
                System.out.println("hi");
                System.out.print(c);
                meetingRepo.deleteEmployee(c.getEmail(), c.getName(), c.getBuilding_id(), c.getFloor_id(), c.getConf_room(), c.getStart_date(), c.getEnd_date(), c.getStart_time(), c.getEnd_time());
            } else {
                meetingRepo.deleteEmployee2(c.getEmail(), c.getName(), c.getBuilding_id(), c.getFloor_id(), c.getConf_room(), c.getStart_date(), c.getEnd_date());
            }
        }
        return new ResponseEntity<String>("Booking removed", HttpStatus.OK);
    }

    public ResponseEntity<Object> makeBookingforTeam(conf_confirmation c) {
    	 Date fdate;
        	Date fdate2;

        	SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
        int bl = 0, fl = 0, mr = 0;
        String stime=c.getStart_time();
        String etime=c.getEnd_time();
        String sdate=c.getStart_date();
        Object obj2 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail2 = ((userDetailsImpl) obj2).getUsername();
       // User cuser = erepo.finduser(mail2);
        String curole=erepo.getRole(mail2);
        String pjname=c.getProject_name();
        int confRecords=meetingRepo.getConfRecords(mail2,stime,etime,sdate,pjname);
        System.out.println("\n The Count of records:"+confRecords);
        if((curole.equalsIgnoreCase("Employee") || curole.equalsIgnoreCase("Project manager") || curole.equalsIgnoreCase("HR")) && confRecords>0) {
        	System.out.println("INSIDE THE NEW IF BLOCK");
        	throw new BookingAlreadyExistsForTeam();
        }
        
        List<String> emails = emailRepository.findEmails(c.getProject_name());
        System.out.print(emails);
        String s2 = Integer.toString(c.getConf_room());
        char bd = s2.charAt(0);
        int bid = Character.getNumericValue(bd);

        char fn = s2.charAt(1);
       int fid = Character.getNumericValue(fn);
        char mrn = s2.charAt(2);
       int mrd = Character.getNumericValue(mrn);
       System.out.println("\n The Bid:"+bid+"\n The Floor:"+fid+"\n The Meeting room"+mrd);
//        String sdate=c.getStart_date();
//        String stime=c.getStart_time();
//        String etime=c.getEnd_time();
        int records=meetingRepo.getRecords(sdate, stime, etime,bid,fid,mrd);
        if(records>0) {
        	System.out.println("\n INSIDE make Booking for Team");
        	throw new BookingAlreadyExists();
        }
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = ((userDetailsImpl) obj).getUsername();
        User cuser = erepo.finduser(mail);

        conf_confirmation c1 = new conf_confirmation();

        String s1 = Integer.toString(c.getConf_room());
        char b1 = s1.charAt(0);
        bl = Character.getNumericValue(b1);

        char f1 = s1.charAt(1);
        fl = Character.getNumericValue(f1);
        char m1 = s1.charAt(2);
        mr = Character.getNumericValue(m1);
        c1.setEmail(mail);
        c1.setBuilding_id(bl);
        c1.setFloor_id(fl);
        c1.setConf_room(mr);
        c1.setStart_date(c.getStart_date());
        c1.setEnd_date(c.getEnd_date());
        c1.setStart_time(c.getStart_time());
        c1.setEnd_time(c.getEnd_time());
        c1.setName(cuser.getName());
        c1.setProject_name(c.getProject_name());
        c1.setBookedBy(cuser.getName());
        c1.setReason_for_booking(c.getReason_for_booking());
        List<meeting> l = mrepo.findAll();

        for (int i = 0; i < l.size(); i++) {
            meeting mn = new meeting();
            if (c.getConf_room() == l.get(i).getNumber()) {
                System.out.println("yes");
                mrepo.updateReason(c.getConf_room(), c.getReason_for_booking());

            }


        }
        meetingRepo.save(c1);
        System.out.print(emails);
        System.out.println(emails);

        if (cuser.getRole().equalsIgnoreCase("Project manager") || cuser.getRole().equalsIgnoreCase("hr")) {

            //System.out.println("wit");
            for (int i = 0; i < emails.size(); i++) {
                String n1 = erepo.findname(emails.get(i));
                try {
                    System.out.print("withinnn");
                    fdate=new SimpleDateFormat("yyyy-MM-dd").parse(c.getStart_date());
                	fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(c.getEnd_date());
                    String finalDate=formates2.format(fdate);
                    String finalDate2=formates2.format(fdate2);
                    sendEmailtouserforMeetingroom(emails.get(i), n1, finalDate, finalDate2, c1.getStart_time(), c1.getEnd_time(), mail, bl, fl, mr);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
       
        return new ResponseEntity<Object>("Team booking saved", HttpStatus.OK);

        
    }

    public List<Pagecount> getTeamBookingforMeetingroom(String pname, String sdate, String edate,int o,int s) {
    	Date fdate;
    	Date fdate2;
        if (pname == null) {
            // System.out.println("no project name found");
            System.out.print("within first if");
            
            if (sdate == null && edate == null) {
            	try {
                System.out.print("within ifff");
                List<conf_confirmation> a = meetingRepo.findAllUsers();
                List<TeamMeetingDetails> al = new ArrayList<>();
                System.out.println(a);
                for (int j = 0; j < a.size(); j++) {
                    System.out.print("within for");
                    String bname = brepo.getBuildingname(a.get(j).getBuilding_id());
                    int fl = a.get(j).getFloor_id();
                    String fl1 = Integer.toString(fl);
                    int bl = a.get(j).getBuilding_id();
                    String bl1 = Integer.toString(bl);
                    String floor = bl1 + fl1;
                    int fno = Integer.parseInt(floor);
                    String fname = frepo.getFloorname(fno);
                    TeamMeetingDetails t = new TeamMeetingDetails();
                    System.out.println(bname);
                    t.setId(a.get(j).getId());
                    t.setEmail(a.get(j).getEmail());
                    t.setName(a.get(j).getName());
                    t.setBuilding_id(a.get(j).getBuilding_id());
                    t.setFloor_id(a.get(j).getFloor_id());
                    t.setConf_room(a.get(j).getConf_room());
                    t.setStart_date(a.get(j).getStart_date());
                    t.setEnd_date(a.get(j).getEnd_date());
                    t.setStart_time(a.get(j).getStart_time());
                    t.setEnd_time(a.get(j).getEnd_time());
                    t.setProjectName(a.get(j).getProject_name());
                    t.setBuildingName(bname);
                    t.setFloorName(fname);
                    t.setBookedBy(a.get(j).getBookedBy());
                    System.out.println("setted");
                    al.add(t);
                }
                System.out.println(al);
                Collections.sort(al, reverseComparator2);
                Collections.reverse(al);
                int recordPerPage=s;
                int skipCount=(o)*recordPerPage;
                List<TeamMeetingDetails> lists=al.stream().skip(skipCount).limit(recordPerPage).collect(Collectors.toList());
           	 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
                System.out.println("\n conf VALUE:"+lists);
               // return lists;
            
                List<Pagecount> P=new ArrayList<>();
                for(int h=0;h<lists.size();h++) {
                	Pagecount pg=new Pagecount();
                	String bname=lists.get(h).getBuildingName();
                    pg.setId(lists.get(h).getId());
                    pg.setBuilding_id(lists.get(h).getBuilding_id());
                    pg.setBuildingName(bname);
                    pg.setFloorName(lists.get(h).getFloorName());
                    pg.setFloor_id(lists.get(h).getFloor_id());
                    pg.setConf_room(lists.get(h).getConf_room());
                    fdate=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getStart_date());
        	        String finalDate=formates2.format(fdate);
        	        System.out.println("\n The Date :"+lists.get(h).getStart_date()+"\n The Changed Date:"+finalDate);  
                    pg.setStart_date(finalDate);
                    pg.setBookedBy(lists.get(h).getBookedBy());
                    fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getEnd_date());
        	        String finalDate2=formates2.format(fdate2);
        	        System.out.println("\n The Date :"+lists.get(h).getEnd_date()+"\n The Changed Date:"+finalDate2); 
                    pg.setEnd_date(finalDate2);
                    pg.setStart_time(lists.get(h).getStart_time());
                    pg.setEnd_time(lists.get(h).getEnd_time());
                    pg.setName(lists.get(h).getName());
                	pg.setTotalrecords(al.size());
                	pg.setProject_name(lists.get(h).getProjectName());  
                	pg.setEmail(lists.get(h).getEmail());		//MOD            
                	P.add(pg);       
                	}
                return P;
        	} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
                return null;
            } else {
                System.out.print("within else");
                List<conf_confirmation> a = meetingRepo.findAllUsers();
                List<TeamMeetingDetails> al1 = new ArrayList<>();
                Authentication obj = SecurityContextHolder.getContext().getAuthentication();
                String mail = obj.getName();
                String name = erepo.findname(mail);
                for (int j = 0; j < a.size(); j++) {
                    if(!(a.get(j).getBookedBy().equals(name)&&a.get(j).getProject_name().equals("NA"))) {
                        try {
                            System.out.print("try");
                            boolean b = checkdatescollide(a.get(j).getStart_date(), a.get(j).getEnd_date(), sdate, edate);
                            if (b == true) {
                                System.out.println("within if");
                                int fl = a.get(j).getFloor_id();
                                String fl1 = Integer.toString(fl);
                                int bl = a.get(j).getBuilding_id();
                                String bl1 = Integer.toString(bl);
                                String floor = bl1 + fl1;
                                int fno = Integer.parseInt(floor);
                                String fname = frepo.getFloorname(fno);
                                String bname = brepo.getBuildingname(a.get(j).getBuilding_id());
                                TeamMeetingDetails t = new TeamMeetingDetails();
                                t.setId(a.get(j).getId());
                                t.setEmail(a.get(j).getEmail());
                                t.setName(a.get(j).getName());
                                t.setBuilding_id(a.get(j).getBuilding_id());
                                t.setFloor_id(a.get(j).getFloor_id());
                                t.setConf_room(a.get(j).getConf_room());
                                t.setStart_date(a.get(j).getStart_date());//made a change here
                                t.setEnd_date(a.get(j).getEnd_date());
                                t.setStart_time(a.get(j).getStart_time());
                                t.setEnd_time(a.get(j).getEnd_time());
                                t.setProjectName(a.get(j).getProject_name());
                                t.setBuildingName(bname);
                                t.setFloorName(fname);
                                t.setBookedBy(a.get(j).getBookedBy());
                                al1.add(t);
                            }
                        } catch (InvalidDateException e) {
                            throw new InvalidDateException();
                        } catch (Exception e) {
                        }
                    }
                }
                try {
                	 Collections.sort(al1, reverseComparator2);
                     Collections.reverse(al1);
                int recordPerPage=s;
                int skipCount=(o)*recordPerPage;
                List<TeamMeetingDetails> lists=al1.stream().skip(skipCount).limit(recordPerPage).collect(Collectors.toList());
           	 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
                System.out.println("\n conf VALUE:"+lists);
               // return lists;
                List<Pagecount> P=new ArrayList<>();
                for(int h=0;h<lists.size();h++) {
                	Pagecount pg=new Pagecount();
                	String bname=lists.get(h).getBuildingName();
                    pg.setId(lists.get(h).getId());
                    pg.setBuilding_id(lists.get(h).getBuilding_id());
                    pg.setBuildingName(bname);
                    pg.setFloorName(lists.get(h).getFloorName());
                    pg.setFloor_id(lists.get(h).getFloor_id());
                    pg.setConf_room(lists.get(h).getConf_room());
                    fdate=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getStart_date());
        	        String finalDate=formates2.format(fdate);
        	        System.out.println("\n The Date :"+lists.get(h).getStart_date()+"\n The Changed Date:"+finalDate);  
                    pg.setStart_date(finalDate);
                    pg.setBookedBy(lists.get(h).getBookedBy());
                    fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getEnd_date());
        	        String finalDate2=formates2.format(fdate2);
        	        System.out.println("\n The Date :"+lists.get(h).getEnd_date()+"\n The Changed Date:"+finalDate2); 
                    pg.setEnd_date(finalDate2);
                    pg.setStart_time(lists.get(h).getStart_time());
                    pg.setEnd_time(lists.get(h).getEnd_time());
                    pg.setName(lists.get(h).getName());
                	pg.setTotalrecords(al1.size());
                	pg.setProject_name(lists.get(h).getProjectName());
                	pg.setEmail(lists.get(h).getEmail());
                	P.add(pg);       
                	}
                return P;
            } catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            }
        } else {
            Authentication obj = SecurityContextHolder.getContext().getAuthentication();
            String mail = obj.getName();
            User cu = erepo.findByEmail(mail);
            String name = erepo.findname(mail);
            List<String> emails = emailRepository.findEmails(pname);
            System.out.println(emails);
            List<TeamMeetingDetails> al = new ArrayList<TeamMeetingDetails>();
            List<conf_confirmation> all = meetingRepo.findAll();
            System.out.println(all);
            for (int i = 0; i < all.size(); i++) {
                try {
                    boolean b = checkdatescollide(all.get(i).getStart_date(), all.get(i).getEnd_date(), sdate, edate);
                    if (b == true) {
                        System.out.println("within if");
                        conf_confirmation l = meetingRepo.findUserOfteam(all.get(i).getId(), all.get(i).getBookedBy(),pname);
                        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111"+l);
                        if (l != null) {
                            String bname = brepo.getBuildingname(l.getBuilding_id());
                            int fl = all.get(i).getFloor_id();
                            String fl1 = Integer.toString(fl);
                            int bl = all.get(i).getBuilding_id();
                            String bl1 = Integer.toString(bl);
                            String floor = bl1 + fl1;
                            int fno = Integer.parseInt(floor);
                            String fname = frepo.getFloorname(fno);
                            TeamMeetingDetails t = new TeamMeetingDetails();
                            t.setId(l.getId());
                            t.setProjectName(pname);
                            t.setEmail(l.getEmail());
                            t.setBuilding_id(l.getBuilding_id());
                            t.setBuildingName(bname);
                            t.setFloor_id(l.getFloor_id());
                            t.setConf_room(l.getFloor_id());
                            t.setStart_date(l.getStart_date());
                            t.setEnd_date(l.getEnd_date());
                            t.setStart_time(l.getStart_time());
                            t.setEnd_time(l.getEnd_time());
                            t.setName(l.getName());
                            t.setBookedBy(l.getBookedBy());
                            t.setFloorName(fname);
                            al.add(t);
                        }
                    } else {
                        System.out.println("not in if");
                    }
                } catch (InvalidDateException e) {
                    throw new InvalidDateException();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //System.out.println(al));
            }
            System.out.println(al);
            try {
            	 Collections.sort(al, reverseComparator2);
                 Collections.reverse(al);
            int recordPerPage=s;
            int skipCount=(o)*recordPerPage;
            List<TeamMeetingDetails> lists=al.stream().skip(skipCount).limit(recordPerPage).collect(Collectors.toList());
       	 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println("\n conf VALUE:"+lists);
           // return lists;
            List<Pagecount> P=new ArrayList<>();
            for(int h=0;h<lists.size();h++) {
            	Pagecount pg=new Pagecount();
            	String bname=lists.get(h).getBuildingName();
                pg.setId(lists.get(h).getId());
                pg.setBuilding_id(lists.get(h).getBuilding_id());
                pg.setBuildingName(bname);
                pg.setFloorName(lists.get(h).getFloorName());
                pg.setFloor_id(lists.get(h).getFloor_id());
                pg.setConf_room(lists.get(h).getConf_room());
                fdate=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getStart_date());
    	        String finalDate=formates2.format(fdate);
    	        System.out.println("\n The Date :"+lists.get(h).getStart_date()+"\n The Changed Date:"+finalDate);  
                pg.setStart_date(finalDate);
                pg.setBookedBy(lists.get(h).getBookedBy());
                fdate2=new SimpleDateFormat("yyyy-MM-dd").parse(lists.get(h).getEnd_date());
    	        String finalDate2=formates2.format(fdate2);
    	        System.out.println("\n The Date :"+lists.get(h).getEnd_date()+"\n The Changed Date:"+finalDate2); 
                pg.setEnd_date(finalDate2);
                pg.setStart_time(lists.get(h).getStart_time());
                pg.setEnd_time(lists.get(h).getEnd_time());
                pg.setName(lists.get(h).getName());
            	pg.setTotalrecords(al.size());
            	pg.setProject_name(lists.get(h).getProjectName());
            	pg.setEmail(lists.get(h).getEmail());
            	P.add(pg);       
            	}
            return P;} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
		return null;
    }
//    public void sendEmailtouser(String email, String name, int b1, int f, int w, String Date, String hremail,
//    		String reason) throws Exception {
//
//    		String toEmail = email;
//    		String fromEmail = "automatedworkstationassigner@gmail.com";
//    		String senderName = "Automated workstation assigner ";
//    		String subject = "Workstation booking cancelled";
//    		String hrname = erepo.findname(hremail);
//
//    		String username = name;
//    		String wno = Integer.toString(w);
//    		String b = "";
//    		if (b1 == 1) {
//    		b = "Main Building";
//    		}
//    		if (b1 == 2) {
//    		b = "SVC Building";
//    		}
//    		if (b1 == 3) {
//    		b = "Pasta Street Building";
//    		}
//    		if (b1 == 4) {
//    		b = "Roush Building";
//    		}
//    		if (b1 == 5) {
//    		b = "CCD Building";
//    		}
//    		String b2 = "";
//    		if (f == 1) {
//    		b2 = "1st floor";
//    		}
//    		if (f == 2) {
//    		b2 = "2nd floor";
//    		}
//    		if (f == 3) {
//    		b2 = "3rd floor";
//    		}
//    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
//    		b2 = "2nd floor";
//    		}
//    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
//    		b2 = "3rd floor";
//
//    		}
//    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
//    		b2 = "4th floor";
//    		}
//    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
//    		+"</style></head>"
//    		+"<div style=\"background-color: white; color:black \">\n"
//    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
//    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
//    		+ " font-family: 'Arial' \n" + " ;\n"
//    		+ " \"\n" + " >\n"
//    		+ " [[hrname]] has cancelled your booking for workstation.</p>"
//    		+ " <p>Reason for disabling building/floor: [[rsn]] <p>\n"
//    		+ "<p> Kindly make new booking.</p>\n" + " </p>\n"
//    		+"<p>Please check the details of cancelled booking given below.</p>"
//    		+"<table><tr><th>Date</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
//    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
//    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
//    		+ " automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
//    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
//    		+ " Thanks & Regards, </p>\n"
//    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
//    		+ " </div>";
//    		content = content.replace("[[name]]", username);
//    		content = content.replace("[[workstation]]", wno);
//    		content = content.replace("[[building]]", b);
//    		content = content.replace("[[floor]]", b2);
//    		content = content.replace("[[date]]", Date);
//    		content = content.replace("[[Mail]]", hremail);
//    		content = content.replace("[[rsn]]", reason);
//    		content = content.replace("[[hrname]]", hrname);
//    		JSONObject obj = new JSONObject();
//    		obj.put("fromEmail", fromEmail);
//    		obj.put("toEmail", toEmail);
//    		obj.put("senderName", senderName);
//    		obj.put("subject", subject);
//    		obj.put("content", content);
//    		sendMailer(obj);
//    		}
    
    
    public void sendEmailtouser(String email, String name, int b1, int f, int w, String Date, String hremail,
            String reason) throws Exception {
        String toEmail = email;
        String fromEmail = "automatedworkstationassigner@gmail.com";
        String senderName = "Automated workstation assigner ";
        String subject = "Workstation booking cancelled";
        String hrname = erepo.findname(hremail);
        String username = name;
        String wno = Integer.toString(w);
        String b = "";
        if (b1 == 1) {
            b = "Main Building";
        }
        if (b1 == 2) {
            b = "SVC Building";
        }
        if (b1 == 3) {
            b = "Pasta Street Building";
        }
        if (b1 == 4) {
            b = "Roush Building";
        }
        if (b1 == 5) {
            b = "CCD Building";
        }
        String b2 = "";
        if (f == 1) {
            b2 = "1st floor";
        }
        if (f == 2) {
            b2 = "2nd floor";
        }
        if (f == 3) {
            b2 = "3rd floor";
        }
        if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
            b2 = "2nd floor";
        }
        if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
            b2 = "3rd floor";
        }
        if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
            b2 = "4th floor";
        }
        String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; text-align:center }"
        +"</style></head>"
       +"<div style=\"background-color: white; color:black  \">\n"
       + " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
       + " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
       + " font-family: 'Arial' \n" + " ;\n"
       + " \"\n" + " >\n"
       + " [[hrname]] has cancelled your booking for workstation.Kindly make new booking</p>"
       +"<p>Please check the details of cancelled booking given below.</p>"
       +"<table><tr><th>Date</th><th>Building</th><th>Floor</th><th>Workstation Number</th><th>Reason for disabling building/floor</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td style=text-align:center>[[workstation]]</td><td>[[rsn]]</td></tr></table>"
       + " <p style=\" text-align: left ;font-size:13px \">\n"
       + " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
       + " automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
       + " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
       + " Thanks & Regards, </p>\n"
       + " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
       + " </div>";
        content = content.replace("[[name]]", username);
        content = content.replace("[[workstation]]", wno);
        content = content.replace("[[building]]", b);
        content = content.replace("[[floor]]", b2);
        content = content.replace("[[date]]", Date);
        content = content.replace("[[Mail]]", hremail);
        content = content.replace("[[rsn]]", reason);
        content = content.replace("[[hrname]]", hrname);
        JSONObject obj = new JSONObject();
        obj.put("fromEmail", fromEmail);
        obj.put("toEmail", toEmail);
        obj.put("senderName", senderName);
        obj.put("subject", subject);
        obj.put("content", content);
        sendMailer(obj);
    }
    
    

//    		public void sendEmailtouserforDisablingMeetingroom(String email, String name, int b1, int f, int w, String Date,
//    		String edate, String hremail, String reason) throws Exception {
//    		String toEmail = email;
//    		String fromEmail = "automatedworkstationassigner@gmail.com";
//    		String senderName = "Automated workstation assigner ";
//    		String subject = "Meeting room booking cancelled";
//    		String hrname = erepo.findname(hremail);
//
//    		String username = name;
//    		String wno = Integer.toString(w);
//    		String b = "";
//    		if (b1 == 1) {
//    		b = "Main Building";
//    		}
//    		if (b1 == 2) {
//    		b = "SVC Building";
//    		}
//    		if (b1 == 3) {
//    		b = "Pasta Street Building";
//    		}
//    		if (b1 == 4) {
//    		b = "Roush Building";
//    		}
//    		if (b1 == 5) {
//    		b = "CCD Building";
//    		}
//    		String b2 = "";
//    		if (f == 1) {
//    		b2 = "1st floor";
//    		}
//    		if (f == 2) {
//    		b2 = "2nd floor";
//    		}
//    		if (f == 3) {
//    		b2 = "3rd floor";
//    		}
//    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
//    		b2 = "2nd floor";
//    		}
//    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
//    		b2 = "3rd floor";
//
//    		}
//    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
//    		b2 = "4th floor";
//    		}
//    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
//    		+"</style></head>"
//    		+"<div style=\"background-color: white; color:black \">\n"
//    		+ " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
//    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
//    		+ " font-family: 'Arial' \n" + " ;\n"
//    		+ " \"\n" + " >\n"
//    		+ " [[hrname]] has cancelled your booking for meeting room.</p>"
//    		+ " <p>Reason for disabling meeting room: [[rsn]] <p>\n"
//    		+ "<p> Kindly make new booking.</p>\n" + " </p>\n"
//    		+"<p>Please check the details of cancelled booking given below.</p>"
//    		+"<table><tr><th>From</th><th>To</th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
//    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
//    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
//    		+ " automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
//    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
//    		+ " Thanks & Regards, </p>\n"
//    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
//    		+ " </div>";
//    		content = content.replace("[[name]]", username);
//    		content = content.replace("[[workstation]]", wno);
//    		content = content.replace("[[building]]", b);
//    		content = content.replace("[[floor]]", b2);
//    		content = content.replace("[[date]]", Date);
//    		content = content.replace("[[end]]", edate);
//    		content = content.replace("[[Mail]]", hremail);
//    		content = content.replace("[[rsn]]", reason);
//    		content = content.replace("[[hrname]]", hrname);
//    		// content = content.replace("[[otp]]", OTPsent);
//    		JSONObject obj = new JSONObject();
//    		obj.put("fromEmail", fromEmail);
//    		obj.put("toEmail", toEmail);
//    		obj.put("senderName", senderName);
//    		obj.put("subject", subject);
//    		obj.put("content", content);
//    		sendMailer(obj);
//    		}



    		public void sendEmailtouserforCancellingMeetingroom(String email, String name, String sdate, String edate,
    		String stime, String etime, String mail, int b1, int f, int w) throws Exception {

    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Meeting room booking cancelled";
    		String hrname = erepo.findname(mail);

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head>"
    		+"<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has cancelled booking for meeting room .Please check the details of cancelled booking given below.</p>"

    		+"<table><tr><th>From date</th><th>To date</th><th>Start time</th><th>End time</th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[stime]]</td><td>[[etime]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", sdate);
    		content = content.replace("[[end]]", edate);
    		content = content.replace("[[Mail]]", mail);
    		content = content.replace("[[stime]]", stime);
    		content = content.replace("[[hrname]]", hrname);
    		content = content.replace("[[etime]]", etime);

    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouserforCancellingMeetingroomWithoutTime(String email, String name, String sdate,
    		String edate, String mail, int b1, int f, int w) throws Exception {

    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Meeting room booking cancelled";
    		String hrname = erepo.findname(mail);

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head>"
    		+"<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has booked meeting room for you.Please check the details given below.</p>"

    		+"<table><tr><th>From </th><th>To </th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", sdate);
    		content = content.replace("[[end]]", edate);
    		content = content.replace("[[Mail]]", mail);
    		content = content.replace("[[hrname]]", hrname);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouserforMeetingroom(String email, String name, String sdate, String edate, String stime,
    		String etime, String mail, int b1, int f, int w) throws Exception {
    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Meeting room booked";
    		String pmname = erepo.findname(mail);

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head>"
    		+"<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[pmname]] has booked meeting room for you.Please check the details given below.</p>"

    		+"<table><tr><th>From date</th><th>To date</th><th>Start time</th><th>End time</th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[stime]]</td><td>[[etime]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";

    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", sdate);
    		content = content.replace("[[end]]", edate);
    		content = content.replace("[[Mail]]", mail);
    		content = content.replace("[[stime]]", stime);
    		content = content.replace("[[etime]]", etime);
    		content = content.replace("[[pmname]]", pmname);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}
    		public void sendEmailtoTeamMembers(String email,String name ,int b1,int f,int w,String sdate,String edate ,String etime,String stime,String hremail) throws Exception {
    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Meeting room booking changed";
    		String pmname=emailRepository.findname(hremail);
    		int i=email.indexOf("@");
    		String username=name;
    		String wno=Integer.toString(w);
    		String b="";
    		if(b1==1)
    		{
    		b="Main Building";
    		}
    		if(b1==2){
    		b="SVC Building";
    		}
    		if(b1==3)
    		{
    		b="Pasta Street Building";
    		}
    		if(b1==4)
    		{
    		b="Roush Building";
    		}
    		if(b1==5)
    		{
    		b="CCD Building";
    		}
    		String b2="";
    		if(f==1)
    		{
    		b2="1st floor";
    		}
    		if(f==2){
    		b2="2nd floor";
    		}
    		if(f==3)
    		{
    		b2="3rd floor";
    		}
    		if((b1==1&&f==1)||(b1==3&&f==1))
    		{
    		b2="2nd floor";
    		}
    		if((b1==1&&f==2)||(b1==3&&f==2))
    		{
    		b2="3rd floor";
    		}
    		if((b1==1&&f==3)||(b1==3&&f==3))
    		{
    		b2="4th floor";
    		}
    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head>"
    		+"<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:1px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[pmname]] has changed your booking for meeting room .Please check the updated details given below.</p>"

    		+"<table><tr><th>From date</th><th>To date</th><th>Start time</th><th>End time</th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[startdate]]</td><td>[[enddate]]</td><td>[[starttime]]</td><td>[[endtime]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]",wno);
    		content =content.replace("[[building]]",b);
    		content =content.replace("[[floor]]",b2);
    		content =content.replace("[[startdate]]",sdate);
    		content =content.replace("[[enddate]]",edate);
    		content =content.replace("[[starttime]]",stime);
    		content =content.replace("[[endtime]]",etime);
    		content=content.replace("[[pmname]]",pmname);
    		content =content.replace("[[Mail]]",hremail);
    		// content =content.replace("[[rsn]]", reason);
    		//content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

//    		public void sendEmailtouserforDisablingMeetingroom(String email, String name, int b1, int f, int w, String Date,
//    		String edate, String hremail, String reason) throws Exception {
//    		String toEmail = email;
//    		String fromEmail = "automatedworkstationassigner@gmail.com";
//    		String senderName = "Automated workstation assigner ";
//    		String subject = "Meeting room booking cancelled";
//    		String hrname = erepo.findname(hremail);
//
//    		String username = name;
//    		String wno = Integer.toString(w);
//    		String b = "";
//    		if (b1 == 1) {
//    		b = "Main Building";
//    		}
//    		if (b1 == 2) {
//    		b = "SVC Building";
//    		}
//    		if (b1 == 3) {
//    		b = "Pasta Street Building";
//    		}
//    		if (b1 == 4) {
//    		b = "Roush Building";
//    		}
//    		if (b1 == 5) {
//    		b = "CCD Building";
//    		}
//    		String b2 = "";
//    		if (f == 1) {
//    		b2 = "1st floor";
//    		}
//    		if (f == 2) {
//    		b2 = "2nd floor";
//    		}
//    		if (f == 3) {
//    		b2 = "3rd floor";
//    		}
//    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
//    		b2 = "2nd floor";
//    		}
//    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
//    		b2 = "3rd floor";
//
//    		}
//    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
//    		b2 = "4th floor";
//    		}
//    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
//    		+"</style></head>"
//    		+"<div style=\"background-color: white; color:black \">\n"
//    		+ " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
//    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
//    		+ " font-family: 'Arial' \n" + " ;\n"
//    		+ " \"\n" + " >\n"
//    		+ " [[hrname]] has cancelled your booking for meeting room.</p>"
//    		+ " <p>Reason for disabling meeting room: [[rsn]] <p>\n"
//    		+ "<p> Kindly make new booking.</p>\n" + " </p>\n"
//    		+"<p>Please check the details of cancelled booking given below.</p>"
//    		+"<table><tr><th>From</th><th>To</th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
//    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
//    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
//    		+ " automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
//    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
//    		+ " Thanks & Regards, </p>\n"
//    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
//    		+ " </div>";
//    		content = content.replace("[[name]]", username);
//    		content = content.replace("[[workstation]]", wno);
//    		content = content.replace("[[building]]", b);
//    		content = content.replace("[[floor]]", b2);
//    		content = content.replace("[[date]]", Date);
//    		content = content.replace("[[end]]", edate);
//    		content = content.replace("[[Mail]]", hremail);
//    		content = content.replace("[[rsn]]", reason);
//    		content = content.replace("[[hrname]]", hrname);
//    		// content = content.replace("[[otp]]", OTPsent);
//    		JSONObject obj = new JSONObject();
//    		obj.put("fromEmail", fromEmail);
//    		obj.put("toEmail", toEmail);
//    		obj.put("senderName", senderName);
//    		obj.put("subject", subject);
//    		obj.put("content", content);
//    		sendMailer(obj);
//    		}
    		
    		
    		public void sendEmailtouserforDisablingMeetingroom(String email, String name, int b1, int f, int w, String Date,
    	            String edate, String hremail, String reason) throws Exception {
    	        String toEmail = email;
    	        String fromEmail = "automatedworkstationassigner@gmail.com";
    	        String senderName = "Automated workstation assigner ";
    	        String subject = "Meeting room booking cancelled";
    	        String hrname = erepo.findname(hremail);
    	        String username = name;
    	        String wno = Integer.toString(w);
    	        String b = "";
    	        if (b1 == 1) {
    	            b = "Main Building";
    	        }
    	        if (b1 == 2) {
    	            b = "SVC Building";
    	        }
    	        if (b1 == 3) {
    	            b = "Pasta Street Building";
    	        }
    	        if (b1 == 4) {
    	            b = "Roush Building";
    	        }
    	        if (b1 == 5) {
    	            b = "CCD Building";
    	        }
    	        String b2 = "";
    	        if (f == 1) {
    	            b2 = "1st floor";
    	        }
    	        if (f == 2) {
    	            b2 = "2nd floor";
    	        }
    	        if (f == 3) {
    	            b2 = "3rd floor";
    	        }
    	        if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    	            b2 = "2nd floor";
    	        }
    	        if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    	            b2 = "3rd floor";
    	        }
    	        if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    	            b2 = "4th floor";
    	        }
    	        String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; text-align:center}"
    	        +"</style></head>"
    	       +"<div style=\"background-color: white; color:black  \">\n"
    	       + " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
    	       + " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    	       + " font-family: 'Arial' \n" + " ;\n"
    	       + " \"\n" + " >\n"
    	       + " [[hrname]] has cancelled your booking for meeting room.Kindly make new booking</p>"
    	       +"<p>Please check the details of cancelled booking given below.</p>"
    	       +"<table><tr><th>From</th><th>To</th><th>Building</th><th>Floor</th><th>Meeting room Number</th><th>Reason for disabling meeting room</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td><td>[[rsn]]</td></tr></table>"
    	       + " <p style=\" text-align: left ;font-size:13px \">\n"
    	       + " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    	       + " automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    	       + " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    	       + " Thanks & Regards, </p>\n"
    	       + " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    	       + " </div>";
    	        content = content.replace("[[name]]", username);
    	        content = content.replace("[[workstation]]", wno);
    	        content = content.replace("[[building]]", b);
    	        content = content.replace("[[floor]]", b2);
    	        content = content.replace("[[date]]", Date);
    	        content = content.replace("[[end]]", edate);
    	        content = content.replace("[[Mail]]", hremail);
    	        content = content.replace("[[rsn]]", reason);
    	        content = content.replace("[[hrname]]", hrname);
    	        // content = content.replace("[[otp]]", OTPsent);
    	        JSONObject obj = new JSONObject();
    	        obj.put("fromEmail", fromEmail);
    	        obj.put("toEmail", toEmail);
    	        obj.put("senderName", senderName);
    	        obj.put("subject", subject);
    	        obj.put("content", content);
    	        sendMailer(obj);
    	    }
    		

    		public void sendEmailtouser5(String email, String name, int b1, int f, int w, String sdate, String edate,
    		String etime, String stime, String hremail) throws Exception {
    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Meeting room booking overriden";
    		String hrname = erepo.findname(hremail);

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head>"
    		+"<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has cancelled your booking for meeting room .Please check the details of cancelled booking given below.</p>"
    		+"<table><tr><th>From date</th><th>To date</th><th>Start time</th><th>End time</th><th>Building</th><th>Floor</th><th>Meeting room Number</th></tr><tr><td>[[startdate]]</td><td>[[enddate]]</td><td>[[starttime]]</td><td>[[endtime]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[startdate]]", sdate);
    		content = content.replace("[[enddate]]", edate);
    		content = content.replace("[[starttime]]", stime);
    		content = content.replace("[[endtime]]", etime);
    		content = content.replace("[[hrname]]", hrname);
    		content = content.replace("[[Mail]]", hremail);
    		// content =content.replace("[[rsn]]", reason);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouserwithoutReason(String email, String name, int b1, int f, int w, String Date,
    		String hremail) throws Exception {
    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Workstation booking cancelled";
    		String hrname = erepo.findname(hremail);

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " Your booking for workstation is cancelled by HR.Please check the details of cancelled booking given below and kindly make new booking</p>"
    		+"<head>"
    		+"<style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head><body>"
    		+"<table><tr><th>Date</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", Date);
    		content = content.replace("[[Mail]]", hremail);
    		content = content.replace("[[hrname]]", hrname);

    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouser2(String email, String name, int b1, int f, int w, String Date, String pmemail)
    		throws Exception {

    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Workstation booking cancelled";

    		String hrname = erepo.findname(pmemail);

    		int i = email.indexOf("@");
    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has cancelled your booking for workstation .Please check the details of cancelled booking given below.</p>"
    		+"<head>"
    		+"<style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head><body>"
    		+"<table><tr><th>Date</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", Date);
    		content = content.replace("[[Mail]]", pmemail);
    		content = content.replace("[[hrname]]", hrname);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouser1(String email, String name, int wn, String Date, String edate, String pmemail)
    		throws Exception {
    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Workstations Booked ";

    		String username = name;
    		String s1 = Integer.toString(wn);
    		String wno = s1.substring(2, s1.length());
    		String hrname = erepo.findname(pmemail);

    		char c1 = s1.charAt(0);
    		String b = "";
    		if (c1 == '1') {
    		b = "Main Building";
    		}
    		if (c1 == '2') {
    		b = "SVC Building";
    		}
    		if (c1 == '3') {
    		b = "Pasta Street Building";
    		}
    		if (c1 == '4') {
    		b = "Roush Building";
    		}
    		if (c1 == '5') {
    		b = "CCD Building";
    		}
    		char c2 = s1.charAt(1);
    		String b2 = "";

    		if (c2 == '1') {
    		b2 = "1st floor";
    		}
    		if (c2 == '2') {
    		b2 = "2nd floor";
    		}
    		if (c2 == '3') {
    		b2 = "3rd floor";

    		}
    		if ((c1 == '1' && c2 == '1') || (c1 == '3' && c2 == '1')) {
    		b2 = "2nd floor";
    		}
    		if ((c1 == '1' && c2 == '2') || (c1 == '3' && c2 == '2')) {
    		b2 = "3rd floor";

    		}
    		if ((c1 == '1' && c2 == '3') || (c1 == '3' && c2 == '3')) {
    		b2 = "4th floor";
    		}

    		String content ="<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head>"
    		+"<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has booked workstation for you.Please check the details given below.</p>"

    		+"<table><tr><th>From</th><th>To</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[end]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", Date);
    		content = content.replace("[[end]]", edate);
    		content = content.replace("[[Mail]]", pmemail);
    		content = content.replace("[[hrname]]", hrname);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouser3(String email, String name, int b1, int f, int w, String Date, String pmemail)
    		throws Exception {

    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Workstation Booking changed ";
    		String hrname = erepo.findname(pmemail);
    		int i = email.indexOf("@");
    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";

    		}

    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has changed your booking for workstation.Please check the updated booking details given below.</p>"
    		+"<head>"
    		+"<style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head><body>"
    		+"<table><tr><th>From</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";

    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", Date);
    		content = content.replace("[[Mail]]", pmemail);
    		content = content.replace("[[hrname]]", hrname);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouserWhenOverridingWorkstation(String email, String name, int b1, int f, int w, String sdate,
    		String edate, String hremail) throws Exception {

    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Workstation booking overriden";
    		String hrname = erepo.findname(hremail);

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has cancelled your booking for workstation .Please check the details of cancelled booking given below.</p>"
    		+"<head>"
    		+"<style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head><body>"
    		+"<table><tr><th>Date</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";
    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", sdate);
    		content = content.replace("[[edate]]", edate);
    		content = content.replace("[[Mail]]", hremail);

    		content = content.replace("[[hrname]]", hrname);
    		// content =content.replace("[[rsn]]", reason);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		public void sendEmailtouserWhenOverridingWorkstation(String email, String name, int b1, int f, int w, String sdate,
    		String hremail) throws Exception {

    		String toEmail = email;
    		String fromEmail = "automatedworkstationassigner@gmail.com";
    		String senderName = "Automated workstation assigner ";
    		String subject = "Meeting room booking overriden";

    		String username = name;
    		String wno = Integer.toString(w);
    		String b = "";
    		if (b1 == 1) {
    		b = "Main Building";
    		}
    		if (b1 == 2) {
    		b = "SVC Building";
    		}
    		if (b1 == 3) {
    		b = "Pasta Street Building";
    		}
    		if (b1 == 4) {
    		b = "Roush Building";
    		}
    		if (b1 == 5) {
    		b = "CCD Building";
    		}
    		String b2 = "";
    		if (f == 1) {
    		b2 = "1st floor";
    		}
    		if (f == 2) {
    		b2 = "2nd floor";
    		}
    		if (f == 3) {
    		b2 = "3rd floor";
    		}
    		if ((b1 == 1 && f == 1) || (b1 == 3 && f == 1)) {
    		b2 = "2nd floor";
    		}
    		if ((b1 == 1 && f == 2) || (b1 == 3 && f == 2)) {
    		b2 = "3rd floor";

    		}
    		if ((b1 == 1 && f == 3) || (b1 == 3 && f == 3)) {
    		b2 = "4th floor";
    		}
    		String content = "<div style=\"background-color: white; color:black \">\n"
    		+ " <p style=\"text-align: left; font-size:13px ;\">Hi [[name]],</p>\n"
    		+ " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
    		+ " font-family: 'Arial' \n" + " ;\n"
    		+ " \"\n" + " >\n"
    		+ " [[hrname]] has cancelled your booking for workstation .Please check the details of cancelled booking given below.</p>"
    		+"<head>"
    		+"<style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
    		+"</style></head><body>"
    		+"<table><tr><th>Date</th><th>Building</th><th>Floor</th><th>Workstation Number</th></tr><tr><td>[[date]]</td><td>[[building]]</td><td>[[floor]]</td><td>[[workstation]]</td></tr></table>"
    		+ " <p style=\" text-align: left ;font-size:13px \">\n"
    		+ " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
    		+ " >automatedworkstationassigner@gmail.com</span\n" + " >\n" + " </p>\n"
    		+ " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
    		+ " Thanks & Regards, </p>\n"
    		+ " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">Automated workstation assigner team</span\n"
    		+ " </div>";

    		content = content.replace("[[name]]", username);
    		content = content.replace("[[workstation]]", wno);
    		content = content.replace("[[building]]", b);
    		content = content.replace("[[floor]]", b2);
    		content = content.replace("[[date]]", sdate);

    		content = content.replace("[[Mail]]", hremail);
    		// content =content.replace("[[rsn]]", reason);
    		// content = content.replace("[[otp]]", OTPsent);
    		JSONObject obj = new JSONObject();
    		obj.put("fromEmail", fromEmail);
    		obj.put("toEmail", toEmail);
    		obj.put("senderName", senderName);
    		obj.put("subject", subject);
    		obj.put("content", content);
    		sendMailer(obj);
    		}

    		private void sendMailer(JSONObject obj) throws MessagingException, JSONException, UnsupportedEncodingException {
    		MimeMessage message = mailSender.createMimeMessage();
    		MimeMessageHelper helper = new MimeMessageHelper(message);
    		helper.setFrom(obj.get("fromEmail").toString(), obj.get("senderName").toString());
    		helper.setTo(obj.get("toEmail").toString());
    		helper.setText(obj.get("content").toString(), true);
    		helper.setSubject(obj.get("subject").toString());
    		System.out.println(message);
    		mailSender.send(message);
    		}

    		

    public boolean checkdatescollide(String st1,String et1,String st2,String et2) throws Exception
    {
        //create object of SimpleDateFormat
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
        
        Date s11=s.parse(st1);
        Date e11=s.parse(et1);
        Date s12=s.parse(st2);
        Date e12=s.parse(et2);

        if(s12.compareTo(e12) > 0) throw new InvalidDateException();

      Boolean b= ! (s11.before(s12) || s11.after(e12));
      Boolean b1=! (e11.before(s12) || e11.after(e12));
       Boolean b2= ! (s12.before(s11) || s12.after(e11));
      Boolean b3=! (e12.before(s11) || e12.after(e11));
   
      if((b==true || b1== true)||(b2==true || b3==true))
        return true;
       else
       return false;

}
    
    


public static boolean TimerangesCollideornot(String s1,String s2,String s3,String s4)throws Exception
{
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        
        LocalTime s11=LocalTime.parse(s1);
        LocalTime e11=LocalTime.parse(s2);
        LocalTime s12=LocalTime.parse(s3);
        LocalTime e12=LocalTime.parse(s4);
       long diffInMinutes = java.time.Duration.between(s12, e12).toMinutes();
      //   System.out.println(diffInMinutes);
         //System.out.println("yesssssssssssssssssssssssssssssssssssssssssssssssssss"+diffInMinutes);

       if(diffInMinutes<=15l)
      {

        throw new InvalidTimeException();
        //System.out.println("after");

      }

      Boolean b= ! (s11.isBefore(s12) || s11.isAfter(e12));
      Boolean b1=! (e11.isBefore(s12) || e11.isAfter(e12));
       Boolean b2= ! (s12.isBefore(s11) || s12.isAfter(e11));
      Boolean b3=! (e12.isBefore(s11) || e12.isAfter(e11));
      System.out.println(b2);
      System.out.print(b3);
      if((b==true || b1== true)||(b2==true || b3==true))
     return true;
       else
       return false;
     }



@Override
public ResponseEntity<Object> removeBulkBooking(Dummy_Confirm conf) {
	String[] str=new String[100];
	int[] wnum=new int[100];
	int m1,bdn,fln1;

	 String date;
	 System.out.println("\n starting:"+conf.getLdates().get(0));

	for(int dt=0;dt<conf.getLdates().size();dt++) {
			date=conf.getLdates().get(dt);
for(int h=0;h<conf.getEmail().size();h++) {
	str[h]=conf.getEmail().get(h);

	//System.out.println("\n size of wst \t"+conf.getWorkstation_id().size()+"email size:"+conf.getEmail().size());
// String date=conf.getDate();
 m1=crepo.getwid(str[h],date);
 bdn=crepo.getbid(str[h], date);
 fln1=crepo.getfid(str[h], date);
 String name=erepo.findname(str[h]);
 System.out.println("\n name:"+name);
 System.out.println("\n mail:"+str[h]);
	System.out.println("\n wnum:"+m1+" bdn:"+bdn+" fln1:"+fln1);
	System.out.println("\n date:"+date);
  if(crepo.selectbyfid(bdn,fln1,m1,date)==null)throw new WorkstationNotFoundException();
  Authentication obj = SecurityContextHolder.getContext().getAuthentication();
  System.out.println("\n object is:"+obj);
   String mail=(obj).getName();
    System.out.println("\n mail is:"+mail);
    User cuser=erepo.finduser(mail);
   System.out.println("\n user is:"+cuser.getEmail());
   Confirmation C=crepo.selectbyfid(bdn,fln1,m1,date);
    if(cuser.getRole().equalsIgnoreCase("project manager") || cuser.getRole().equalsIgnoreCase("employee"))
    {
    String e=C.getEmail();//this will get the email of the deleted data
    System.out.println("\n e:"+e);
   String na=C.getName();
    try
            {
            sendEmailtouser2(e,na,bdn,fln1,m1,date,mail);
            System.out.println("entered try ");
            }
            catch(Exception e1)
            {
                 System.out.print("mail not sent");
            }
    }
//    System.out.println(crepo.selectbyfid(bdn,fln1,m1,date));
   // System.out.println("dleeeee"+crepo.deletebyfid(bid, fid, wid, date));
   crepo.deletebyfid(bdn,fln1,m1,date);
}
}
  return new ResponseEntity<>("Workstation is deleted successfully", HttpStatus.OK);
    }
    @Override
    public ResponseEntity<String> updateWorkstationBooking(Dummy_Confirm  c )
    {

        Authentication obj = SecurityContextHolder.getContext().getAuthentication();
        String mail = obj.getName();
        User cuser=erepo.finduser(mail);
        //  String name=erepo.findname(cuser.getEmail());
        LocalDate localstartDate = LocalDate.parse(c.getDate(),formatter);
        LocalDate localendDate = LocalDate.parse(c.getDate_end(),formatter);
        String end=formatter.format(localendDate);
        localendDate=localendDate.plusDays(1);
        int f1=0,b1=0,w1=0;
        if(localendDate.compareTo(localstartDate)<0)
        {
            throw new InvalidDateException();
        }

        String sdate="";
        String edate="";
        String start=formatter.format(localstartDate);

        for(int k=0;k<c.getWorkstation_id().size();k++)
        {
            sdate = formatter.format(localstartDate);
            edate=formatter.format(localendDate);
            //2122


            String s=Integer.toString(c.getWorkstation_id().get(k));
            char b=s.charAt(0);
            b1=Character.getNumericValue(b);

            char f=s.charAt(1);
            f1=Character.getNumericValue(f);
            //   System.out.print(c);

            String w=s.substring(2,s.length());
            w1=Integer.parseInt(w);
            List<String> emails=crepo.findDistinctEmails(w1,f1,b1);
            for(int j=0;j<emails.size();j++)
            {
                String name=erepo.findname(emails.get(j)) ;
                try {
                    sendEmailtouserWhenOverridingWorkstation (emails.get(j),name , b1,f1 ,w1,sdate,cuser.getEmail());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        for(int i=0;i<c.getEmail().size();i++)

        {

            for (LocalDate date = localstartDate;date.isBefore(localendDate); date = date.plusDays(1))
            // Do your job here with `date`.
            {     //System.out.println(date);


                // System.out.println(w);
                String s=Integer.toString(c.getWorkstation_id().get(i));
                char b=s.charAt(0);
                b1=Character.getNumericValue(b);

                char f=s.charAt(1);
                f1=Character.getNumericValue(f);
                //   System.out.print(c);

                String w=s.substring(2,s.length());
                w1=Integer.parseInt(w);

                // System.out.print(c);

                String name=erepo.findname(c.getEmail().get(i));
                // System.out.println(b1);
                // System.out.println(f1);
                // System.out.println(w1);
                // System.out.println(c.getEmail().get(i));

                // System.out.println(name);
                crepo.updateWorkstation(c.getEmail().get(i),name,w1,f1,b1);
                List<String> emails=crepo.findDistinctEmails(w1,f1,b1);
                int count=0;
                System.out.println(emails);

                try{
                    //count++;
                    //System.out.print(count);
                    sendEmailtouser1(emails.get(i),name,c.getWorkstation_id().get(i),c.getDate(),c.getDate_end(),cuser.getEmail()) ;
                }catch(Exception e){

                }
            }

        }
        return null;
    }
@Override
    public List<Page> onDate(String date) {
	Date fdate;
	try {
		
		 SimpleDateFormat formates2 = new SimpleDateFormat("dd-MM-yyyy");
    Authentication obj = SecurityContextHolder.getContext().getAuthentication();
    String mail = obj.getName();
    LocalDate localendDate = LocalDate.parse(date, formatter);
    localendDate = localendDate.plusDays(1);
    String end = formatter.format(localendDate);
    List<Confirmation> conf = new ArrayList<>();
    conf = crepo.onDate(date,end,mail);
    System.out.println(conf);
    List<Page> P=new ArrayList<>();
    for(int i=0;i<conf.size();i++) {
    	Page pg=new Page();
    	pg.setBuilding_id(conf.get(i).getBuilding_id());
    	pg.setBuilding_name(conf.get(i).getBuilding_name());
    	pg.setFloor_id(conf.get(i).getFloor_id());
    	pg.setFloor_name(conf.get(i).getFloor_name());
    	pg.setId(conf.get(i).getId());
    	 fdate=new SimpleDateFormat("yyyy-MM-dd").parse(conf.get(i).getDate());
	        String finalDate=formates2.format(fdate);
	        System.out.println("\n The Date :"+conf.get(i).getDate()+"\n The Changed Date:"+finalDate);         
	        pg.setDate(finalDate);
    	pg.setEmail(conf.get(i).getEmail());
    	pg.setWorkstation_id(conf.get(i).getWorkstation_id());
    	pg.setName(conf.get(i).getName());
    	pg.setTotalrecords(conf.size());
    	P.add(pg);
    	}
    return P;
    
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}


//@Override
//    public List<conf_confirmation> roomDate(String date){
//    Authentication obj = SecurityContextHolder.getContext().getAuthentication();
//    String mail = obj.getName();
//    User user = erepo.finduser(mail);
//
//    LocalDate localendDate = LocalDate.parse(date, formatter);
//    localendDate = localendDate.plusDays(1);
//    String end = formatter.format(localendDate);
//    List<conf_confirmation> conf = new ArrayList<>();
//    return conf;
//}

}