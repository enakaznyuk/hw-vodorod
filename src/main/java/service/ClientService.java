package service;

import dto.ClientDto;
import entity.Client;
import entity.ClientStatus;
import org.hibernate.exception.ConstraintViolationException;
import repository.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean addClient(ClientDto clientDto) {
        if (clientRepository.existsByPhoneNumber(clientDto.getPhoneNumber())) {
            System.out.println("Клиент с телефоном " + clientDto.getPhoneNumber() + " уже существует, пропускаем");
            return false;
        }

        try {
            clientRepository.save(toEntity(clientDto));
            return true;
        } catch (RuntimeException exception) {
            if (isDuplicateKeyError(exception)) {
                System.out.println("Не удалось добавить клиента " + clientDto.getFirstName() + " " +
                        clientDto.getLastName() + ": запись с такими данными уже есть в базе");
                return false;
            }
            throw exception;
        }
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

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ClientDto> findClientById(Long id) {
        return clientRepository.findByIdViaSession(id).map(this::toDto);
    }

    public void deleteClient(Long id) {
        if (clientRepository.findById(id).isEmpty()) {
            System.out.println("Клиент с id=" + id + " не найден, удаление пропущено");
            return;
        }
        clientRepository.deleteById(id);
    }

    public void changeClientStatus(Long id, ClientStatus status) {
        if (clientRepository.findById(id).isEmpty()) {
            System.out.println("Клиент с id=" + id + " не найден, смена статуса пропущена");
            return;
        }
        clientRepository.updateStatus(id, status);
    }

    private Client toEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setAge(clientDto.getAge());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        client.setLastVisitDate(clientDto.getLastVisitDate());
        client.setStatus(clientDto.getStatus());
        client.setSpentAmount(clientDto.getSpentAmount());
        return client;
    }

    private ClientDto toDto(Client client) {
        return new ClientDto(
                client.getFirstName(),
                client.getLastName(),
                client.getAge(),
                client.getPhoneNumber(),
                client.getLastVisitDate(),
                client.getStatus(),
                client.getSpentAmount()
        );
    }
}
