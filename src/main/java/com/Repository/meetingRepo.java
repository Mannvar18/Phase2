package com.Repository;

import javax.transaction.Transactional;

import com.Model.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface meetingRepo extends JpaRepository<meeting,Integer> 
{
@Modifying
@Transactional
@Query(value="update meeting set room_state='disable' where number=?1",nativeQuery = true)
void  disableRoom(int number);   

@Modifying
@Transactional
@Query(value="update meeting set reason_for_disabling=?2 where number=?1",nativeQuery = true)
void  disableRoomSetReason(int number,String reason);   
    
@Modifying
@Transactional
@Query(value="update meeting set room_state='enable' where number=?1",nativeQuery = true)
void enableMeetingRooms(int number);

@Modifying
@Transactional
@Query(value="update meeting set reason_for_booking=?2 where number=?1",nativeQuery = true)
void updateReason(int number,String reason);
}
