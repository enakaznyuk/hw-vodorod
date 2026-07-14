import dto.ClientDto;
import dto.FacilityDto;
import dto.ProvidedServiceDto;
import entity.ClientStatus;
import entity.FacilityStatus;
import jakarta.persistence.EntityManagerFactory;
import repository.ClientRepository;
import repository.FacilityRepository;
import repository.ProvidedServiceRepository;
import service.ClientService;
import service.FacilityService;
import service.ProvidedServiceService;
import util.HibernateUtil;
import util.JpaUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

        ClientRepository clientRepository = new ClientRepository(entityManagerFactory);
        ClientService clientService = new ClientService(clientRepository);

        ProvidedServiceRepository providedServiceRepository = new ProvidedServiceRepository();
        ProvidedServiceService providedServiceService = new ProvidedServiceService(providedServiceRepository);

        FacilityRepository facilityRepository = new FacilityRepository();
        FacilityService facilityService = new FacilityService(facilityRepository);

        System.out.println("=== Клиенты (EntityManager) ===");
        int addedClients = addInitialClients(clientService);
        System.out.println("Добавлено новых клиентов: " + addedClients);
        printClients(clientService.getAllClients());

        System.out.println("\n=== Поиск клиента по id через Session ===");
        clientService.findClientById(1L)
                .ifPresentOrElse(
                        client -> System.out.println("Найден клиент: " + client),
                        () -> System.out.println("Клиент с id=1 не найден")
                );

        System.out.println("\n=== Услуги (Session) ===");
        int addedServices = addInitialServices(providedServiceService);
        System.out.println("Добавлено новых услуг: " + addedServices);
        printServices(providedServiceService.getAllServices());

        System.out.println("\n=== Помещения (Session) ===");
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

        System.out.println("\n=== Клиенты: смена статуса и удаление ===");
        clientService.changeClientStatus(2L, ClientStatus.PREMIUM);
        clientService.deleteClient(5L);
        printClients(clientService.getAllClients());

        JpaUtil.shutdown();
        HibernateUtil.shutdown();
    }

    private static int addInitialClients(ClientService clientService) {
        int addedClients = 0;

        if (clientService.addClient(createClient(
                "Иван", "Петров", 28, "+7-900-111-22-33",
                LocalDate.of(2026, 6, 15), ClientStatus.ACTIVE, "12500.00"))) {
            addedClients++;
        }

        if (clientService.addClient(createClient(
                "Анна", "Смирнова", 24, "+7-900-222-33-44",
                LocalDate.of(2026, 6, 28), ClientStatus.PREMIUM, "45800.50"))) {
            addedClients++;
        }

        if (clientService.addClient(createClient(
                "Дмитрий", "Козлов", 35, "+7-900-333-44-55",
                LocalDate.of(2026, 5, 10), ClientStatus.ACTIVE, "8700.00"))) {
            addedClients++;
        }

        if (clientService.addClient(createClient(
                "Елена", "Волкова", 31, "+7-900-444-55-66",
                LocalDate.of(2026, 3, 2), ClientStatus.BLOCKED, "3200.75"))) {
            addedClients++;
        }

        if (clientService.addClient(createClient(
                "Сергей", "Новиков", 42, "+7-900-555-66-77",
                LocalDate.of(2026, 7, 1), ClientStatus.ACTIVE, "15600.00"))) {
            addedClients++;
        }

        return addedClients;
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
        FacilityDto gym = new FacilityDto(
                "Тренажёрный зал",
                "GYM-001",
                30,
                FacilityStatus.ACTIVE,
                new BigDecimal("1500.00")
        );

        facilityService.addFacility(gym);
        return facilityService.findIdByIdentificationNumber("GYM-001").orElse(null);
    }

    private static ClientDto createClient(String firstName, String lastName, int age, String phoneNumber,
                                          LocalDate lastVisitDate, ClientStatus status, String spentAmount) {
        return new ClientDto(
                firstName,
                lastName,
                age,
                phoneNumber,
                lastVisitDate,
                status,
                new BigDecimal(spentAmount)
        );
    }

    private static void printClients(List<ClientDto> clients) {
        for (ClientDto client : clients) {
            System.out.println(client);
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
}
