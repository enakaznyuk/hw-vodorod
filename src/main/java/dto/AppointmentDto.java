package dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public AppointmentDto() {
    }

    public AppointmentDto(LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public String toString() {
        return "AppointmentDto{" +
                "appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                '}';
    }
}
