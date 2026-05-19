package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.Room;
import com.example.Student_Course_Registration_System.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public String addRoom(Room room) {
        if (roomRepository.findById(room.getRoomId()).isPresent()) return "Room ID already exists";
        for (Room r : roomRepository.findAll()) {
            if (r.getRoomName().equalsIgnoreCase(room.getRoomName())) return "A room with this name already exists";
        }
        if (room.getCapacity() <= 0) return "Room capacity must be greater than zero";
        roomRepository.save(room);
        return null;
    }

    public List<Room> getAllRooms() { return roomRepository.findAll(); }
    public Room getRoomById(String roomId) { return roomRepository.findById(roomId).orElse(null); }
    public List<Room> getAvailableRooms() { return roomRepository.findByAvailableTrue(); }

    public List<Room> getAvailableRoomsByCapacity(int requiredCapacity) {
        List<Room> result = new ArrayList<>();
        for (Room room : roomRepository.findByAvailableTrue()) {
            if (room.getCapacity() >= requiredCapacity) result.add(room);
        }
        return result;
    }

    public String updateRoom(Room room) {
        if (roomRepository.findById(room.getRoomId()).isEmpty()) return "Room not found";
        if (room.getCapacity() <= 0) return "Room capacity must be greater than zero";
        for (Room r : roomRepository.findAll()) {
            if (!r.getRoomId().equals(room.getRoomId())) {
                if (r.getRoomName().equalsIgnoreCase(room.getRoomName())) return "A room with this name already exists";
            }
        }
        roomRepository.save(room);
        return null;
    }

    public void deleteRoom(String roomId) {
        Room existing = roomRepository.findById(roomId).orElse(null);
        if (existing == null) return;
        if (!existing.isAvailable()) return;
        roomRepository.deleteById(roomId);
    }

    public List<Room> searchByName(String name) {
        List<Room> result = new ArrayList<>();
        for (Room r : roomRepository.findAll()) {
            if (r.getRoomName().toLowerCase().contains(name.toLowerCase())) result.add(r);
        }
        return result;
    }

    public int getTotalRooms() { return (int) roomRepository.count(); }
}