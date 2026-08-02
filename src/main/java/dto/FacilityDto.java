package dto;

import entity.FacilityStatus;

import java.math.BigDecimal;

public class FacilityDto {

    private String facilityName;
    private String identificationNumber;
    private int maxCapacity;
    private FacilityStatus status;
    private BigDecimal hourlyRentalCost;
    private String serviceName;

    public FacilityDto() {
    }

    public FacilityDto(String facilityName, String identificationNumber, int maxCapacity,
                       FacilityStatus status, BigDecimal hourlyRentalCost, String serviceName) {
        this.facilityName = facilityName;
        this.identificationNumber = identificationNumber;
        this.maxCapacity = maxCapacity;
        this.status = status;
        this.hourlyRentalCost = hourlyRentalCost;
        this.serviceName = serviceName;
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

    public FacilityStatus getStatus() {
        return status;
    }

    public void setStatus(FacilityStatus status) {
        this.status = status;
    }

    public BigDecimal getHourlyRentalCost() {
        return hourlyRentalCost;
    }

    public void setHourlyRentalCost(BigDecimal hourlyRentalCost) {
        this.hourlyRentalCost = hourlyRentalCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "FacilityDto{" +
                "facilityName='" + facilityName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", status=" + status +
                ", hourlyRentalCost=" + hourlyRentalCost +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
