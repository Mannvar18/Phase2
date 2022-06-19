package com.Repository;
import org.springframework.data.repository.CrudRepository;

import com.Model.Building;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Buildrepo extends CrudRepository<Building, Integer>{

	// @Modifying
	// @Transactional
	// @Query("update Building set no_of_workstations=no_of_workstations-1 where _id=:b ")
	// void Bupdate(@Param(value="b") int b);


	// @Modifying
	// @Transactional
	// @Query("update Building set no_of_workstations=no_of_workstations+1 where _id=:bd ")
	// void Bdecrement(@Param(value="bd") int bd);


	// @Query("SELECT name,no_of_workstations FROM Building")
	// List<String> findByNameAndNo();

	@Query("select count(*) from Building b where b.building_state='enable' and b._id=:n")
	public int getBstate(@Param("n") int n);

	@Modifying
	@Transactional
	@Query("update Building b set b.building_state='disable' where b._id=:n ")
	public void setState(@Param("n") int n);

	@Modifying
	@Transactional
	@Query("update Building b set b.building_state='enable' where b._id=:n ")
	public void setStatetoEnable(@Param("n") int n);

	@Query(value = "select name from building where _id=?",nativeQuery = true)
    String findId(int id);

	@Query(value="select * from building where _id=?1",nativeQuery = true)
	public Building findbuilding(int _id);

    @Query(value="select * from building where _id=?1",nativeQuery = true)
	public Building findbworkstation(int _id);

	@Query("select b.name from Building b where b._id=:n")
	public String getBuildingname(@Param("n") int n);

    @Modifying
	@Transactional
    @Query(value="update building set reason_for_disabling_building=?1 where _id=?2" ,nativeQuery=true)
	public void updateReason(String reason,int id);
	@Query(value = "select * from building where _id=?",nativeQuery = true)
	List<Building> findUsingId(int id);

}