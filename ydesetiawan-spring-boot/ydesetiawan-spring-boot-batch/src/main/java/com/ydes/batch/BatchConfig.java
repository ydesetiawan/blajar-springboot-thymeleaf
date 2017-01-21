package com.ydes.batch;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.Calendar;
import org.quartz.SchedulerException;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BatchConfig.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    CalendarSettings calendarSettings;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private JobRegistry jobRegistry;

	@Bean
	public List<Calendar> defineCalendars(SchedulerFactoryBean schedulerFactory) {
		if (calendarSettings == null
				|| calendarSettings.getCalendarNames() == null) {
			return Collections.emptyList();
		}
		List<Calendar> calendars = new LinkedList<>();
		for (String name : calendarSettings.getCalendarNames()) {
			HolidayCalendar cal = new HolidayCalendar();
			try {
				for (Date date : calendarSettings.getDatesForCalendar(name)) {
					cal.addExcludedDate(date);
				}
				schedulerFactory.getScheduler().addCalendar(name, cal, true,
						true);
				calendars.add(cal);
			} catch (ParseException | SchedulerException e) {
				throw new IllegalStateException("Cannot initialize calendar: "
						+ name, e);
			}
		}
		return calendars;
	}

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        Map<String, Object> schedulerContextMap = new HashMap<>();
        schedulerContextMap.put("jobLocator", jobLocator);
        schedulerContextMap.put("jobLauncher", jobLauncher);
        schedulerContextMap.put("jobExplorer", jobExplorer);
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setAutoStartup(false);
        schedulerFactory.setSchedulerContextAsMap(schedulerContextMap);
        return schedulerFactory;
    }
}
