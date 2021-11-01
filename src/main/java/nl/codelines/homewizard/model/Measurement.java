package nl.codelines.homewizard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Measurement {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    private final Integer smrVersion;
    private final String meterModel;
    private final String wifiSsid;
    private final Integer wifiStrength;
    private final Double totalPowerImportT1;
    private final Double totalPowerImportT2;
    private final Double totalPowerExportT1;
    private final Double totalPowerExportT2;
    private final Double activePower;
    private final Double activePowerL1;
    private final Double activePowerL2;
    private final Double activePowerL3;
    private final Double totalGas;
    private final LocalDateTime gasTimestamp;

    public Measurement(@JsonProperty("smr_version") Integer smrVersion,
                        @JsonProperty("meter_model") String meterModel,
                       @JsonProperty("wifi_ssid") String wifiSsid,
                       @JsonProperty("wifi_strength") Integer wifiStrength,
                       @JsonProperty("total_power_import_t1_kwh") Double totalPowerImportT1,
                       @JsonProperty("total_power_import_t2_kwh") Double totalPowerImportT2,
                       @JsonProperty("total_power_export_t1_kwh") Double totalPowerExportT1,
                       @JsonProperty("total_power_export_t2_kwh") Double totalPowerExportT2,
                       @JsonProperty("active_power_w") Double activePower,
                       @JsonProperty("active_power_l1_w") Double activePowerL1,
                       @JsonProperty("active_power_l2_w") Double activePowerL2,
                       @JsonProperty("active_power_l3_w") Double activePowerL3,
                       @JsonProperty("total_gas_m3") Double totalGas,
                       @JsonProperty("gas_timestamp") String gasTimestamp) {
        this.smrVersion = smrVersion;
        this.meterModel = meterModel;
        this.wifiSsid = wifiSsid;
        this.wifiStrength = wifiStrength;
        this.totalPowerImportT1 = totalPowerImportT1;
        this.totalPowerImportT2 = totalPowerImportT2;
        this.totalPowerExportT1 = totalPowerExportT1;
        this.totalPowerExportT2 = totalPowerExportT2;
        this.activePower = activePower;
        this.activePowerL1 = activePowerL1;
        this.activePowerL2 = activePowerL2;
        this.activePowerL3 = activePowerL3;
        this.totalGas = totalGas;
        this.gasTimestamp = gasTimestamp == null ? null : LocalDateTime.parse(gasTimestamp, TIMESTAMP_FORMAT);
    }

    public static DateTimeFormatter getTimestampFormat() {
        return TIMESTAMP_FORMAT;
    }

    public int getSmrVersion() {
        return smrVersion;
    }

    public String getMeterModel() {
        return meterModel;
    }

    public String getWifiSsid() {
        return wifiSsid;
    }

    public int getWifiStrength() {
        return wifiStrength;
    }

    public double getTotalPowerImportT1() {
        return totalPowerImportT1;
    }

    public double getTotalPowerImportT2() {
        return totalPowerImportT2;
    }

    public double getTotalPowerExportT1() {
        return totalPowerExportT1;
    }

    public double getTotalPowerExportT2() {
        return totalPowerExportT2;
    }

    public double getActivePower() {
        return activePower;
    }

    public double getActivePowerL1() {
        return activePowerL1;
    }

    public double getActivePowerL2() {
        return activePowerL2;
    }

    public double getActivePowerL3() {
        return activePowerL3;
    }

    public double getTotalGas() {
        return totalGas;
    }

    public LocalDateTime getGasTimestamp() {
        return gasTimestamp;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "smrVersion=" + smrVersion +
                ", meterModel='" + meterModel + '\'' +
                ", wifiSsid='" + wifiSsid + '\'' +
                ", wifiStrength=" + wifiStrength +
                ", totalPowerImportT1=" + totalPowerImportT1 +
                ", totalPowerImportT2=" + totalPowerImportT2 +
                ", totalPowerExportT1=" + totalPowerExportT1 +
                ", totalPowerExportT2=" + totalPowerExportT2 +
                ", activePower=" + activePower +
                ", activePowerL1=" + activePowerL1 +
                ", activePowerL2=" + activePowerL2 +
                ", activePowerL3=" + activePowerL3 +
                ", totalGas=" + totalGas +
                ", gasTimestamp=" + gasTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Objects.equals(smrVersion, that.smrVersion) && Objects.equals(meterModel, that.meterModel) && Objects.equals(wifiSsid, that.wifiSsid) && Objects.equals(wifiStrength, that.wifiStrength) && Objects.equals(totalPowerImportT1, that.totalPowerImportT1) && Objects.equals(totalPowerImportT2, that.totalPowerImportT2) && Objects.equals(totalPowerExportT1, that.totalPowerExportT1) && Objects.equals(totalPowerExportT2, that.totalPowerExportT2) && Objects.equals(activePower, that.activePower) && Objects.equals(activePowerL1, that.activePowerL1) && Objects.equals(activePowerL2, that.activePowerL2) && Objects.equals(activePowerL3, that.activePowerL3) && Objects.equals(totalGas, that.totalGas) && Objects.equals(gasTimestamp, that.gasTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smrVersion, meterModel, wifiSsid, wifiStrength, totalPowerImportT1, totalPowerImportT2, totalPowerExportT1, totalPowerExportT2, activePower, activePowerL1, activePowerL2, activePowerL3, totalGas, gasTimestamp);
    }
}
