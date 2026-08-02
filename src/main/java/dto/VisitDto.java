package dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VisitDto {

    private LocalDate visitDate;
    private BigDecimal spentAmount;

    public VisitDto() {
    }

    public VisitDto(LocalDate visitDate, BigDecimal spentAmount) {
        this.visitDate = visitDate;
        this.spentAmount = spentAmount;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    @Override
    public String toString() {
        return "VisitDto{" +
                "visitDate=" + visitDate +
                ", spentAmount=" + spentAmount +
                '}';
    }
}
