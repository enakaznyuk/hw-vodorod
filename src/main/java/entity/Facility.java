package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facilities")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_name", nullable = false)
    private String facilityName;

    @Column(name = "identification_number", nullable = false, unique = true)
    private String identificationNumber;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityStatus status;

    @Column(name = "hourly_rental_cost", nullable = false)
    private BigDecimal hourlyRentalCost;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ProvidedService providedService;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public Facility() {
    }

    public Facility(String facilityName, String identificationNumber, int maxCapacity,
                    FacilityStatus status, BigDecimal hourlyRentalCost, ProvidedService providedService) {
        this.facilityName = facilityName;
        this.identificationNumber = identificationNumber;
        this.maxCapacity = maxCapacity;
        this.status = status;
        this.hourlyRentalCost = hourlyRentalCost;
        this.providedService = providedService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public FacilityStatus getStatus() {
        return status;
    }

    public void setStatus(FacilityStatus status) {
        this.status = status;
    }

    public BigDecimal getHourlyRentalCost() {
        return hourlyRentalCost;
    }

    public void setHourlyRentalCost(BigDecimal hourlyRentalCost) {
        this.hourlyRentalCost = hourlyRentalCost;
    }

    public ProvidedService getProvidedService() {
        return providedService;
    }

    public void setProvidedService(ProvidedService providedService) {
        this.providedService = providedService;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", facilityName='" + facilityName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", status=" + status +
                ", hourlyRentalCost=" + hourlyRentalCost +
                ", serviceId=" + (providedService != null ? providedService.getId() : null) +
                '}';
    }
}
