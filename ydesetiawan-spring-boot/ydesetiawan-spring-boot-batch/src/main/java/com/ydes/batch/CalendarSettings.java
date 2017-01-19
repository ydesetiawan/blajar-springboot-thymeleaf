package com.ydes.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author edys
 * @version 1.0, May 31, 2014
 * @since 3.1.0
 */
@Configuration
@ConfigurationProperties
public class CalendarSettings {

    private Map<String, String> calendars;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Set<String> getCalendarNames() {
        if (calendars == null) {
            return null;
        }
        return calendars.keySet();
    }

    public Map<String, String> getCalendars() {
        return calendars;
    }

    public List<Date> getDatesForCalendar(String name) throws ParseException {
        if (calendars == null || calendars.get(name) == null) {
            return null;
        }
        List<Date> dates = new LinkedList<Date>();
        for (String dateValue : calendars.get(name).split(",")) {
            dates.add(dateFormat.parse(dateValue));
        }
        return dates;
    }

    public void setCalendars(Map<String, String> calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return "CalendarSettings [calendars=" + calendars + "]";
    }
}
