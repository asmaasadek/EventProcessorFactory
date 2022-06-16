package tech.picnic.assignment.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.picnic.assignment.api.Filter;
import tech.picnic.assignment.utility.DateTimeUtility;
import tech.picnic.assignment.utility.TemperatureZones;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pick implements Serializable, Filter {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("id")
    private String id;

    @JsonProperty("picker")
    private Picker picker;

    @JsonProperty("article")
    private Article article;

    @JsonProperty("quantity")
    private Integer quantity;

    public String getTimestamp() {
        return timestamp;
    }

    public Picker getPicker() {
        return picker;
    }

    public Article getArticle() {
        return article;
    }

    public void mapPicksToResult(TreeMap<Picker, TreeMap<Date, String>> result) {
        Date date = null;
        try {
            date = DateTimeUtility.convertUTCDateToString(this.getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (result.containsKey(this.getPicker())) {
            result.get(this.getPicker()).put(date, this.getArticle().getName());
        } else {
            TreeMap<Date, String> picks = new TreeMap<>();
            picks.put(date, this.getArticle().getName());
            result.put(this.getPicker(), picks);
        }
    }

    @Override
    public Pick filterPicksByTempZone() {
        if (TemperatureZones.ambient.name().equals(this.getArticle().getTemperature_zone()))
            return this;
        return null;
    }
}
