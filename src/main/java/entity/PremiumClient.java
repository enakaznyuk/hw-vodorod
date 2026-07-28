package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Immutable
@Subselect("""
        SELECT u.id,
               u.first_name,
               u.last_name,
               u.birth_year,
               u.city,
               u.street,
               u.house_number,
               u.postal_code,
               v.status,
               v.last_visit_at,
               v.spent_amount,
               v.first_visit_date
        FROM users u
        JOIN visitors v ON u.id = v.id
        WHERE v.status = 'PREMIUM'
        """)
@Synchronize({"users", "visitors"})
public class PremiumClient {

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_year")
    private int birthYear;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @Column(name = "last_visit_at")
    private LocalDateTime lastVisitAt;

    @Column(name = "spent_amount")
    private BigDecimal spentAmount;

    @Column(name = "first_visit_date")
    private LocalDate firstVisitDate;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public Address getAddress() {
        return address;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastVisitAt() {
        return lastVisitAt;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public LocalDate getFirstVisitDate() {
        return firstVisitDate;
    }

    @Override
    public String toString() {
        return "PremiumClient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthYear=" + birthYear +
                ", address=" + address +
                ", status=" + status +
                ", lastVisitAt=" + lastVisitAt +
                ", spentAmount=" + spentAmount +
                ", firstVisitDate=" + firstVisitDate +
                '}';
    }
}
