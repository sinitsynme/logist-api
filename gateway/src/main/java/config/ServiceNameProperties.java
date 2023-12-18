package config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "service-names")
@ConstructorBinding
public class ServiceNameProperties {
    private String productServiceName;
    private String warehouseServiceName;
    private String orderServiceName;

    public ServiceNameProperties() {
    }

    public ServiceNameProperties(String productServiceName, String warehouseServiceName, String orderServiceName) {
        this.productServiceName = productServiceName;
        this.warehouseServiceName = warehouseServiceName;
        this.orderServiceName = orderServiceName;
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

    public List<String> getAllServiceNames() {
        return List.of(
                productServiceName,
                orderServiceName,
                warehouseServiceName
        );
    }
}
