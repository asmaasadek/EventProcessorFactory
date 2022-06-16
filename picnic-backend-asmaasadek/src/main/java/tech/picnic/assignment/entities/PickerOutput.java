package tech.picnic.assignment.entities;


import tech.picnic.assignment.utility.DateTimeUtility;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is used to represent the expected JSON output
 */
public class PickerOutput {

    public static List<PickerResult> pickers = new ArrayList<>();

    public static List<PickerResult> mapPickerOutput(Picker picker, TreeMap<Date, String> picks) {
        PickerResult pickerResult = new PickerResult();
        pickerResult.active_since = picker.getActive_since();
        pickerResult.picker_name = picker.getName();
        pickerResult.picks = mapInternalPick(picks);
        pickers.add(pickerResult);
        return pickers;
    }

    public static List<Pick> mapInternalPick(TreeMap<Date, String> picks) {
        List<Pick> internalPicks = new ArrayList<>();
        picks.forEach(
                (key, value) -> {
                    PickerOutput.Pick pick = new PickerOutput.Pick();
                    pick.article_name = value;
                    try {
                        pick.timestamp = DateTimeUtility
                                .convertUTCDateToString(key);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    internalPicks.add(pick);
                }
        );
        return internalPicks;
    }

    public static class PickerResult implements Serializable {
        public String picker_name;
        public String active_since;
        public List<Pick> picks = new ArrayList<>();
    }

    public static class Pick {
        public String article_name;
        public String timestamp;
    }
}
