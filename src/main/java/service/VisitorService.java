package service;

import dto.AddressDto;
import dto.VisitorDto;
import entity.Address;
import entity.ClientStatus;
import entity.Visitor;
import org.hibernate.exception.ConstraintViolationException;
import repository.VisitorRepository;

import java.util.List;
import java.util.Optional;

public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public boolean addVisitor(VisitorDto visitorDto) {
        if (visitorRepository.existsByFullNameAndBirthYear(
                visitorDto.getFirstName(), visitorDto.getLastName(), visitorDto.getBirthYear())) {
            System.out.println("Посетитель " + visitorDto.getFirstName() + " " + visitorDto.getLastName()
                    + " уже существует, пропускаем");
            return false;
        }

        try {
            visitorRepository.save(toEntity(visitorDto));
            return true;
        } catch (RuntimeException exception) {
            if (isDuplicateKeyError(exception)) {
                System.out.println("Не удалось добавить посетителя " + visitorDto.getFirstName() + " "
                        + visitorDto.getLastName() + ": запись уже есть в базе");
                return false;
            }
            throw exception;
        }
    }

    public List<VisitorDto> getAllVisitors() {
        return visitorRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<VisitorDto> findVisitorById(Long id) {
        return visitorRepository.findById(id).map(this::toDto);
    }

    public void deleteVisitor(Long id) {
        if (visitorRepository.findById(id).isEmpty()) {
            System.out.println("Посетитель с id=" + id + " не найден, удаление пропущено");
            return;
        }
        visitorRepository.deleteById(id);
    }

    public void changeVisitorStatus(Long id, ClientStatus status) {
        if (visitorRepository.findById(id).isEmpty()) {
            System.out.println("Посетитель с id=" + id + " не найден, смена статуса пропущена");
            return;
        }
        visitorRepository.updateStatus(id, status);
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

    private Visitor toEntity(VisitorDto visitorDto) {
        Visitor visitor = new Visitor();
        visitor.setFirstName(visitorDto.getFirstName());
        visitor.setLastName(visitorDto.getLastName());
        visitor.setBirthYear(visitorDto.getBirthYear());
        visitor.setAddress(toAddressEntity(visitorDto.getAddress()));
        visitor.setStatus(visitorDto.getStatus());
        visitor.setLastVisitAt(visitorDto.getLastVisitAt());
        visitor.setSpentAmount(visitorDto.getSpentAmount());
        visitor.setFirstVisitDate(visitorDto.getFirstVisitDate());
        return visitor;
    }

    private VisitorDto toDto(Visitor visitor) {
        return new VisitorDto(
                visitor.getFirstName(),
                visitor.getLastName(),
                visitor.getBirthYear(),
                toAddressDto(visitor.getAddress()),
                visitor.getStatus(),
                visitor.getLastVisitAt(),
                visitor.getSpentAmount(),
                visitor.getFirstVisitDate()
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
