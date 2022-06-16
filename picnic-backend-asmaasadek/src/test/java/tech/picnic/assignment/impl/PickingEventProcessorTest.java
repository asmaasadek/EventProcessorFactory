package tech.picnic.assignment.impl;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import tech.picnic.assignment.entities.Picker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

final class PickingEventProcessorTest {

    static TreeMap<Picker, TreeMap<Date, String>> expectedTreemapAsResult = new TreeMap<>();
    static Picker p = new Picker("14", "Joris", "2018-09-20T08:20:00Z");

    static Stream<Arguments> inputStreamProvider() {
        return Stream.of(
                Arguments.of(
                        "happy-path-input.json-stream"
                ));
    }

    static Stream<Arguments> outputStreamProvider() {
        return Stream.of(
                Arguments.of(
                        "happy-path-output.json"
                ));
    }

    @BeforeAll
    private static void loadTreemap() {
        TreeMap<Date, String> internalResult = new TreeMap<>();
        List<LocalDateTime> list = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2018, 12, 20, 11, 50, 48),
                LocalDateTime.of(2018, 12, 20, 11, 51),
                LocalDateTime.of(2018, 12, 20, 11, 50, 49)
        ));
        List<Date> dates = new ArrayList();
        for (LocalDateTime l : list) {
            Instant instant = l.atZone(ZoneId.systemDefault()).toInstant();
            dates.add(Date.from(instant));
        }

        internalResult.put(dates.get(0), "ACME BANANAS");
        internalResult.put(dates.get(1), "ACME APPLES");
        expectedTreemapAsResult.put(p,
                internalResult);
        internalResult = new TreeMap<>();
        internalResult.put(dates.get(2), "ACME BANANAS");
        expectedTreemapAsResult.put(new Picker("15", "Jan", "2018-11-14T08:20:15Z"),
                internalResult);
    }

    private String loadResource(String resource) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resource);
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    @ParameterizedTest
    @MethodSource("inputStreamProvider")
    void testProcessInputStream(
            String inputResource
    ) throws IOException {
        try (PickingEventProcessor processor = new PickingEventProcessor();
             InputStream source = getClass().getResourceAsStream(inputResource)) {
            processor.initializeConditions(3, Duration.ofSeconds(50));
            TreeMap<Picker, TreeMap<Date, String>> actualOutput =
                    processor.processInputStream(source);

            //Assert that picks are grouped by PickerName
            Assertions.assertTrue(actualOutput.get(p).size() ==
                    expectedTreemapAsResult.get(p).size());

            //Assert that pickers are sorted by active_since
            Assertions.assertIterableEquals(
                    Arrays.asList(expectedTreemapAsResult.keySet().stream().iterator().next().getActive_since()),
                    Arrays.asList(actualOutput.keySet().stream().iterator().next().getActive_since()));

            //Assert that picks are sorted by Timestamp
            Assertions.assertIterableEquals(
                    expectedTreemapAsResult.values().stream().iterator().next().keySet(),
                    actualOutput.values().stream().iterator().next().keySet());
        }
    }

    @ParameterizedTest
    @MethodSource("outputStreamProvider")
    void testProcessOutputStream(String outputResource) throws IOException, JSONException {
        try (PickingEventProcessor processor = new PickingEventProcessor();
             ByteArrayOutputStream actualOutputStream = new ByteArrayOutputStream()
        ) {
            String expectedOutput = loadResource(outputResource);
            processor.processOutputStream(expectedTreemapAsResult,
                    (actualOutputStream));
            String actualOutput = actualOutputStream.toString(StandardCharsets.UTF_8);

            JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.STRICT);
        }
    }
}
