package tech.picnic.assignment.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.picnic.assignment.utility.DateTimeUtility;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Picker implements Serializable, Comparable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("active_since")
    private String active_since;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getActive_since() {
        return active_since;
    }

    @Override
    public int compareTo(Object o) {
        Picker that = (Picker) o;
        if (that.getName().equalsIgnoreCase(this.getName()))
            return 0;

        Date thisDate = null;
        Date thatDate = null;
        try {
            thisDate = DateTimeUtility.convertUTCDateToString(this.getActive_since());
            thatDate = DateTimeUtility.convertUTCDateToString(that.getActive_since());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int val = thisDate.compareTo(thatDate);
        if (val == 0)
            return this.getId().compareTo(that.getId());

        return val;
    }

    public Picker() {
    }

    public Picker(String id, String name, String active_since) {
        this.id = id;
        this.name = name;
        this.active_since = active_since;
    }
}
