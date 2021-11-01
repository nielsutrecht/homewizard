package nl.codelines.homewizard;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import nl.codelines.homewizard.model.Measurement;
import nl.codelines.homewizard.model.ProductInfo;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        stub("/api", "product-info");

        var productInfo = homewizard.getProductInfo();
        var expected = new ProductInfo(
            "HWE-P1",
            "P1 Meter",
            "3c39e7aabbcc",
            "2.11",
            "v1");

        assertThat(productInfo).isEqualTo(expected);
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
            stub("/api/v1/data", resource);
            var measurement = homewizard.getMeasurement();
            assertThat(measurement).isEqualTo(expected);
        });
    }

    @Test
    public void getTelegram() throws Exception {
    }

    @Test
    public void getState() throws Exception {
    }

    private static void stub(String uri, String resource) {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo(uri))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(readResource(resource))
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
