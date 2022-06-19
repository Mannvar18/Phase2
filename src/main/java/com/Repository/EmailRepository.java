package com.Repository;


//import com.Model.Email;
import com.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EmailRepository extends JpaRepository<User, Integer> {
//    User findAllUser(String email);
//@Query(value = "select role from user u where email",nativeQuery = true)
//String findByEmail(String email);

//    @Query(value = "select role from user where email='email'",nativeQuery = true)
	//SELECT * FROM workstation4.user where email='kln.sagar@gmail.com' group by email having COUNT(email) <= 10 ;
	@Query(value="select * from User  where email=:email LIMIT 1",nativeQuery=true)
    User findByEmail(@Param("email")String email);
  //Need to Update The below
	User findByName(String name);
//    @Query(value = "SELECT * FROM User WHERE user.email=email")
//    Optional<User> findByEmail(String email);
@Query(value="SELECT email FROM User WHERE name=:name LIMIT 1",nativeQuery=true)
String findEmail(@Param("name") String name);

//@Query(value ="select * from user where email=?1",nativeQuery = true)
@Query(value="select * from User where email=:email LIMIT 1",nativeQuery=true)
User finduser(@Param("email")String email);
//
@Query(value="select name from User where email=:email LIMIT 1",nativeQuery=true)
String findname(@Param("email")String email);
//
@Query(value="SELECT role FROM User WHERE email=:email LIMIT 1",nativeQuery=true)
String getRole(@Param("email")String email);
//
@Query("select COUNT(*) from User where role=:role")
public int getCountOfEmp(@Param("role") String role);

@Query(value = "SELECT * FROM User WHERE project_name=? and role='Employee'",nativeQuery = true)
List<User> findByProjectName(String project_name);

@Query(value="SELECT project_name FROM User WHERE email=:email LIMIT 1",nativeQuery=true)
String findProjectByEmail(@Param("email")String email);

@Query("select name from User where project_name=:name and role='Employee'")
List<String> findByProject_Name(@Param("name")String project_name);

@Query("select email from User where project_name=:name and role='Employee'")
List<String> findEmails(@Param("name")String project_name);

@Query("select project_name from User where email=:email")
List<String> findByManager_name(@Param("email")String email);

@Query("select project_name from User where email=:email")
List<String> findByEmails(@Param("email")String email);
//List<User> findByEmails(String email);

@Query("select distinct project_name from User where project_name<>'NULL'")
List<String> findProject();

@Query("select COUNT(*) from User where email=:email and project_name=:project")
public int getCountOfRecords(@Param("email") String email,@Param("project") String project );

@Query("select COUNT(*) from User")
public int getUserRecords();

}

