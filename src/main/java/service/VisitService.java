package service;

import dto.VisitDto;
import entity.Visit;
import entity.Visitor;
import repository.VisitRepository;
import repository.VisitorRepository;

import java.util.List;

public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;

    public VisitService(VisitRepository visitRepository, VisitorRepository visitorRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
    }

    public boolean addVisit(Long visitorId, VisitDto visitDto) {
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        if (visitor == null) {
            System.out.println("Посетитель с id=" + visitorId + " не найден, посещение не добавлено");
            return false;
        }

        Visit visit = new Visit();
        visit.setVisitDate(visitDto.getVisitDate());
        visit.setSpentAmount(visitDto.getSpentAmount());
        visit.setVisitor(visitor);
        visitRepository.save(visit);
        return true;
    }

    public List<VisitDto> getAllVisits() {
        return visitRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<Visit> getAllVisitEntities() {
        return visitRepository.findAll();
    }

    private VisitDto toDto(Visit visit) {
        return new VisitDto(visit.getVisitDate(), visit.getSpentAmount());
    }
}
