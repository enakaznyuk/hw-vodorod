package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visitors")
@PrimaryKeyJoinColumn(name = "id")
public class Visitor extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status;

    @Column(name = "last_visit_at")
    private LocalDateTime lastVisitAt;

    @Column(name = "spent_amount", nullable = false)
    private BigDecimal spentAmount;

    @Column(name = "first_visit_date", nullable = false)
    private LocalDate firstVisitDate;

    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    public Visitor() {
    }

    public Visitor(String firstName, String lastName, int birthYear, Address address,
                   ClientStatus status, LocalDateTime lastVisitAt, BigDecimal spentAmount,
                   LocalDate firstVisitDate) {
        super(firstName, lastName, birthYear, address);
        this.status = status;
        this.lastVisitAt = lastVisitAt;
        this.spentAmount = spentAmount;
        this.firstVisitDate = firstVisitDate;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastVisitAt() {
        return lastVisitAt;
    }

    public void setLastVisitAt(LocalDateTime lastVisitAt) {
        this.lastVisitAt = lastVisitAt;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public LocalDate getFirstVisitDate() {
        return firstVisitDate;
    }

    public void setFirstVisitDate(LocalDate firstVisitDate) {
        this.firstVisitDate = firstVisitDate;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", birthYear=" + getBirthYear() +
                ", address=" + getAddress() +
                ", status=" + status +
                ", lastVisitAt=" + lastVisitAt +
                ", spentAmount=" + spentAmount +
                ", firstVisitDate=" + firstVisitDate +
                '}';
    }
}
