package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "id")
public class Employee extends User {

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "fire_date")
    private LocalDate fireDate;

    @Column(nullable = false)
    private String position;

    @Column(name = "monthly_salary", nullable = false)
    private BigDecimal monthlySalary;

    public Employee() {
    }

    public Employee(String firstName, String lastName, int birthYear, Address address,
                    LocalDate hireDate, LocalDate fireDate, String position, BigDecimal monthlySalary) {
        super(firstName, lastName, birthYear, address);
        this.hireDate = hireDate;
        this.fireDate = fireDate;
        this.position = position;
        this.monthlySalary = monthlySalary;
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
        return "Employee{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", birthYear=" + getBirthYear() +
                ", address=" + getAddress() +
                ", hireDate=" + hireDate +
                ", fireDate=" + fireDate +
                ", position='" + position + '\'' +
                ", monthlySalary=" + monthlySalary +
                '}';
    }
}
