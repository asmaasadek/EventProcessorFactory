package tech.picnic.assignment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.picnic.assignment.entities.Pick;
import tech.picnic.assignment.entities.Picker;
import tech.picnic.assignment.entities.PickerOutput;

import java.io.*;
import java.util.Date;
import java.util.TreeMap;

/**
 * PickingEventProcessor used to apply the process according to the Task specifications
 */
public class PickingEventProcessor extends BaseStreamProcessor {

    @Override
    public void process(InputStream source, OutputStream sink) {
        processOutputStream(processInputStream(source), sink);
    }

    public void processOutputStream(TreeMap<Picker, TreeMap<Date, String>> result
            , OutputStream sink) {
        result.forEach(
                PickerOutput::mapPickerOutput);
        try {
            byte[] data = new ObjectMapper().writeValueAsBytes(PickerOutput.pickers);
            sink.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeMap<Picker, TreeMap<Date, String>> processInputStream(InputStream source) {
        TreeMap<Picker, TreeMap<Date, String>> result = new TreeMap<>();
        int counter = 0;
        try (source;
             InputStreamReader isReader = new InputStreamReader(source);
             BufferedReader reader = new BufferedReader(isReader)) {
            ObjectMapper mapper = new ObjectMapper();
            long maxTimeMillis = System.currentTimeMillis() + duration.toMillis();
            String str = reader.readLine();
            while (str != null && System.currentTimeMillis() < maxTimeMillis &&
                    counter < maxEvents) {
                if (!"\\n".equalsIgnoreCase(str) &&
                        !System.getProperty("line.separator").equalsIgnoreCase(str)) {
                    Pick p = mapper.readValue(str, Pick.class);
                    if (p.filterPicksByTempZone() != null)
                        p.mapPicksToResult(result);
                    str = reader.readLine();
                    counter++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}

