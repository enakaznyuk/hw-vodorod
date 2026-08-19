package service;

import dto.AppointmentDto;
import entity.Appointment;
import entity.Facility;
import entity.Visitor;
import repository.AppointmentRepository;
import repository.FacilityRepository;
import repository.VisitorRepository;

import java.util.List;

public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final VisitorRepository visitorRepository;
    private final FacilityRepository facilityRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              VisitorRepository visitorRepository,
                              FacilityRepository facilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.visitorRepository = visitorRepository;
        this.facilityRepository = facilityRepository;
    }

    public boolean addAppointment(Long clientId, Long facilityId, AppointmentDto appointmentDto) {
        Visitor client = visitorRepository.findById(clientId).orElse(null);
        if (client == null) {
            System.out.println("Клиент с id=" + clientId + " не найден, запись не добавлена");
            return false;
        }

        Facility facility = facilityRepository.findById(facilityId).orElse(null);
        if (facility == null) {
            System.out.println("Помещение с id=" + facilityId + " не найдено, запись не добавлена");
            return false;
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setFacility(facility);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        appointmentRepository.save(appointment);
        return true;
    }

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<Appointment> getAllAppointmentEntities() {
        return appointmentRepository.findAll();
    }

    public long countAppointments() {
        return appointmentRepository.count();
    }

    public List<Appointment> findByFacilityId(Long facilityId) {
        return appointmentRepository.findByFacilityId(facilityId);
    }

    private AppointmentDto toDto(Appointment appointment) {
        return new AppointmentDto(appointment.getAppointmentDate(), appointment.getAppointmentTime());
    }
}
