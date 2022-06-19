package com.Repository;

import com.Model.Building;
import com.Model.Confirmation;
import com.Model.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;
@Repository
public interface ConfirmationRepo extends JpaRepository<Confirmation,Integer> {
    User findByEmail(String email);
   
    @Query(value="select building_id from  confirmation where email=?1 and date=?2 and deleted='false'",nativeQuery = true)
    public int getbid( String email, String date);
   
    @Query(value="select floor_id from confirmation where email=?1 and date=?2 and deleted='false'",nativeQuery = true)
    public int getfid( String email,String date);

    @Query(value="select workstation_id from confirmation where email=?1 and date=?2",nativeQuery = true)
    public int getwid( String email,String date);

    @Modifying
    @Transactional
    @Query("update Confirmation set building_id=:bdn where email=:str and date=:date ")
    public void bupdate(@Param("bdn") int bdn,@Param("str") String str,@Param("date") String date);

    @Modifying
    @Transactional
    @Query("update Confirmation set floor_id=:fln1 where email=:str and date=:date ")
    public void fupdate(@Param("fln1") int fln1,@Param("str") String str,@Param("date") String date);

    @Modifying
    @Transactional
    @Query("update Confirmation set workstation_id=:m1 where email=:str and date=:date")
    public void wupdate(@Param("m1") int m1,@Param("str") String str,@Param("date") String date);
    @Query(value = "select COUNT(*) from confirmation  where email=?1 and deleted='false'",nativeQuery = true)
    public int getByEmailCount(String email);
    @Query(value = "select * from confirmation  where email=?1 and deleted='false'",nativeQuery = true)
    List<Confirmation> getByEmail(String email,Pageable pageable);
    @Query(value = "select COUNT(*) from confirmation where email=?1 and date between ?2 and ?3 and deleted='false' ",nativeQuery = true)
    public int getByEmailDateCount(@Param("email")String email,@Param("sdate")String sdate,@Param("edate")String edate);
   @Query(value = "select * from confirmation where email=?1 and date between ?2 and ?3 and deleted='false' ",nativeQuery = true)
   List<Confirmation> getByEmailDate(@Param("email")String email,@Param("sdate")String sdate,@Param("edate")String edate,Pageable pageable);
    //    List<String> findEmail(@Param("name") String name);
//    @Query("select co from Confirmation co where co.email=:l and co.date=:date")
    @Query(value = "select * from confirmation where email=?1 and date between ?2 and ?3 and deleted='false' ",nativeQuery = true)
    public List<Confirmation> getUsingEmail(@Param("email") String email, @Param("start_date") String start_date, @Param("end_date") String end_date);

    @Modifying
    @Transactional
    @Query(value="DELETE  FROM confirmation WHERE building_id=?1 AND floor_id=?2 AND workstation_id=?3 and date=?4 and deleted='false'",nativeQuery = true)
  void deletebyfid(int building_id,int floor_id,int workstation_id,String date);
   //@Query(value="select * from Confirmation where")

   @Query(value="select * FROM confirmation WHERE building_id=?1 AND floor_id=?2 AND workstation_id=?3 and date=?4 and deleted='false'",nativeQuery = true)
   Confirmation selectbyfid(int building_id,int floor_id,int workstation_id,String date);

   @Query(value="select email from confirmation where building_id=?1 ",nativeQuery=true)
    public List<String>findbybuilding(int building_id);


    @Query(value="select email from confirmation where building_id=?1 and floor_id=?2",nativeQuery = true)
     public List<String>findbyfloorandbuilding(int building_id,int floor_id);

    @Query(value = "select email from confirmation where building_id=?1 and floor_id=?2 and workstation_id=?3",nativeQuery = true)
    public String findperson(int building_id,int floor_id,int workstation_id);


    @Query(value = "select count(*) from confirmation where email=?1 and deleted='false' and date between ?2 and ?3",nativeQuery = true)
    int findBooked(String email , String sdate, String edate);

    @Query("select co from Confirmation co where co.email=:l and co.date=:date")
    public List<Confirmation> getByNameDate(@Param("l") String l,@Param("date") String date);

//    @Query(value ="select count(*) from confirmation where email=?1 and date=?2 and deleted='false'",nativeQuery = true)
//    public int checkexisiting(@Param("email")String email,@Param("date")String date);
    @Query(value ="select * from confirmation where email=?1 and date=?2 and deleted='false'",nativeQuery = true)
    public Confirmation checkexisiting(String email,String date);
    @Query(value ="select count(*) from confirmation where email=?1 and date=?2 and deleted='true'",nativeQuery = true)
    public int findDuplicate(String email,String date);
    @Modifying
    @Transactional
    @Query(value ="delete from confirmation where email=?1 and date=?2 and deleted='true'",nativeQuery = true)
    public void deleteDuplicate(String email,String date);
    @Query("select co from Confirmation co where co.email=:l")
     List<List<Confirmation>> getByEmail2(@Param("l") String l);

     @Query(value = "select count(*) from confirmation where building_id=? and date=?",nativeQuery = true)
     int findBuild(int id , String date);
    @Query(value = "select count(DISTINCT workstation_id) from confirmation where building_id=? and date=?",nativeQuery = true)
    int findCountBuild(int id , String date);

     @Query(value = "select count(*) from confirmation where building_id=? and floor_id=? and date=?",nativeQuery = true)
    int findFloor(int bid,int id , String date);
    @Query(value = "select count(*) from confirmation where date between ?1 and ?2 and building_id=?3 and floor_id=?4",nativeQuery = true)
    int findFloorCount(String start,String end,int b_id, int f_id );

    @Query(value = "select email from confirmation where building_id=? and floor_id=? and workstation_id=? and date=? and deleted='false'",nativeQuery = true)
    String findWorkstation(int b_id, int f_id ,int w_id , String date);
    @Query(value = "select email from confirmation where building_id=? and floor_id=? and workstation_id=? and date=?",nativeQuery = true)
    String findForTrue(int b_id, int f_id ,int w_id , String date);

    @Query(value="select floor_id from confirmation group by building_id,floor_id,workstation_id",nativeQuery = true)
    List<Integer> findcountfloor();

    @Query(value="select count(*) from confirmation where date between ?1 and ?2 and building_id=?3",nativeQuery = true)
    int findcount(String sdate,String edate,int build);

    @Query(value="select building_id from confirmation group by building_id,floor_id,workstation_id",nativeQuery = true)
    List<Integer> findcountbuilding();


    @Modifying
@Transactional
@Query(value = "update confirmation set deleted='true' where building_id=?",nativeQuery = true)
void deleteBEntry(int id);
    @Modifying
    @Transactional
    @Query(value = "update confirmation set deleted='false' where building_id=?",nativeQuery = true)
    void returnBEntry(int id);

@Modifying
@Transactional
@Query(value = "update confirmation set deleted='true' where building_id=? and floor_id=?",nativeQuery = true)
void deleteFEntry(int bid ,int fid);

    @Modifying
    @Transactional
    @Query(value = "update confirmation set deleted='false' where building_id=? and floor_id=?",nativeQuery = true)
    void returnFEntry(int bid ,int fid);

@Modifying
@Transactional
@Query(value = "update confirmation set deleted='true' where building_id=? and floor_id=? and workstation_id=?",nativeQuery = true)
void deleteWEntry(int bid ,int fid ,int wid);

    @Modifying
    @Transactional
    @Query(value = "update confirmation set deleted='false' where building_id=? and floor_id=? and workstation_id=?",nativeQuery = true)
    void returnWEntry(int bid ,int fid ,int wid);

@Modifying
    @Transactional
    @Query("update Confirmation set building_name=:bdname where email=:str and date=:date")
    public void bdnameupdate(@Param("bdname") String bdname,@Param("str") String str,@Param("date") String date);

    @Modifying
    @Transactional
    @Query("update Confirmation set floor_name=:flname where email=:str and date=:date")
    public void flnameupdate(@Param("flname") String flname,@Param("str") String str,@Param("date") String date);


    @Query(value="select * from confirmation where building_id=?1 and deleted='false'",nativeQuery = true)
    public List<Confirmation> findpersonbybuilding(int building_id);

    @Query(value="select * from confirmation where building_id=?1 and floor_id=?2",nativeQuery = true)
   public List<Confirmation> findbypersonbyfloor(int building_id,int floor_id);


   @Query(value="select * from confirmation where building_id=?1 and floor_id=?2 and workstation_id=?3",nativeQuery = true)
   public List<Confirmation> findpersonbyworkstation(int building_id,int floor_id,int workstation_id);

   @Query(value = "select * from confirmation where date between ?1 and ?2 and deleted='false'",nativeQuery = true)
   public List<Confirmation> findAllByDate(String sdate, String edate);

@Query(value = "select count(*) from confirmation where date=?1 and building_id=?2 and floor_id=?3 and workstation_id=?4 and deleted='false'",nativeQuery = true)
   public int countAll(String start,int b,int f, int w);
    @Query(value = "select count(*) from confirmation where date=?1 and building_id=?2 and floor_id=?3 and workstation_id=?4",nativeQuery = true)
    public int countForReport(String start,int b,int f, int w);
    @Query("select count(*) from Confirmation where email=:str and date=:date and deleted='false'")
    public int getOnDateCount(@Param("str") String str,@Param("date") String date);

    @Query("select co.workstation_id from Confirmation co where co.email=:str and co.date=:date and deleted='false'")
    public int getWID(@Param("str") String str,@Param("date") String date);

    @Modifying
    @Transactional
    @Query(value="update confirmation set email=?1,name=?2 where workstation_id=?3 and floor_id=?4 and building_id=?5 and deleted='false'",nativeQuery = true)
    public void updateWorkstation(String email,String name,int workstation_id,int floor_id,int building_id);
    @Query(value="select distinct email from confirmation where workstation_id=?1 and floor_id=?2 and building_id=?3 and deleted='false'",nativeQuery = true)
    public List<String> findDistinctEmails(int w1,int  f1,int b1);
    @Query(value = "select count(*) from confirmation where date=?1 and building_id=?2 and floor_id=?3 and workstation_id=?4 and deleted='false'",nativeQuery = true)
    public int findUsingData(String date, int bid, int fid, int wid);
    @Modifying
    @Transactional
    @Query(value = "delete from confirmation where date=?1 and building_id=?2 and floor_id=?3 and workstation_id=?4 and deleted='false'",nativeQuery = true)
    void deleteDate(String date, int bid, int fid, int wid);
    @Modifying
    @Transactional
    @Query("update Confirmation set building_id=:bid,building_name=:bname,floor_name=:fname,floor_id=:fid,workstation_id=:wid,date=:dt where email=:str and date=:date ")
    public void newUpdate(@Param("bid") int bid,@Param("bname") String bname,@Param("fname") String fname,@Param("fid") int fid,@Param("wid") int wid,@Param("dt") String dt,@Param("str") String str,@Param("date") String date);
    @Query(value = "select * from confirmation where email=?3 and (date=?1 or date=?2) and deleted='false' ",nativeQuery = true)
    public List<Confirmation> onDate(String date, String end , String email);


    @Query(value="select * from confirmation where workstation_id=?1 and floor_id=?2 and building_id=?3 and date=?4 and deleted='false'",nativeQuery = true)
    public List<Confirmation> findBookings(int w1,int  f1,int b1,String date);
    
    @Query(value ="select * from confirmation where email=?1 and date=?2 and deleted='false'",nativeQuery = true)
    public Confirmation checkexisiting1(String email,String date);
    
    @Modifying
    @Transactional
    @Query(value="update confirmation set deleted='true' where date=?1",nativeQuery=true)
    public void removeOldDateBookings(String date);

}