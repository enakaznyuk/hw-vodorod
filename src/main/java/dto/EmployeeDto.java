package dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDto {

    private String firstName;
    private String lastName;
    private int birthYear;
    private AddressDto address;
    private LocalDate hireDate;
    private LocalDate fireDate;
    private String position;
    private BigDecimal monthlySalary;

    public EmployeeDto() {
    }

    public EmployeeDto(String firstName, String lastName, int birthYear, AddressDto address,
                       LocalDate hireDate, LocalDate fireDate, String position, BigDecimal monthlySalary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.address = address;
        this.hireDate = hireDate;
        this.fireDate = fireDate;
        this.position = position;
        this.monthlySalary = monthlySalary;
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

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getFireDate() {
        return fireDate;
    }

    public void setFireDate(LocalDate fireDate) {
        this.fireDate = fireDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(BigDecimal monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthYear=" + birthYear +
                ", address=" + address +
                ", hireDate=" + hireDate +
                ", fireDate=" + fireDate +
                ", position='" + position + '\'' +
                ", monthlySalary=" + monthlySalary +
                '}';
    }
}
