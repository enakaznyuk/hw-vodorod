import dto.ClientDto;
import entity.ClientStatus;
import jakarta.persistence.EntityManagerFactory;
import repository.ClientRepository;
import service.ClientService;
import util.JpaUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();
        ClientRepository clientRepository = new ClientRepository(entityManagerFactory);
        ClientService clientService = new ClientService(clientRepository);

        int addedClients = addInitialClients(clientService);
        System.out.println("Добавлено новых клиентов: " + addedClients);

        System.out.println("\nВсе клиенты:");
        printClients(clientService.getAllClients());

        Long clientIdToUpdate = 2L;
        clientService.changeClientStatus(clientIdToUpdate, ClientStatus.PREMIUM);
        System.out.println("\nСтатус клиента с id=" + clientIdToUpdate + " изменён на PREMIUM");

        Long clientIdToDelete = 5L;
        clientService.deleteClient(clientIdToDelete);
        System.out.println("\nКлиент с id=" + clientIdToDelete + " удалён");

        System.out.println("\nКлиенты после изменений:");
        printClients(clientService.getAllClients());

        JpaUtil.shutdown();
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

    private static ClientDto createClient(String firstName, String lastName, int age, String phoneNumber,
                                          LocalDate lastVisitDate, ClientStatus status, String spentAmount) {
        return new ClientDto(
                null,
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
}
