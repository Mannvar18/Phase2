package com.Controller;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Model.User;
import com.Repository.EmailRepository;


import com.Service.Helper;
import com.exception.AutoworkExceptions;
import com.exception.BackendException;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ExcelController {
	@Autowired
	public EmailRepository erepo;

	@Autowired
	public Helper help;
	@PostMapping("/upload")
	public  ResponseEntity<?> save(@RequestParam("file") MultipartFile file) {
		if(Helper.checkExcelFormat(file)){
		try {
			List<User> userdata=help.convertExcelToList(file.getInputStream()).get("data1");
			System.out.println("\n THE USERDATA LIST:"+userdata.get(0));
			this.erepo.saveAll(userdata);
			System.out.println("\n BEFORE THE UPDATE FUNCTION BLOCK THIS IS UPDATE BEFORE");
			return ResponseEntity.ok(Map.of("message", "File is Uploaded"));
			
			}catch(BackendException e) {
				AutoworkExceptions ae=new AutoworkExceptions(e.getErrCode(),e.getErrMsg());
				return new ResponseEntity<AutoworkExceptions>(ae,HttpStatus.BAD_REQUEST);
			} catch(Exception e) {
				
				AutoworkExceptions ae=new AutoworkExceptions("703","You have re-uploaded the same file");
				return new ResponseEntity<AutoworkExceptions>(ae,HttpStatus.BAD_REQUEST);
				//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("YOU HAVE RE-UPLOADED THE SAME FILE");
			}
			//return null;
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Upload Excel file only");
		}
	}
	
	@GetMapping("/GetUser/{offset}/{size}")
	public Page<User> GetUser(@PathVariable("offset") int offset,@PathVariable("size") int size){
		Page<User> data=erepo.findAll(PageRequest.of(offset, size));
		return data;
	}


}