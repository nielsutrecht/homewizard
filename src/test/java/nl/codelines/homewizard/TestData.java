package nl.codelines.homewizard;

import nl.codelines.homewizard.model.Measurement;
import nl.codelines.homewizard.model.State;

public class TestData {
    public static final Measurement HWE_P1 = new Measurement(
        50,
        "ISKRA  2M550T-101",
        "My Wi-Fi",
        100,
        10830.511,
        2948.827,
        1285.951,
        2876.51,
        -543.0,
        -676.0,
        133.0,
        0.0,
        2569.646,
        "210606140010"
    );

    public static final Measurement SDM230 = new Measurement(
        null,
        null,
        "My Wi-Fi",
        100,
        10830.0,
        null,
        1285.951,
        null,
        640.0,
        640.0,
        null,
        null,
        null,
        null
    );

    public static final Measurement SDM630 = new Measurement(
        null,
        null,
        "My Wi-Fi",
        100,
        10830.511,
        null,
        1285.951,
        null,
        -543.0,
        -676.0,
        133.0,
        0.0,
        null,
        null
    );

    public static final State STATE = new State(
        true,
        false,
        255);
}
