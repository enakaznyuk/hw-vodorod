package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Премиум-клиенты. В Hibernate 7 аннотация {@code @Where} заменена на {@code @SQLRestriction}.
 */
@Entity
@Table(name = "clients")
@Immutable
@SQLRestriction("status = 'PREMIUM'")
public class PremiumClient {

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private int age;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @Column(name = "spent_amount")
    private BigDecimal spentAmount;

    @Embedded
    private Address address;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "PremiumClient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", lastVisitDate=" + lastVisitDate +
                ", status=" + status +
                ", spentAmount=" + spentAmount +
                ", address=" + address +
                '}';
    }
}
