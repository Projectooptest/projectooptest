package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.Room;
import com.example.Student_Course_Registration_System.model.Schedule;
import com.example.Student_Course_Registration_System.repository.RoomRepository;
import com.example.Student_Course_Registration_System.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private RoomRepository roomRepository;

    public void addSchedule(Schedule schedule) {
        if (scheduleRepository.findById(schedule.getScheduleId()).isPresent()) return;
        Room room = roomRepository.findById(schedule.getRoom().getRoomId()).orElse(null);
        if (room == null || !room.isAvailable()) return;
        if (room.getCapacity() < schedule.getCourse().getMaxStudents()) return;
        for (Schedule s : scheduleRepository.findAll()) {
            if (schedule.hasConflict(s)) return;
        }
        scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() { return scheduleRepository.findAll(); }
    public Schedule getScheduleById(String scheduleId) { return scheduleRepository.findById(scheduleId).orElse(null); }
    public List<Schedule> getSchedulesByLecturerId(String lecturerId) { return scheduleRepository.findByLecturerLecturerId(lecturerId); }
    public List<Schedule> getSchedulesByCourseId(String courseId) { return scheduleRepository.findByCourseCourseId(courseId); }

    public void updateSchedule(Schedule schedule) {
        if (scheduleRepository.findById(schedule.getScheduleId()).isEmpty()) return;
        for (Schedule s : scheduleRepository.findAll()) {
            if (!s.getScheduleId().equals(schedule.getScheduleId())) {
                if (schedule.hasConflict(s)) return;
            }
        }
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(String scheduleId) {
        if (scheduleRepository.findById(scheduleId).isEmpty()) return;
        scheduleRepository.deleteById(scheduleId);
    }

    public int getTotalSchedules() { return (int) scheduleRepository.count(); }
}