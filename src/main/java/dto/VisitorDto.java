package dto;

import entity.ClientStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VisitorDto {

    private String firstName;
    private String lastName;
    private int birthYear;
    private AddressDto address;
    private ClientStatus status;
    private LocalDateTime lastVisitAt;
    private BigDecimal spentAmount;
    private LocalDate firstVisitDate;

    public VisitorDto() {
    }

    public VisitorDto(String firstName, String lastName, int birthYear, AddressDto address,
                      ClientStatus status, LocalDateTime lastVisitAt, BigDecimal spentAmount,
                      LocalDate firstVisitDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.address = address;
        this.status = status;
        this.lastVisitAt = lastVisitAt;
        this.spentAmount = spentAmount;
        this.firstVisitDate = firstVisitDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "VisitorDto{" +
                "firstName='" + firstName + '\'' +
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
