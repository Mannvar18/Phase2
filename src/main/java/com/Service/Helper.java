package com.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Model.User;
import com.Repository.EmailRepository;

import com.exception.AutoworkExceptions;
import com.exception.BackendException;
@Service
public class Helper {
	
	@Autowired
	EmailRepository erepo;
	public static int colLen=4;	
	
	
	public static boolean checkExcelFormat(MultipartFile file) {
		
		String contentType = file.getContentType();
		// to check the type is of excel or not
		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public Map<String,List<User>> convertExcelToList(InputStream is){
		List<User> list=new ArrayList<>();

		try {
			int rowcount=1,cellcount=0;
			
			XSSFWorkbook workbook =new XSSFWorkbook(is);
			XSSFSheet sheet=workbook.getSheet("d1");
			System.out.println("\n Physical  count"+sheet.getPhysicalNumberOfRows()+"\n Last:"+sheet.getLastRowNum());
			int rowNumber=0;
			Iterator<Row> iterator=sheet.iterator();
			int last=sheet.getPhysicalNumberOfRows();
			int i=0,j=0;
			int UserRecords=erepo.getUserRecords();
			int ct= UserRecords+1;
			String[] arr=new String[20];
			while(true) {
				if(rowNumber>=last) {
					break;
				}
				
				if(!iterator.hasNext()) {
					iterator.next();
					continue;
				}
				if(rowNumber==0){
					rowNumber++;
					iterator.next();
					continue;
				}
				
				Row row=iterator.next();				
				
				System.out.println("\n Executed row");
				Iterator<Cell> cells=row.iterator();
				
				int cid=0;
				User u=new User();
				
				while(true) {
					if(cid>=colLen) {
						break;
					}
					if(!cells.hasNext()) {
						System.out.println("\n DATA IS MISSING IN THE CELLS");
						throw new BackendException("701","Data is missing in the row:"+rowcount+" cell:"+cellcount);
						
					}
					Cell cell=cells.next();
					u.setId(ct);
//					if(cid==0) {
//						ct++;
//					}
					u.setIsbooked(false);
					u.setPassword("$2a$10$UQtSt99yyQmRK6ggWvCXKe9hBcEGY4w8Yqir5mR2Xs3PSGyUWx2Oy");
					System.out.println("\n Executed cell");
					
					switch(cid) {
					
					
					case 0:
						CellType q1=cell.getCellType();						
						String sp1=q1.toString();	
						System.out.println("\n Data type in cell:"+sp1);
						if(sp1.equalsIgnoreCase("string")) {
							String p1=cell.getStringCellValue();
							if(p1.endsWith(".com")) {
								u.setEmail(cell.getStringCellValue());
								break;
							}
							break;
						}
						else {
							System.out.println("\n Enter a valid value in 0");
							break;
						}
					case 1:
							CellType q2=cell.getCellType();						
							String sp2=q2.toString();	
							System.out.println("\n Data type in cell:"+sp2);
							String p2=cell.getStringCellValue();
							if(p2.equalsIgnoreCase("HR") || p2.equalsIgnoreCase("Employee") || p2.equalsIgnoreCase("Project Manager")) {
								u.setRole(cell.getStringCellValue());
								arr[j]=p2;
								break;
								}
							
							else {
								System.out.println("\n IN THE ROLE EXCEPTION");
								throw new BackendException("708","Give a correct role in the row:"+rowcount+" cell:"+cellcount);
								}
					case 2:
							CellType q3=cell.getCellType();						
							String sp3=q3.toString();	
							System.out.println("\n Data type in cell:"+sp3);
							if(arr[j].equalsIgnoreCase("HR") && sp3.equalsIgnoreCase("BLANK")) {
								u.setProject_name("NULL");
								break;
							}
							if(sp3.equalsIgnoreCase("string")) {
							String p3=cell.getStringCellValue();
							u.setProject_name(p3);
							break;
								}
							
						else {
							System.out.println("\n Enter a valid value in 2");
							break;
						}
					case 3:CellType q4=cell.getCellType();						
							String sp4=q4.toString();	
							System.out.println("\n Data type in cell:"+sp4);
						if(sp4.equalsIgnoreCase("string")) {
							String p4=cell.getStringCellValue();
							u.setName(p4);
							break;
							}
						else {
							System.out.println("\n Enter a valid value in 3");
							break;
							}
			
					default:
						System.out.println("\n IN DEFAULT CASE");
						break;
					
					}
					cid++;
					cellcount++;
				}
				list.add(u);
				String E_mail=list.get(i).getEmail();
				String Project=list.get(i).getProject_name();
				System.out.println("\n THE EMAIL IS:"+E_mail+"  THE PROJECT IS:"+Project+" I VALUE:"+i);
				int records=erepo.getCountOfRecords(E_mail, Project);
				if(records>0) {
					System.out.println("\n----- INSIDE IF BLOCK ----");
					list.remove(i);
					rowNumber++;
					rowcount++;
					ct++;
					continue;
				}
				ct++;
				rowNumber++;
				rowcount++;
				i++;
				
			}
		
		}catch(BackendException e) {
			throw new BackendException(e.getErrCode(),e.getErrMsg());
		}
		catch(Exception e) {
			throw new BackendException("702","Something went wrong in the service layer");
		}
		System.out.println("\n THE USER DATA LIST:"+list.get(0).getEmail());

		Map<String,List<User>> hash=new HashMap<>();
		hash.put("data1", list);
		System.out.println("\n END OF 1ST BLOCK");
		return hash;
		
	}
	
	
}
