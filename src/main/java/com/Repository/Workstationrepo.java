package com.Repository;

import org.springframework.data.repository.CrudRepository;

import com.Model.Workstation;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Workstationrepo extends CrudRepository<Workstation, Integer>{

	@Query("SELECT count(*) FROM Workstation w WHERE w.number=:val")
	public int getCount(@Param("val") int val) ;

//
//	@Modifying
//	@Transactional
//	@Query("update Workstation set state='false' where number=:num ")
//	void Book(@Param(value="num") int num);
//
//	@Modifying
//	@Transactional
//	@Query("update Workstation set state='true' where number=:a ")
//	void deallocate(@Param(value="a") int a);
//	
//	//for deallocation
//	@Query("SELECT count(*) FROM Workstation w WHERE w.state='true' and w.number=:a")
//	public int getC1ount(@Param("a") int a) ;
	@Query("select count(*) from Workstation w where w.workstation_state='enable' and w.number=:b")
	public int getWstate(@Param("b") int b);

	@Modifying
	@Transactional
	@Query("update Workstation set workstation_state='disable' where number=:b")
	void setwstate(@Param("b") int b);

	@Modifying
	@Transactional
	@Query("update Workstation set workstation_state='enable' where number=:q")
	void unsetW(@Param("q") int q);

	@Query("select count(*) from Workstation w where w.workstation_state='disable' and w.number=:q")
	public int getWstate2(@Param("q") int q);
    
	@Query(value="SELECT * FROM workstation WHERE number=?1",nativeQuery = true)
    public Workstation findByworkstation(int workstation_id);

	@Query(value="select * from workstation where number=?1",nativeQuery = true)
	public Workstation findworkstation(int number);

    

}

