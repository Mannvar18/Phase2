package com.Repository;

import com.Model.Building;
import org.springframework.data.repository.CrudRepository;

import com.Model.Floor;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface Floorrepo extends CrudRepository<Floor, Integer> {

	// @Modifying
	// @Transactional
	// @Query("update Floor set no_of_workstations=no_of_workstations-1 where Floor_no=:f ")
	// void Fupdate(@Param(value="f") int f);
	
	// @Modifying
	// @Transactional
	// @Query("update Floor set no_of_workstations=no_of_workstations+1 where Floor_no=:fl ")
	// void Fdecrement(@Param(value="fl") int fl);

	@Query("select count(*) from Floor f where f.floor_state='enable' and f.Floor_no=:n")
	public int getFstate(@Param("n") int n);

	@Modifying
	@Transactional
	@Query("update Floor f set f.floor_state='disable' where f.Floor_no=:n ")
	public void setFstate(@Param("n") int n);

	@Modifying
	@Transactional
	@Query("update Floor f set f.floor_state='enable' where f.Floor_no=:n ")
	public void setFstatetoEnable(@Param("n") int n);

	@Query(value="select * from floor where floor_no=?1",nativeQuery = true)
    Floor findByfloor_number(int floor_no);


	@Query(value="select * from floor where floor_no=?1",nativeQuery = true)
	public Floor findfloor(int floor_no);

	@Query(value="select * from floor where bdid=?1",nativeQuery=true)
	public List<Floor> listoffloor(int bid);

	@Query(value = "select floor_name from floor where floor_no=?",nativeQuery = true)
    String findFId(int id);

	@Query("select f.floor_name from Floor f where f.Floor_no=:n")
	public String getFloorname(@Param("n") int n);
     
   
    @Modifying
	@Transactional
    @Query(value="update floor set reason_for_disabling=?1 where floor_no=?2" ,nativeQuery=true)
	public void updateReasonforFloor(String reason,int id);

	//	@Query("select _id from Building where name=:build_name")
//	int findByName(@Param(value = "build_name") String build_name);
//	Building findByName(String build_name);

//	@Query("SELECT floor_name,no_of_workstation FROM Floor where bdid=:id")
//	List<String> findFloorData(@Param(value = "id") int id);
@Query(value = "select count(*) from floor where Building_id=? and floor_state='disable'",nativeQuery = true)
int findDisableCount(int id);
	@Query(value = "select count(*) from floor where Building_id=?",nativeQuery = true)
	int findCount(int id);
}