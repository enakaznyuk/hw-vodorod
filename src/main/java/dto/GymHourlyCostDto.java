package dto;

import java.math.BigDecimal;

public class GymHourlyCostDto {

    private String facilityName;
    private String identificationNumber;
    private int maxCapacity;
    private BigDecimal hourlyRentalCost;
    private BigDecimal costPerPersonPerHour;

    public GymHourlyCostDto() {
    }

    public GymHourlyCostDto(String facilityName, String identificationNumber, int maxCapacity,
                            BigDecimal hourlyRentalCost, BigDecimal costPerPersonPerHour) {
        this.facilityName = facilityName;
        this.identificationNumber = identificationNumber;
        this.maxCapacity = maxCapacity;
        this.hourlyRentalCost = hourlyRentalCost;
        this.costPerPersonPerHour = costPerPersonPerHour;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public BigDecimal getHourlyRentalCost() {
        return hourlyRentalCost;
    }

    public void setHourlyRentalCost(BigDecimal hourlyRentalCost) {
        this.hourlyRentalCost = hourlyRentalCost;
    }

    public BigDecimal getCostPerPersonPerHour() {
        return costPerPersonPerHour;
    }

    public void setCostPerPersonPerHour(BigDecimal costPerPersonPerHour) {
        this.costPerPersonPerHour = costPerPersonPerHour;
    }

    @Override
    public String toString() {
        return "GymHourlyCostDto{" +
                "facilityName='" + facilityName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", hourlyRentalCost=" + hourlyRentalCost +
                ", costPerPersonPerHour=" + costPerPersonPerHour +
                '}';
    }
}
