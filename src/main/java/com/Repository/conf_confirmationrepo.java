package com.Repository;

import java.util.List;

import javax.transaction.Transactional;

import com.Model.Confirmation;
import com.Model.conf_confirmation;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface conf_confirmationrepo extends JpaRepository<conf_confirmation, Integer> {
    

 @Query(value="select * from conf_confirmation where  date=?1 and end_date=?2 and start_time=?3 and end_time=?4 and building_id=?5 and floor_id=?6 and conf_room=?7",nativeQuery=true)

 public conf_confirmation findUserby(String sdate,String edate,String stime,String etime,int bid,int fid,int room);
    @Query(value="select * from conf_confirmation where date=?1 and start_time=?2 and end_time=?3 and building_id=?4 and floor_id=?5 and conf_room=?6 and deleted='false'",nativeQuery=true)

    public List<conf_confirmation> findUserbyList(String sdate,String stime,String etime,int bid,int fid,int room);

    @Query(value="select * from conf_confirmation where date=?1 and end_date=?2 and conf_room=?3 and deleted='false'",nativeQuery = true)
    public List<conf_confirmation> findAllUserby(String sdate, String edate,int mid);



 @Modifying
 @Transactional
 @Query(value="update conf_confirmation set email=?1 ,name=?2 ,reason_for_booking =?3 , project_name=?11,booked_by=?12 where building_id=?4 and floor_id=?5 and conf_room=?6 and date=?7 and end_date=?8 and start_time=?9 and end_time=?10 and deleted='false'",nativeQuery = true)
 public void  updateUser(String email,String name, String reason,int bid,int fid,int room,String sdate,String edate,String stime,String etime,String pro,String bname);

 @Modifying
    @Transactional
    @Query(value = "update conf_confirmation set building_id=?2, floor_id=?3, conf_room=?4 where email=?1 and date=?5 and end_date=?6 and start_time=?7 and end_time=?8 ",nativeQuery = true)
    public void update(String email,int bid,int fid,int room,String sdate,String edate,String stime,String etime);


    @Modifying
    @Transactional
    @Query(value="delete from conf_confirmation where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and start_time=?8 and end_time=?9 and deleted='false' ",nativeQuery = true)
    public void  deleteUser(String email,String name,int bid,int fid,int room,String sdate,String edate,String stime,String etime);


    @Query(value="select * from conf_confirmation where building_id=?1 and floor_id=?2 and conf_room=?3",nativeQuery=true)
    public List<conf_confirmation> findpersonbymeetingroom(int building_id,int floor_id,int conf_room);
   

   @Modifying
   @Transactional
   @Query(value="update conf_confirmation set deleted='true' where email=?1 and building_id=?2 and floor_id=?3 and conf_room=?4",nativeQuery = true)
   public void deletePerson(String email,int building_id,int floor_id,int conf_room);

    @Modifying
    @Transactional
    @Query(value="update conf_confirmation set deleted='false' where building_id=?1 and floor_id=?2 and conf_room=?3",nativeQuery = true)
    public void returnPerson(int building_id,int floor_id,int conf_room);
    @Query(value = "select * from conf_confirmation where email=?1 and deleted='false'",nativeQuery = true)
    List<conf_confirmation> findByEmail(String email);
   @Query(value = "select * from conf_confirmation where email=?1 and (?2 between date and end_date or ?3 between date and end_date or date between ?2 and ?3 or end_date between ?2 and ?3) and deleted='false'",nativeQuery = true)
   List<conf_confirmation> findByEmailDate(String email,String sdate,String edate);
   @Query(value="select * from conf_confirmation where id=?1 and booked_by=?2 and project_name=?3",nativeQuery = true)
   conf_confirmation findUserOfteam(int id,String name,String pname);
    @Query(value = "select * from conf_confirmation where date between ?1 and ?2 and deleted='false'",nativeQuery = true)
    public List<conf_confirmation> findAllByDate(String sdate, String edate);
    @Query(value = "select reason_for_booking from conf_confirmation where building_id=?1 and floor_id=?2 and conf_room=?3 and (date between ?4 and ?5 or end_date between ?4 and ?5) and ((start_time > ?6 and start_time < ?7 or end_time > ?6 and end_time < ?7)or(?6 < start_time and ?6 > end_time and ?7 < start_time and ?7 > end_time)or(start_time=?6 and end_time=?7))",nativeQuery = true)
    List<String> findRoom(int b_id, int f_id ,int w_id , String date,String end_date ,String stime , String etime );
    @Query(value = "select reason_for_booking from conf_confirmation where building_id=?1 and floor_id=?2 and conf_room=?3 and (date between ?4 and ?5 or end_date between ?4 and ?5)",nativeQuery = true)
    List<String> findRoomOnTime(int b_id, int f_id ,int w_id , String date ,String end_date);
    @Query(value = "select count(*) from conf_confirmation where (date between ?1 and ?2 or end_date between ?1 and ?2) and building_id=?3 and floor_id=?4 and conf_room=?5 and deleted='false'",nativeQuery = true)
    public int countAll(String start,String end,int b,int f, int w);
    @Query(value = "select count(*) from conf_confirmation where (date between ?1 and ?2 or end_date between ?1 and ?2) and building_id=?3 and floor_id=?4 and conf_room=?5",nativeQuery = true)
    public int countForReport(String start,String end,int b,int f, int w);
    @Query(value="select * from conf_confirmation",nativeQuery = true)
    List<conf_confirmation>findAllUsers();

    @Modifying
    @Transactional
    @Query(value="delete from conf_confirmation where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and project_name=?8 ",nativeQuery = true)
    public void  deleteUser2(String email,String name,int bid,int fid,int room,String sdate,String edate,String project_name);

    @Modifying
    @Transactional
    @Query(value="delete from conf_confirmation where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and start_time=?8 and end_time=?9 and project_name=?10 ",nativeQuery = true)
    public void  deleteUser1(String email,String name,int bid,int fid,int room,String sdate,String edate,String stime,String etime,String project_name);

    @Modifying
    @Transactional
    @Query(value="delete from conf_confirmation where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and start_time=?8 and end_time=?9",nativeQuery = true)
    public void  deleteEmployee(String email,String name,int bid,int fid,int room,String sdate,String edate,String stime,String etime);
    @Modifying
    @Transactional
    @Query(value="delete from conf_confirmation where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7  ",nativeQuery = true)
    public void  deleteEmployee2(String email,String name,int bid,int fid,int room,String sdate,String edate);

    @Query(value="select * from conf_confirmation where building_id=?1 and deleted='false'",nativeQuery = true)
    public List<conf_confirmation> findpersonbybuilding(int building_id);

    @Query(value="select * from conf_confirmation where building_id=?1 and floor_id=?2",nativeQuery = true)
    public List<conf_confirmation> findbypersonbyfloor(int building_id,int floor_id);
    
//    @Modifying
//    @Transactional
//    @Query("update conf_confirmation set deleted='true' where email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and project_name=?8  and deleted='false'")
//    public void  deleteUser2(String email,String name,int bid,int fid,int room,String sdate,String edate,String project_name);
//    
//
//    @Modifying
//    @Transactional
//    @Query("update conf_confirmation set deleted='true' where email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7  and deleted='false'")
//    public void  deleteEmployee2(String email,String name,int bid,int fid,int room,String sdate,String edate);
//    
//    
//    @Modifying
//    @Transactional
//    @Query("update conf_confirmation set deleted='true' where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and start_time=?8 and end_time=?9 and project_name=?10 and deleted='false' ")
//    public void  deleteUser1(String email,String name,int bid,int fid,int room,String sdate,String edate,String stime,String etime,String project_name);
//    
//    
//    @Modifying
//    @Transactional
//    @Query("update conf_confirmation set deleted='true' where  email=?1 and name=?2 and building_id=?3 and floor_id=?4 and conf_room=?5 and date=?6 and end_date=?7 and start_time=?8 and end_time=?9 and deleted='false' ")
//    public void  deleteEmployee(String email,String name,int bid,int fid,int room,String sdate,String edate,String stime,String etime);
//    
    @Modifying
    @Transactional
    @Query(value = "update conf_confirmation set deleted='true' where building_id=?",nativeQuery = true)
    void deleteBEntry(int id);

    @Modifying
    @Transactional
    @Query(value = "update conf_confirmation set deleted='true' where building_id=? and floor_id=?",nativeQuery = true)
    void deleteFEntry(int bid ,int fid);

    @Modifying
    @Transactional
    @Query(value = "update conf_confirmation set deleted='false' where building_id=?",nativeQuery = true)
    void returnBEntry(int id);

    @Modifying
    @Transactional
    @Query(value = "update conf_confirmation set deleted='false' where building_id=? and floor_id=?",nativeQuery = true)
    void returnFEntry(int bid ,int fid);
    
    @Query(value = "select * from conf_confirmation where (?1 between date and end_date or ?2 between date and end_date) and deleted='false' and (email=?3 or project_name=?4) and (start_time>=?5 or( ?2 between date and end_date and start_time<=?5 ) )", nativeQuery = true)
    List<conf_confirmation> onDate(String date, String end, String email, String pro, String time);

    @Query(value=" select COUNT(*) from conf_confirmation where date=:sdate and start_time=:stime and end_time=:etime and building_id=:bid and floor_id=:fid and conf_room=:mrd", nativeQuery = true)
    public int getRecords(@Param("sdate") String sdate,@Param("stime") String stime,@Param("etime") String etime,@Param("bid") int bid,@Param("fid") int fid,@Param("mrd") int mrd);
  
    @Query(value="select COUNT(*) from conf_confirmation where email=:mail and start_time=:stime and end_time=:etime and date=:sdate and project_name=:pjname",nativeQuery = true)
    public int getConfRecords(@Param("mail") String mail,@Param("stime") String stime,@Param("etime") String etime,@Param("sdate") String sdate,@Param("pjname") String pjname);
    
 
    @Query(value="select COUNT(*) from conf_confirmation where email=:mail and start_time=:stime and end_time=:etime and date=:sdate",nativeQuery = true)
    public int getConfRecords2(@Param("mail") String mail,@Param("stime") String stime,@Param("etime") String etime,@Param("sdate") String sdate);
}
