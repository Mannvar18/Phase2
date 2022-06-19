package com.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Repository.Workstationrepo;
import com.Service.ServiceInterface;
import com.Service.ServiceImpl;
import com.Service.userDetailsService;
import com.Util.jwtUtil;
import com.exception.AutoworkExceptions;
import com.exception.BackendException;

import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.type.DateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;

import com.Model.Building;
import com.Model.Confirmation;
import com.Model.Floor;
import com.Model.User;
import com.Model.Workstation;
import com.Model.meeting;
import com.Repository.Buildrepo;
import com.Repository.ConfirmationRepo;
import com.Repository.EmailRepository;

import com.Repository.Floorrepo;
import com.Service.*;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class buildingController {
	@Autowired
	Buildrepo repo;
	
	@Autowired
	Floorrepo repo2;
	
	@Autowired
	Workstationrepo repo3;//adding both the building and floor details at a time

	@Autowired
	ServiceInterface ser;

	@Autowired
	ServiceImpl se;

	@Autowired
	ConfirmationRepo conf;
    
	@Autowired
	EmailRepository erepo;

	@Autowired
	ServiceImpl es;
	

@ApiOperation("To get the details of the buildings,floors and workstations")
@GetMapping("/all_buildings")  //name of the building and the number of workstations
public List<Building> getAllDetails(@RequestParam String date) 
{

	return es.find_building_details(date);
}
	

@GetMapping("/details")
public List<Building> getDetails(@RequestParam("start_date") String startDate,@RequestParam("end_date") String endDate,@RequestParam(value = "report",required = false) String report,@RequestParam(value = "start_time",required = false) String start_time,@RequestParam(value = "end_time",required = false) String end_time)
{
  return   es. getBuildingForRangeofDates( startDate,endDate,report,start_time,end_time);

}

// @GetMapping("v1/meeting-room-details-for-single-day")
// public List<Building> getMeetingRoom(@RequestParam("date")String date)
// {
// 	return es.getsingleDayDetails(date);
// }



@ApiOperation("Disabling building or floor by HR")	
@PutMapping(value="/DisableBFstate")
public ResponseEntity<?> Disable(@RequestParam("num") int num,@RequestParam("reason") String reason)
{

return ser. disablingBuildingFloor(num,reason);

}
@ApiOperation("Enabling building or floor by HR")
@PutMapping(value="/EnableBFstate")
	public ResponseEntity<?> Enable(@RequestParam("num1") int num1)
	{
		return ser.enablingBuildingFloor(num1);

	}

}