package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.math.BigDecimal;

@Entity
@Immutable
@Subselect("""
        SELECT id,
               facility_name,
               identification_number,
               max_capacity,
               status,
               hourly_rental_cost
        FROM facilities
        WHERE max_capacity <= 15
        """)
@Synchronize("facilities")
public class SmallCapacityFacility {

    @Id
    private Long id;

    @Column(name = "facility_name")
    private String facilityName;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Enumerated(EnumType.STRING)
    private FacilityStatus status;

    @Column(name = "hourly_rental_cost")
    private BigDecimal hourlyRentalCost;

    public Long getId() {
        return id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public FacilityStatus getStatus() {
        return status;
    }

    public BigDecimal getHourlyRentalCost() {
        return hourlyRentalCost;
    }

    @Override
    public String toString() {
        return "SmallCapacityFacility{" +
                "id=" + id +
                ", facilityName='" + facilityName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", status=" + status +
                ", hourlyRentalCost=" + hourlyRentalCost +
                '}';
    }
}
