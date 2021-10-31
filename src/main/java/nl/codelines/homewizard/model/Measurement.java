package nl.codelines.homewizard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Measurement {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    private final int smrVersion;
    private final String meterModel;
    private final String wifiSsid;
    private final int wifiStrength;
    private final double totalPowerImportT1;
    private final double totalPowerImportT2;
    private final double totalPowerExportT1;
    private final double totalPowerExportT2;
    private final double activePower;
    private final double activePowerL1;
    private final double activePowerL2;
    private final double activePowerL3;
    private final double totalGas;
    private final LocalDateTime gasTimestamp;

    public Measurement(@JsonProperty("smr_version") int smrVersion,
                        @JsonProperty("meter_model") String meterModel,
                       @JsonProperty("wifi_ssid") String wifiSsid,
                       @JsonProperty("wifi_strength") int wifiStrength,
                       @JsonProperty("total_power_import_t1_kwh") double totalPowerImportT1,
                       @JsonProperty("total_power_import_t2_kwh") double totalPowerImportT2,
                       @JsonProperty("total_power_export_t1_kwh") double totalPowerExportT1,
                       @JsonProperty("total_power_export_t2_kwh") double totalPowerExportT2,
                       @JsonProperty("active_power_w") double activePower,
                       @JsonProperty("active_power_l1_w") double activePowerL1,
                       @JsonProperty("active_power_l2_w") double activePowerL2,
                       @JsonProperty("active_power_l3_w") double activePowerL3,
                       @JsonProperty("total_gas_m3") double totalGas,
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
        this.gasTimestamp = LocalDateTime.parse(gasTimestamp, TIMESTAMP_FORMAT);
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
}
