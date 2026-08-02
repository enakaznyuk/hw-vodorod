import dto.AddressDto;
import dto.AppointmentDto;
import dto.EmployeeDto;
import dto.FacilityDto;
import dto.ProvidedServiceDto;
import dto.VisitDto;
import dto.VisitorDto;
import entity.Appointment;
import entity.ClientStatus;
import entity.FacilityStatus;
import entity.PremiumClient;
import entity.SmallCapacityFacility;
import entity.Visit;
import repository.AppointmentRepository;
import repository.EmployeeRepository;
import repository.FacilityRepository;
import repository.PremiumClientRepository;
import repository.ProvidedServiceRepository;
import repository.SmallCapacityFacilityRepository;
import repository.VisitRepository;
import repository.VisitorRepository;
import service.AppointmentService;
import service.EmployeeService;
import service.FacilityService;
import service.ProvidedServiceService;
import service.VisitService;
import service.VisitorService;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        VisitorRepository visitorRepository = new VisitorRepository();
        VisitorService visitorService = new VisitorService(visitorRepository);

        EmployeeRepository employeeRepository = new EmployeeRepository();
        EmployeeService employeeService = new EmployeeService(employeeRepository);

        ProvidedServiceRepository providedServiceRepository = new ProvidedServiceRepository();
        ProvidedServiceService providedServiceService = new ProvidedServiceService(providedServiceRepository);

        FacilityRepository facilityRepository = new FacilityRepository();
        FacilityService facilityService = new FacilityService(facilityRepository, providedServiceRepository);

        VisitRepository visitRepository = new VisitRepository();
        VisitService visitService = new VisitService(visitRepository, visitorRepository);

        AppointmentRepository appointmentRepository = new AppointmentRepository();
        AppointmentService appointmentService = new AppointmentService(
                appointmentRepository, visitorRepository, facilityRepository);

        SmallCapacityFacilityRepository smallCapacityFacilityRepository = new SmallCapacityFacilityRepository();
        PremiumClientRepository premiumClientRepository = new PremiumClientRepository();

        System.out.println("=== Посетители (JOINED, Session) ===");
        int addedVisitors = addInitialVisitors(visitorService);
        System.out.println("Добавлено новых посетителей: " + addedVisitors);
        printVisitors(visitorService.getAllVisitors());

        System.out.println("\n=== Работники (JOINED, Session) ===");
        int addedEmployees = addInitialEmployees(employeeService);
        System.out.println("Добавлено новых работников: " + addedEmployees);
        printEmployees(employeeService.getAllEmployees());

        System.out.println("\n=== Поиск посетителя по id через Session ===");
        visitorService.findVisitorById(1L)
                .ifPresentOrElse(
                        visitor -> System.out.println("Найден посетитель: " + visitor),
                        () -> System.out.println("Посетитель с id=1 не найден")
                );

        System.out.println("\n=== Услуги (Session) ===");
        int addedServices = addInitialServices(providedServiceService);
        System.out.println("Добавлено новых услуг: " + addedServices);
        printServices(providedServiceService.getAllServices());

        System.out.println("\n=== Помещения (Session, связь с услугами) ===");
        Long gymTemplateId = addInitialFacilities(facilityService);
        printFacilities(facilityService.getAllFacilities());

        System.out.println("\n=== Добавление помещения через detach ===");
        if (gymTemplateId != null) {
            facilityService.addFacilityByDetach(gymTemplateId, "GYM-002")
                    .ifPresent(facility -> System.out.println("Добавлено помещение через detach: " + facility));
            facilityService.addFacilityByDetach(gymTemplateId, "GYM-003")
                    .ifPresent(facility -> System.out.println("Добавлено помещение через detach: " + facility));
        }
        printFacilities(facilityService.getAllFacilities());

        System.out.println("\n=== Смена стоимости аренды помещения ===");
        facilityService.changeHourlyRentalCost(gymTemplateId, new BigDecimal("1800.00"));
        facilityService.findById(gymTemplateId)
                .ifPresent(facility -> System.out.println("Обновлённая стоимость: " + facility));

        System.out.println("\n=== Посещения (1 посетитель — много посещений) ===");
        addInitialVisits(visitService, visitorRepository);
        printVisits(visitService.getAllVisitEntities());

        System.out.println("\n=== Записи (1 пользователь — много записей, 1 помещение — много записей) ===");
        Long yogaFacilityId = addInitialAppointments(appointmentService, visitorRepository, facilityService);
        printAppointments(appointmentService.getAllAppointmentEntities());

        System.out.println("\n=== Каскадное удаление: удаляем помещение YOGA-001 ===");
        System.out.println("Записей до удаления: " + appointmentService.countAppointments());
        if (yogaFacilityId != null) {
            System.out.println("Записей у YOGA-001 до удаления: "
                    + appointmentService.findByFacilityId(yogaFacilityId).size());
            facilityService.deleteFacility(yogaFacilityId);
        }
        System.out.println("Записей после удаления помещения: " + appointmentService.countAppointments());
        printAppointments(appointmentService.getAllAppointmentEntities());

        System.out.println("\n=== Посетители: смена статуса ===");
        visitorService.changeVisitorStatus(2L, ClientStatus.PREMIUM);
        printVisitors(visitorService.getAllVisitors());

        System.out.println("\n=== Помещения вместимостью не более 15 человек (@Subselect) ===");
        List<SmallCapacityFacility> smallFacilities = smallCapacityFacilityRepository.findAll();
        if (smallFacilities.isEmpty()) {
            System.out.println("Таких помещений нет");
        } else {
            for (SmallCapacityFacility facility : smallFacilities) {
                System.out.println(facility.getFacilityName()
                        + " (" + facility.getIdentificationNumber() + ")"
                        + ", вместимость=" + facility.getMaxCapacity()
                        + ", стоимость=" + facility.getHourlyRentalCost());
            }
        }

        System.out.println("\n=== Премиум-посетители (@Subselect) ===");
        List<PremiumClient> premiumClients = premiumClientRepository.findAll();
        if (premiumClients.isEmpty()) {
            System.out.println("Премиум-посетителей нет");
        } else {
            for (PremiumClient client : premiumClients) {
                System.out.println(client);
            }
        }

        HibernateUtil.shutdown();
    }

    private static int addInitialVisitors(VisitorService visitorService) {
        int added = 0;

        if (visitorService.addVisitor(new VisitorDto(
                "Иван", "Петров", 1998,
                new AddressDto("Москва", "Тверская", "12", "125009"),
                ClientStatus.ACTIVE,
                LocalDateTime.of(2026, 6, 15, 18, 30),
                new BigDecimal("12500.00"),
                LocalDate.of(2024, 1, 10)
        ))) {
            added++;
        }

        if (visitorService.addVisitor(new VisitorDto(
                "Анна", "Смирнова", 2002,
                new AddressDto("Санкт-Петербург", "Невский проспект", "28", "191186"),
                ClientStatus.PREMIUM,
                LocalDateTime.of(2026, 6, 28, 12, 0),
                new BigDecimal("45800.50"),
                LocalDate.of(2023, 5, 20)
        ))) {
            added++;
        }

        if (visitorService.addVisitor(new VisitorDto(
                "Дмитрий", "Козлов", 1991,
                new AddressDto("Казань", "Баумана", "5", "420111"),
                ClientStatus.ACTIVE,
                LocalDateTime.of(2026, 5, 10, 9, 45),
                new BigDecimal("8700.00"),
                LocalDate.of(2025, 2, 1)
        ))) {
            added++;
        }

        if (visitorService.addVisitor(new VisitorDto(
                "Елена", "Волкова", 1995,
                new AddressDto("Новосибирск", "Красный проспект", "50", "630091"),
                ClientStatus.BLOCKED,
                LocalDateTime.of(2026, 3, 2, 16, 15),
                new BigDecimal("3200.75"),
                LocalDate.of(2024, 11, 3)
        ))) {
            added++;
        }

        return added;
    }

    private static int addInitialEmployees(EmployeeService employeeService) {
        int added = 0;

        if (employeeService.addEmployee(new EmployeeDto(
                "Олег", "Соколов", 1988,
                new AddressDto("Москва", "Арбат", "10", "119019"),
                LocalDate.of(2020, 3, 1),
                null,
                "Тренер",
                new BigDecimal("75000.00")
        ))) {
            added++;
        }

        if (employeeService.addEmployee(new EmployeeDto(
                "Мария", "Кузнецова", 1993,
                new AddressDto("Москва", "Садовая", "7", "123001"),
                LocalDate.of(2021, 6, 15),
                null,
                "Администратор",
                new BigDecimal("55000.00")
        ))) {
            added++;
        }

        if (employeeService.addEmployee(new EmployeeDto(
                "Павел", "Морозов", 1985,
                new AddressDto("Химки", "Ленина", "3", "141400"),
                LocalDate.of(2019, 1, 10),
                LocalDate.of(2025, 12, 31),
                "Менеджер зала",
                new BigDecimal("90000.00")
        ))) {
            added++;
        }

        if (employeeService.addEmployee(new EmployeeDto(
                "Ирина", "Белова", 1990,
                new AddressDto("Мытищи", "Мира", "15", "141008"),
                LocalDate.of(2022, 9, 1),
                null,
                "Инструктор по плаванию",
                new BigDecimal("68000.00")
        ))) {
            added++;
        }

        return added;
    }

    private static int addInitialServices(ProvidedServiceService providedServiceService) {
        int addedServices = 0;

        if (providedServiceService.addService(new ProvidedServiceDto("Теннис", new BigDecimal("1500.00")))) {
            addedServices++;
        }
        if (providedServiceService.addService(new ProvidedServiceDto("Плавание", new BigDecimal("1200.00")))) {
            addedServices++;
        }
        if (providedServiceService.addService(new ProvidedServiceDto("Футбол", new BigDecimal("2000.00")))) {
            addedServices++;
        }
        if (providedServiceService.addService(new ProvidedServiceDto("Баскетбол", new BigDecimal("1800.00")))) {
            addedServices++;
        }

        return addedServices;
    }

    private static Long addInitialFacilities(FacilityService facilityService) {
        facilityService.addFacility(new FacilityDto(
                "Тренажёрный зал",
                "GYM-001",
                30,
                FacilityStatus.ACTIVE,
                new BigDecimal("1500.00"),
                "Футбол"
        ));

        facilityService.addFacility(new FacilityDto(
                "Йога-студия",
                "YOGA-001",
                12,
                FacilityStatus.ACTIVE,
                new BigDecimal("900.00"),
                "Плавание"
        ));

        facilityService.addFacility(new FacilityDto(
                "Массажный кабинет",
                "MASSAGE-001",
                4,
                FacilityStatus.ACTIVE,
                new BigDecimal("2500.00"),
                "Теннис"
        ));

        facilityService.addFacility(new FacilityDto(
                "Зал пилатеса",
                "PILATES-001",
                15,
                FacilityStatus.ACTIVE,
                new BigDecimal("1100.00"),
                "Баскетбол"
        ));

        return facilityService.findIdByIdentificationNumber("GYM-001").orElse(null);
    }

    private static void addInitialVisits(VisitService visitService, VisitorRepository visitorRepository) {
        Long ivanId = visitorRepository.findByFullNameAndBirthYear("Иван", "Петров", 1998)
                .map(visitor -> visitor.getId())
                .orElse(null);
        Long annaId = visitorRepository.findByFullNameAndBirthYear("Анна", "Смирнова", 2002)
                .map(visitor -> visitor.getId())
                .orElse(null);

        if (ivanId != null) {
            visitService.addVisit(ivanId, new VisitDto(LocalDate.of(2026, 6, 10), new BigDecimal("1500.00")));
            visitService.addVisit(ivanId, new VisitDto(LocalDate.of(2026, 6, 15), new BigDecimal("2000.00")));
        }
        if (annaId != null) {
            visitService.addVisit(annaId, new VisitDto(LocalDate.of(2026, 6, 20), new BigDecimal("3000.00")));
            visitService.addVisit(annaId, new VisitDto(LocalDate.of(2026, 6, 28), new BigDecimal("2500.00")));
        }
    }

    private static Long addInitialAppointments(AppointmentService appointmentService,
                                               VisitorRepository visitorRepository,
                                               FacilityService facilityService) {
        Long ivanId = visitorRepository.findByFullNameAndBirthYear("Иван", "Петров", 1998)
                .map(visitor -> visitor.getId())
                .orElse(null);
        Long annaId = visitorRepository.findByFullNameAndBirthYear("Анна", "Смирнова", 2002)
                .map(visitor -> visitor.getId())
                .orElse(null);
        Long dmitryId = visitorRepository.findByFullNameAndBirthYear("Дмитрий", "Козлов", 1991)
                .map(visitor -> visitor.getId())
                .orElse(null);

        Long gymId = facilityService.findIdByIdentificationNumber("GYM-001").orElse(null);
        Long yogaId = facilityService.findIdByIdentificationNumber("YOGA-001").orElse(null);
        Long massageId = facilityService.findIdByIdentificationNumber("MASSAGE-001").orElse(null);

        if (ivanId != null && gymId != null) {
            appointmentService.addAppointment(ivanId, gymId,
                    new AppointmentDto(LocalDate.of(2026, 7, 10), LocalTime.of(10, 0)));
        }
        if (annaId != null && yogaId != null) {
            appointmentService.addAppointment(annaId, yogaId,
                    new AppointmentDto(LocalDate.of(2026, 7, 11), LocalTime.of(12, 30)));
            appointmentService.addAppointment(annaId, yogaId,
                    new AppointmentDto(LocalDate.of(2026, 7, 12), LocalTime.of(18, 0)));
        }
        if (dmitryId != null && massageId != null) {
            appointmentService.addAppointment(dmitryId, massageId,
                    new AppointmentDto(LocalDate.of(2026, 7, 15), LocalTime.of(15, 0)));
        }
        if (ivanId != null && yogaId != null) {
            appointmentService.addAppointment(ivanId, yogaId,
                    new AppointmentDto(LocalDate.of(2026, 7, 16), LocalTime.of(9, 0)));
        }

        return yogaId;
    }

    private static void printVisitors(List<VisitorDto> visitors) {
        for (VisitorDto visitor : visitors) {
            System.out.println(visitor);
        }
    }

    private static void printEmployees(List<EmployeeDto> employees) {
        for (EmployeeDto employee : employees) {
            System.out.println(employee);
        }
    }

    private static void printServices(List<ProvidedServiceDto> services) {
        for (ProvidedServiceDto service : services) {
            System.out.println(service);
        }
    }

    private static void printFacilities(List<FacilityDto> facilities) {
        for (FacilityDto facility : facilities) {
            System.out.println(facility);
        }
    }

    private static void printVisits(List<Visit> visits) {
        if (visits.isEmpty()) {
            System.out.println("Посещений нет");
            return;
        }
        for (Visit visit : visits) {
            System.out.println(visit);
        }
    }

    private static void printAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("Записей нет");
            return;
        }
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }
}
