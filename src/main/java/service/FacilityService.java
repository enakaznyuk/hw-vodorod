package service;

import dto.FacilityDto;
import entity.Facility;
import org.hibernate.exception.ConstraintViolationException;
import repository.FacilityRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public boolean addFacility(FacilityDto facilityDto) {
        try {
            facilityRepository.save(toEntity(facilityDto));
            return true;
        } catch (RuntimeException exception) {
            if (isDuplicateKeyError(exception)) {
                System.out.println("Помещение с номером " + facilityDto.getIdentificationNumber() +
                        " уже существует, пропускаем");
                return false;
            }
            throw exception;
        }
    }

    public Optional<FacilityDto> addFacilityByDetach(Long templateId, String newIdentificationNumber) {
        try {
            Facility facility = facilityRepository.addFacilityByDetach(templateId, newIdentificationNumber);
            if (facility == null) {
                System.out.println("Шаблон помещения с id=" + templateId + " не найден");
                return Optional.empty();
            }
            return Optional.of(toDto(facility));
        } catch (RuntimeException exception) {
            if (isDuplicateKeyError(exception)) {
                System.out.println("Помещение с номером " + newIdentificationNumber + " уже существует, пропускаем");
                return Optional.empty();
            }
            throw exception;
        }
    }

    public void changeHourlyRentalCost(Long id, BigDecimal hourlyRentalCost) {
        if (facilityRepository.findById(id).isEmpty()) {
            System.out.println("Помещение с id=" + id + " не найдено, смена стоимости пропущена");
            return;
        }
        facilityRepository.updateHourlyRentalCost(id, hourlyRentalCost);
    }

    public List<FacilityDto> getAllFacilities() {
        return facilityRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<FacilityDto> findById(Long id) {
        return facilityRepository.findById(id).map(this::toDto);
    }

    public Optional<Long> findIdByIdentificationNumber(String identificationNumber) {
        return facilityRepository.findByIdentificationNumber(identificationNumber).map(Facility::getId);
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

    private Facility toEntity(FacilityDto facilityDto) {
        Facility facility = new Facility();
        facility.setFacilityName(facilityDto.getFacilityName());
        facility.setIdentificationNumber(facilityDto.getIdentificationNumber());
        facility.setMaxCapacity(facilityDto.getMaxCapacity());
        facility.setStatus(facilityDto.getStatus());
        facility.setHourlyRentalCost(facilityDto.getHourlyRentalCost());
        return facility;
    }

    private FacilityDto toDto(Facility facility) {
        return new FacilityDto(
                facility.getFacilityName(),
                facility.getIdentificationNumber(),
                facility.getMaxCapacity(),
                facility.getStatus(),
                facility.getHourlyRentalCost()
        );
    }
}
