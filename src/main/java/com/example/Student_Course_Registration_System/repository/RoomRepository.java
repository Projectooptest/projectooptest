package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByAvailableTrue();
}