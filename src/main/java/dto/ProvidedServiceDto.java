package dto;

import java.math.BigDecimal;

public class ProvidedServiceDto {

    private String serviceName;
    private BigDecimal price;

    public ProvidedServiceDto() {
    }

    public ProvidedServiceDto(String serviceName, BigDecimal price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProvidedServiceDto{" +
                "serviceName='" + serviceName + '\'' +
                ", price=" + price +
                '}';
    }
}
