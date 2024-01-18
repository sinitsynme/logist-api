package ru.sinitsynme.logistapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "service-names")
public class ServiceNameProperties {
    private String productServiceName;
    private String warehouseServiceName;
    private String orderServiceName;
    private String authServiceName;

    public ServiceNameProperties() {
    }

    public ServiceNameProperties(String productServiceName,
                                 String warehouseServiceName,
                                 String orderServiceName,
                                 String authServiceName) {
        this.productServiceName = productServiceName;
        this.warehouseServiceName = warehouseServiceName;
        this.orderServiceName = orderServiceName;
        this.authServiceName = authServiceName;
    }

    public String getProductServiceName() {
        return productServiceName;
    }

    public void setProductServiceName(String productServiceName) {
        this.productServiceName = productServiceName;
    }

    public String getWarehouseServiceName() {
        return warehouseServiceName;
    }

    public void setWarehouseServiceName(String warehouseServiceName) {
        this.warehouseServiceName = warehouseServiceName;
    }

    public String getOrderServiceName() {
        return orderServiceName;
    }

    public void setOrderServiceName(String orderServiceName) {
        this.orderServiceName = orderServiceName;
    }

    public String getAuthServiceName() {
        return authServiceName;
    }

    public void setAuthServiceName(String authServiceName) {
        this.authServiceName = authServiceName;
    }

    public List<String> getAllServiceNames() {
        return List.of(
                productServiceName,
                orderServiceName,
                warehouseServiceName,
                authServiceName
        );
    }
}
