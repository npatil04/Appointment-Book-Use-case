package com.example.appointmentsystem.service;

import org.springframework.stereotype.Service;

import com.example.appointmentsystem.dto.CreateAvailabilitySlotRequest;
import com.example.appointmentsystem.entity.AvailabilitySlot;
import com.example.appointmentsystem.entity.Doctor;
import com.example.appointmentsystem.enums.AvailabilityStatus;
import com.example.appointmentsystem.exception.SlotOverlapException;
import com.example.appointmentsystem.repository.AvailabilitySlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotService {

    private final AvailabilitySlotRepository slotRepository;

    public AvailabilitySlot createSlot(Doctor doctor, CreateAvailabilitySlotRequest request) {

        if (request.getStartTime().isAfter(request.getEndTime())
                || request.getStartTime().isEqual(request.getEndTime())) {
            throw new RuntimeException("Invalid slot time");
        }

        boolean overlap = slotRepository
                .existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        doctor.getId(),
                        request.getEndTime(),
                        request.getStartTime()
                );
        
        System.out.println("Overlap is:" +overlap );

        if (overlap) {
        	throw new SlotOverlapException("Slot overlaps with existing availability");
        }

        AvailabilitySlot slot = new AvailabilitySlot();
        System.out.println("Doctor inside service is:"+doctor);
        slot.setDoctor(doctor);
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setStatus(AvailabilityStatus.AVAILABLE);

        return slotRepository.save(slot);
    }
}
