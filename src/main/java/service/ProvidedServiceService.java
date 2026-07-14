package service;

import dto.ProvidedServiceDto;
import entity.ProvidedService;
import org.hibernate.exception.ConstraintViolationException;
import repository.ProvidedServiceRepository;

import java.util.List;

public class ProvidedServiceService {

    private final ProvidedServiceRepository providedServiceRepository;

    public ProvidedServiceService(ProvidedServiceRepository providedServiceRepository) {
        this.providedServiceRepository = providedServiceRepository;
    }

    public boolean addService(ProvidedServiceDto serviceDto) {
        try {
            providedServiceRepository.save(toEntity(serviceDto));
            return true;
        } catch (RuntimeException exception) {
            if (isDuplicateKeyError(exception)) {
                System.out.println("Услуга '" + serviceDto.getServiceName() + "' уже существует, пропускаем");
                return false;
            }
            throw exception;
        }
    }

    public List<ProvidedServiceDto> getAllServices() {
        return providedServiceRepository.findAll().stream()
                .map(this::toDto)
                .toList();
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

    private ProvidedService toEntity(ProvidedServiceDto serviceDto) {
        ProvidedService providedService = new ProvidedService();
        providedService.setServiceName(serviceDto.getServiceName());
        providedService.setPrice(serviceDto.getPrice());
        return providedService;
    }

    private ProvidedServiceDto toDto(ProvidedService providedService) {
        return new ProvidedServiceDto(
                providedService.getServiceName(),
                providedService.getPrice()
        );
    }
}
