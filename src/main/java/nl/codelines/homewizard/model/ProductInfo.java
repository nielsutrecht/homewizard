package nl.codelines.homewizard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductInfo {
    private final String productType;
    private final String productName;
    private final String serial;
    private final String firmwareVersion;
    private final String apiVersion;

    public ProductInfo(
            @JsonProperty("product_type") String productType,
            @JsonProperty("product_name") String productName,
            @JsonProperty("serial") String serial,
            @JsonProperty("firmware_version") String firmwareVersion,
            @JsonProperty("api_version") String apiVersion) {
        this.productType = productType;
        this.productName = productName;
        this.serial = serial;
        this.firmwareVersion = firmwareVersion;
        this.apiVersion = apiVersion;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductName() {
        return productName;
    }

    public String getSerial() {
        return serial;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "productType='" + productType + '\'' +
                ", productName='" + productName + '\'' +
                ", serial='" + serial + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                '}';
    }
}
