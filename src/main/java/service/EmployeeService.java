package service;

import dto.AddressDto;
import dto.EmployeeDto;
import entity.Address;
import entity.Employee;
import org.hibernate.exception.ConstraintViolationException;
import repository.EmployeeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public boolean addEmployee(EmployeeDto employeeDto) {
        if (employeeRepository.existsByFullNameAndBirthYear(
                employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getBirthYear())) {
            System.out.println("Работник " + employeeDto.getFirstName() + " " + employeeDto.getLastName()
                    + " уже существует, пропускаем");
            return false;
        }

        try {
            employeeRepository.save(toEntity(employeeDto));
            return true;
        } catch (RuntimeException exception) {
            if (isDuplicateKeyError(exception)) {
                System.out.println("Не удалось добавить работника " + employeeDto.getFirstName() + " "
                        + employeeDto.getLastName() + ": запись уже есть в базе");
                return false;
            }
            throw exception;
        }
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<EmployeeDto> findEmployeeById(Long id) {
        return employeeRepository.findById(id).map(this::toDto);
    }

    public Optional<EmployeeDto> findHighestPaidEmployee() {
        return employeeRepository.findHighestPaid().map(this::toDto);
    }

    public Optional<EmployeeDto> findLowestPaidEmployee() {
        return employeeRepository.findLowestPaid().map(this::toDto);
    }

    public BigDecimal calculateStaffExpenses(LocalDate periodStart, LocalDate periodEnd) {
        if (periodStart == null || periodEnd == null || periodStart.isAfter(periodEnd)) {
            throw new IllegalArgumentException("Некорректный период: дата начала должна быть не позже даты окончания");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<Employee> employees = employeeRepository.findActiveInPeriod(periodStart, periodEnd);

        for (Employee employee : employees) {
            LocalDate workStart = employee.getHireDate().isAfter(periodStart)
                    ? employee.getHireDate()
                    : periodStart;
            LocalDate workEnd = employee.getFireDate() == null || employee.getFireDate().isAfter(periodEnd)
                    ? periodEnd
                    : employee.getFireDate();

            if (workStart.isAfter(workEnd)) {
                continue;
            }

            long months = ChronoUnit.MONTHS.between(
                    workStart.withDayOfMonth(1),
                    workEnd.withDayOfMonth(1)
            ) + 1;

            total = total.add(employee.getMonthlySalary().multiply(BigDecimal.valueOf(months)));
        }

        return total;
    }

    private boolean isDuplicateKeyError(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof ConstraintViolationException constraintViolationException
                    && "23505".equals(constraintViolationException.getSQLState())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private Employee toEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setBirthYear(employeeDto.getBirthYear());
        employee.setAddress(toAddressEntity(employeeDto.getAddress()));
        employee.setHireDate(employeeDto.getHireDate());
        employee.setFireDate(employeeDto.getFireDate());
        employee.setPosition(employeeDto.getPosition());
        employee.setMonthlySalary(employeeDto.getMonthlySalary());
        return employee;
    }

    private EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getBirthYear(),
                toAddressDto(employee.getAddress()),
                employee.getHireDate(),
                employee.getFireDate(),
                employee.getPosition(),
                employee.getMonthlySalary()
        );
    }

    private Address toAddressEntity(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return new Address(
                addressDto.getCity(),
                addressDto.getStreet(),
                addressDto.getHouseNumber(),
                addressDto.getPostalCode()
        );
    }

    private AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDto(
                address.getCity(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getPostalCode()
        );
    }
}
