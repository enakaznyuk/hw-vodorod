package dto;

import entity.ClientStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClientDto {

    private String firstName;
    private String lastName;
    private int age;
    private String phoneNumber;
    private LocalDate lastVisitDate;
    private ClientStatus status;
    private BigDecimal spentAmount;

    public ClientDto() {
    }

    public ClientDto(String firstName, String lastName, int age, String phoneNumber,
                     LocalDate lastVisitDate, ClientStatus status, BigDecimal spentAmount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.lastVisitDate = lastVisitDate;
        this.status = status;
        this.spentAmount = spentAmount;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(LocalDate lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", lastVisitDate=" + lastVisitDate +
                ", status=" + status +
                ", spentAmount=" + spentAmount +
                '}';
    }
}
