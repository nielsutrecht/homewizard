package nl.codelines.homewizard;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import nl.codelines.homewizard.model.Measurement;
import nl.codelines.homewizard.model.ProductInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.List;

import static nl.codelines.homewizard.TestData.STATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HomewizardIntegrationTest {
    private static WireMockServer wireMockServer;
    private Homewizard homewizard;

    @BeforeAll
    public static void setupWiremock() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    public static void teardownWiremock() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setup() {
        wireMockServer.resetAll();
        homewizard = new Homewizard(wireMockServer.baseUrl());
    }

    @Test
    public void getProductInfo() {
        stubGet("/api", "product-info");

        var productInfo = homewizard.getProductInfo();
        var expected = new ProductInfo(
            "HWE-P1",
            "P1 Meter",
            "3c39e7aabbcc",
            "2.11",
            "v1");

        assertThat(productInfo).isEqualTo(expected);
        assertThat(productInfo.hashCode()).isEqualTo(expected.hashCode());
        assertThat(productInfo.toString()).isEqualTo(expected.toString());
        assertThat(productInfo).usingRecursiveComparison().isEqualTo(expected);
    }

    @TestFactory
    public List<DynamicTest> getMeasurement() {
        return List.of(
            measurement("measurement-hwe-p1", TestData.HWE_P1),
            measurement("measurement-sdm230-wifi", TestData.SDM230),
            measurement("measurement-sdm630-wifi", TestData.SDM630)
        );
    }

    private DynamicTest measurement(String resource, Measurement expected) {
        return DynamicTest.dynamicTest(resource, () -> {
            stubGet("/api/v1/data", resource);
            var measurement = homewizard.getMeasurement();
            assertThat(measurement).isEqualTo(expected);
            assertThat(measurement.hashCode()).isEqualTo(expected.hashCode());
            assertThat(measurement.toString()).isEqualTo(expected.toString());
            assertThat(measurement).usingRecursiveComparison().isEqualTo(expected);
        });
    }

    @Test
    public void getTelegram() throws Exception {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/api/v1/telegram"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "text/plainn")
                        .withBody("TELEGRAM DATA")
                )
        );

        var telegram = homewizard.getTelegram();
        assertThat(telegram.getData()).isEqualTo("TELEGRAM DATA");
    }

    @Test
    public void getState() {
        stubGet("/api/v1/state", "state");

        var state = homewizard.getState();
        assertThat(state).isEqualTo(STATE);
        assertThat(state.hashCode()).isEqualTo(STATE.hashCode());
        assertThat(state.toString()).isEqualTo(STATE.toString());
        assertThat(state).usingRecursiveComparison().isEqualTo(STATE);
    }

    @TestFactory
    public List<DynamicTest> setState() {
        stubPut("/api/v1/state", "state");

        return List.of(
            stateUpdate(null, null, null,  "{}"),
            stateUpdate(true, null, null,  "{\"power_on\":true}"),
            stateUpdate(null, true, null,  "{\"switch_lock\":true}"),
            stateUpdate(null, null, 255,  "{\"brightness\":255}"),
            stateUpdate(true, false, 0,  "{\"power_on\":true,\"switch_lock\":false,\"brightness\":0}")
        );
    }

    private DynamicTest stateUpdate(Boolean powerOn, Boolean switchLock, Integer brightness, String expected) {
        return DynamicTest.dynamicTest(String.format("%s,%s,%s -> %s", powerOn, switchLock, brightness, expected), () -> {
            wireMockServer.resetRequests();
            var state = homewizard.setState(powerOn, switchLock, brightness);
            assertThat(state).isEqualTo(STATE);
            assertThat(state.hashCode()).isEqualTo(STATE.hashCode());
            assertThat(state.toString()).isEqualTo(STATE.toString());
            assertThat(state).usingRecursiveComparison().isEqualTo(STATE);
            var requests = wireMockServer.findAll(RequestPatternBuilder.allRequests());

            assertThat(requests.get(0).getBodyAsString()).isEqualTo(expected);
        });
    }

    @TestFactory
    public List<DynamicTest> handleErrors() {
        stubError();
        return List.of(
            error("GET .+/api", () -> { homewizard.getProductInfo();}),
            error("GET .+/api/v1/data", () -> { homewizard.getMeasurement();}),
            error("GET .+/api/v1/telegram", () -> { homewizard.getTelegram();}),
            error("GET .+/api/v1/state", () -> { homewizard.getState();}),
            error("PUT .+/api/v1/state", () -> { homewizard.setState(true, true, 10000);})
        );

    }

    private DynamicTest error(String operation, Executable call) {
        return DynamicTest.dynamicTest(operation, () -> {
            var ex = assertThrows(HomewizardException.class, call);
            assertThat(ex.getOperation()).matches(operation);
        });
    }

    private static void stubGet(String uri, String resource) {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo(uri))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(readResource(resource))
                )
        );
    }

    private static void stubPut(String uri, String resource) {
        wireMockServer.stubFor(
            WireMock.put(WireMock.urlEqualTo(uri))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(readResource(resource))
                )
        );
    }

    private static void stubError() {
        wireMockServer.stubFor(
            WireMock.put(WireMock.anyUrl())
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(readResource("error"))
                )
        );
        wireMockServer.stubFor(
            WireMock.get(WireMock.anyUrl())
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(readResource("error"))
                )
        );
    }

    private static byte[] readResource(String resource) {
        var name = String.format("/responses/%s.json", resource);
        var ins = HomewizardIntegrationTest.class.getResourceAsStream(name);
        if (ins == null) {
            throw new IllegalArgumentException("Resource with name " + name + " not found.");
        }
        try {
            return ins.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
