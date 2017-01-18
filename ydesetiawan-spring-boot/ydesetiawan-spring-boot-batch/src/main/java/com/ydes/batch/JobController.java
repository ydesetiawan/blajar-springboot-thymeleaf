package com.ydes.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author edys
 * @version 1.0, May 10, 2014
 * @since 3.1.0
 */
@Controller
public class JobController {

    private static final String REDIRECT_JOBS2 = "redirect:/jobs";
    private static final String JOB = "Job '";
    private static final String EDIT = "edit";
    private static final String JOB_SCHEDULER_TRIGGER = "job_scheduler_trigger";
    private static final String SCHEDULER = "/scheduler";
    private static final String REDIRECT_JOBS = "redirect:/jobs/";
    private static final String TRIGGER_FOR_JOB = "Trigger for job '";
    private static final String SAVE = "save";
    private static final String ALERT_WARNING = "alertWarning";
    private static final String ALERT_SUCCESS = "alertSuccess";
    private static final String JOB_INSTANCES = "job_instances";
    private static final String JOB_EXECUTIONS = "job_executions";
    private static final String JOBNAME = "InboundInvoiceBikWebdavJob";

    private static Logger log = Logger.getLogger(JobController.class);

    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobOperator jobOperator;
    @Autowired
    private SchedulerFactoryBean schedulers;
    @Autowired
    Environment env;

    private JobDetailImpl createJobDetail(String jobName) {
        return createJobDetail(jobName, false);
    }

    private JobDetailImpl createJobDetail(String jobName, boolean forceNew) {
        JobKey jobKey = new JobKey(UUID.randomUUID().toString(), jobName);
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setKey(jobKey);
        jobDetail.setJobClass(JobLauncherDetails.class);
        jobDetail.getJobDataMap().put(JobLauncherDetails.JOB_NAME, jobName);
        jobDetail.getJobDataMap().put(JobLauncherDetails.FORCE_NEW, forceNew);
        return jobDetail;
    }

    private JobDetailImpl createJobDetail(String jobName, boolean forceNew,
            String selectedFiles, String jobFilePath) {
        JobDetailImpl jobDetail = createJobDetail(jobName, forceNew);
        jobDetail.getJobDataMap().put(JobLauncherDetails.JOB_FILE_PATH,
                jobFilePath);
        jobDetail.getJobDataMap().put(JobLauncherDetails.JOB_SELECTED_FILES,
                selectedFiles);
        return jobDetail;
    }

    @RequestMapping("/jobs/{jobName}/{instanceId}/{executionId}")
    public String getJobExecution(Model model, @PathVariable long executionId,
            @PathVariable String jobName) {
        log.debug(jobName);
        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
        model.addAttribute("job_execution", jobExecution);

        return "job_execution";
    }

    @RequestMapping("/jobs/{jobName}/{instanceId}")
    public String getJobExecutions(Model model, @PathVariable long instanceId,
            @PathVariable String jobName) {
        log.debug(jobName);
        JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        if (jobInstance != null) {
            List<JobExecution> jobExecutions = jobExplorer
                    .getJobExecutions(jobInstance);
            model.addAttribute(JOB_EXECUTIONS, jobExecutions);
        }
        return JOB_EXECUTIONS;
    }

    @RequestMapping("/jobs/{jobName}")
    public String getJobInstances(Model model, @PathVariable String jobName,
            @RequestParam(required = false, defaultValue = "0") int start) {
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName,
                start, 10);
        Map<Long, JobExecution> jobExecutions = new HashMap<>();
        for (JobInstance jobInstance : jobInstances) {
            List<JobExecution> jexs = jobExplorer.getJobExecutions(jobInstance);
            if (jexs != null) {
                // add last execution to the list
                JobExecution jex = jexs.get(0);
                jobExecutions.put(jobInstance.getId(), jex);
            } else {
                // job instance has no executions, add id only
                jobExecutions.put(jobInstance.getId(), null);
            }
        }
        model.addAttribute(JOB_INSTANCES, jobInstances);
        model.addAttribute(JOB_EXECUTIONS, jobExecutions);
        model.addAttribute("job_name", jobName);
        model.addAttribute("start", start);
        return JOB_INSTANCES;
    }

    @RequestMapping("/jobs")
    public String getJobs(Model model, @RequestParam(value = "message",
            required = false) String message) {
        Set<String> jobNames = jobOperator.getJobNames();
        Map<String, JobInstance> jobInstances = new HashMap<>();
        Map<String, JobExecution> jobExecutions = new HashMap<>();
        Map<String, Date> jobRuntimes = new HashMap<>();
        for (String jobName : jobNames) {
            List<JobInstance> jins = jobExplorer.getJobInstances(jobName, 0, 1);
            jinsNotEmpty(jobInstances, jobExecutions, jobName, jins);
            Trigger trigger = getNextTriggerForJob(jobName);
            if (trigger != null) {
                // add next fire time to the list
                jobRuntimes.put(jobName, trigger.getNextFireTime());
            }
        }
        if (StringUtils.isNotBlank(message))
            model.addAttribute(ALERT_SUCCESS, message);

        model.addAttribute("job_name_instances", jobInstances);
        model.addAttribute("job_name_executions", jobExecutions);
        model.addAttribute("job_name_runtimes", jobRuntimes);
        return JOB_INSTANCES;

    }

    protected void jinsNotEmpty(Map<String, JobInstance> jobInstances,
            Map<String, JobExecution> jobExecutions, String jobName,
            List<JobInstance> jins) {
        if (!jins.isEmpty()) {
            // add last instance to the list
            JobInstance jin = jins.get(0);
            jobInstances.put(jobName, jin);
            List<JobExecution> jexs = jobExplorer.getJobExecutions(jin);
            if (!jexs.isEmpty()) {
                // add last execution to the list
                JobExecution jex = jexs.get(0);
                jobExecutions.put(jobName, jex);
            }
        } else {
            // job has no instances, add name only
            jobInstances.put(jobName, null);
        }
    }

    private Trigger getNextTriggerForJob(String jobName) {
        Trigger nextTrigger = null;
        List<Trigger> jobTriggers = null;
        try {
            jobTriggers = getTriggersForJob(jobName);
        } catch (SchedulerException e) {
            if (log.isDebugEnabled()) {
                log.warn("Exception during retrieval of triggers for job: "
                        + jobName, e);
            } else {
                log.warn("Exception during retrieval of triggers for job: "
                        + jobName);
            }
        }
        if (jobTriggers != null) {
            for (Trigger trigger : jobTriggers) {
                if (nextTrigger == null) {
                    nextTrigger = trigger;
                } else if (trigger.getNextFireTime() != null
                        && nextTrigger.getNextFireTime() != null
                        && nextTrigger.getNextFireTime().compareTo(
                                trigger.getNextFireTime()) > 0) {
                    nextTrigger = trigger;
                }
            }
        }
        return nextTrigger;
    }

    private List<Trigger> getTriggersForJob(String jobName)
            throws SchedulerException {
        List<Trigger> triggers = new LinkedList<>();
        for (JobKey jobKey : schedulers.getScheduler().getJobKeys(
                GroupMatcher.jobGroupEquals(jobName))) {
            triggers.addAll(schedulers.getScheduler().getTriggersOfJob(jobKey));
        }
        return triggers;
    }

    @RequestMapping("/jobs/{jobName}/scheduler")
    public String scheduler(Model model, @PathVariable String jobName)
            throws SchedulerException {
        List<Trigger> triggers = getTriggersForJob(jobName);
        Map<TriggerKey, TriggerState> triggerStates = new HashMap<>();
        for (Trigger trigger : triggers) {
            TriggerState triggerState = schedulers.getScheduler()
                    .getTriggerState(trigger.getKey());
            if (triggerState != null) {
                triggerStates.put(trigger.getKey(), triggerState);
            }
        }
        model.addAttribute("job_triggers", triggers);
        model.addAttribute("job_trigger_states", triggerStates);
        return "job_scheduler";
    }

    @RequestMapping("/jobs/{jobName}/scheduler/add")
    public String schedulerTriggerAdd(Model model,
            @PathVariable String jobName, @RequestParam(required = false,
                    defaultValue = "new") String action,
            @Valid @ModelAttribute("job_trigger") CronTriggerImpl trigger,
            BindingResult result, RedirectAttributes redirect)
            throws SchedulerException {
        String forAction = action;
        if (forAction.equals(SAVE) && !result.hasErrors()) {
            String triggerName = UUID.randomUUID().toString();
            JobDetailImpl jobDetail = createJobDetail(jobName);
            trigger.setJobKey(jobDetail.getKey());
            trigger.setName(triggerName);
            trigger.setGroup(jobName);
            if (trigger.getCalendarName() != null
                    && trigger.getCalendarName().isEmpty()) {
                trigger.setCalendarName(null);
            }
            try {
                Date nextRuntime = schedulers.getScheduler().scheduleJob(
                        jobDetail, trigger);
                redirect.addFlashAttribute(ALERT_SUCCESS, TRIGGER_FOR_JOB
                        + jobName + "' scheduled to run first at "
                        + nextRuntime);
                return REDIRECT_JOBS + jobName + SCHEDULER;
            } catch (SchedulerException e) {
                log.debug(e);
                model.addAttribute(ALERT_WARNING, TRIGGER_FOR_JOB + jobName
                        + "' could not be scheduled: " + e.getMessage());
            }
        }
        forAction = "new";
        trigger.setGroup(jobName);
        model.addAttribute("action", forAction);
        model.addAttribute("result", result);
        model.addAttribute("calendar_names", schedulers.getScheduler()
                .getCalendarNames());
        return JOB_SCHEDULER_TRIGGER;
    }

    @RequestMapping("/jobs/{jobName}/scheduler/{triggerName}/delete")
    public String schedulerTriggerDelete(@PathVariable String jobName,
            @PathVariable String triggerName) throws SchedulerException {
        schedulers.getScheduler().unscheduleJob(
                new TriggerKey(triggerName, jobName));
        return REDIRECT_JOBS + jobName + SCHEDULER;
    }

    @RequestMapping("/jobs/{jobName}/scheduler/{triggerName}/disable")
    public String schedulerTriggerDisable(@PathVariable String jobName,
            @PathVariable String triggerName) throws SchedulerException {
        schedulers.getScheduler().pauseTrigger(
                new TriggerKey(triggerName, jobName));
        return REDIRECT_JOBS + jobName + "/scheduler/" + triggerName;
    }

    @RequestMapping("/jobs/{jobName}/scheduler/{triggerName}/edit")
    public String schedulerTriggerEdit(Model model,
            @PathVariable String jobName, @PathVariable String triggerName,
            @RequestParam(required = false, defaultValue = EDIT) String action,
            @Valid @ModelAttribute("job_trigger") CronTriggerImpl trigger,
            BindingResult result, RedirectAttributes redirect)
            throws SchedulerException {
        CronTriggerImpl forTrigger = trigger;
        String forAction = action;
        if (forAction.equals(SAVE) && !result.hasErrors()) {
            JobDetailImpl jobDetail = createJobDetail(jobName);
            forTrigger.setJobKey(jobDetail.getKey());
            forTrigger.setName(triggerName);
            forTrigger.setGroup(jobName);
            if (forTrigger.getCalendarName() != null
                    && forTrigger.getCalendarName().isEmpty()) {
                forTrigger.setCalendarName(null);
            }
            try {
                Date nextRuntime = schedulers.getScheduler().rescheduleJob(
                        forTrigger.getKey(), forTrigger);
                redirect.addFlashAttribute(ALERT_SUCCESS, TRIGGER_FOR_JOB
                        + jobName + "' rescheduled to run first at "
                        + nextRuntime);
                return REDIRECT_JOBS + jobName + SCHEDULER;
            } catch (SchedulerException e) {
                log.debug(e);
                model.addAttribute(ALERT_WARNING, TRIGGER_FOR_JOB + jobName
                        + "' could not be rescheduled: " + e.getMessage());
            }
        }
        if (forAction.equals(EDIT)) {
            forTrigger = (CronTriggerImpl) schedulers.getScheduler()
                    .getTrigger(new TriggerKey(triggerName, jobName));
            model.addAttribute("job_trigger", forTrigger);
        }
        forAction = EDIT;
        model.addAttribute("action", forAction);
        model.addAttribute("result", result);
        model.addAttribute("calendar_names", schedulers.getScheduler()
                .getCalendarNames());
        return JOB_SCHEDULER_TRIGGER;
    }

    @RequestMapping("/jobs/{jobName}/scheduler/{triggerName}/enable")
    public String schedulerTriggerEnable(@PathVariable String jobName,
            @PathVariable String triggerName) throws SchedulerException {
        schedulers.getScheduler().resumeTrigger(
                new TriggerKey(triggerName, jobName));
        return REDIRECT_JOBS + jobName + "/scheduler/" + triggerName;
    }

    @RequestMapping("/jobs/{jobName}/scheduler/{triggerName}")
    public String schedulerTriggerView(Model model,
            @PathVariable String jobName, @PathVariable String triggerName)
            throws SchedulerException {
        TriggerKey key = new TriggerKey(triggerName, jobName);
        Trigger trigger = schedulers.getScheduler().getTrigger(key);
        TriggerState triggerState = schedulers.getScheduler().getTriggerState(
                key);
        model.addAttribute("job_trigger", trigger);
        model.addAttribute("job_trigger_state", triggerState);
        return JOB_SCHEDULER_TRIGGER;
    }

    @RequestMapping("/jobs/{jobName}/start")
    public String startJob(RedirectAttributes redirect,
            @PathVariable String jobName, @RequestParam(required = false,
                    defaultValue = "false") String forceNew)
            throws SchedulerException {
        try {
            String uuid = UUID.randomUUID().toString();
            boolean forceNewBool = Boolean.parseBoolean(forceNew);
            JobDetailImpl jobDetail;
            jobDetail = createJobDetail(jobName, forceNewBool);
            if (JOBNAME.equals(jobName)) {
                List<JobInstance> jins = jobExplorer.getJobInstances(jobName,
                        0, 1);
                JobInstance jin = jins.get(0);
                List<JobExecution> jexs = jobExplorer.getJobExecutions(jin);
                if (!jexs.isEmpty()) {
                    JobExecution jex = jexs.get(0);
                    if (jex != null) {
                        if (jex.getStatus() == BatchStatus.STARTED) {
                            redirect.addFlashAttribute(ALERT_WARNING, JOB
                                    + jobName + "' still running");
                            return REDIRECT_JOBS2;
                        }
                    }
                }
            }
            // start in 5 seconds
            long startTime = System.currentTimeMillis() + 5 * 1000L;
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(uuid, jobName).startAt(new Date(startTime))
                    .build();
            schedulers.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.debug(e);
            redirect.addFlashAttribute(ALERT_WARNING, TRIGGER_FOR_JOB + jobName
                    + "' could not be scheduled: " + e.getMessage());
        }

        return REDIRECT_JOBS2;
    }

    @ResponseBody
    @RequestMapping("/jobs/{jobName}/start/files")
    public String startJobFiles(
            @PathVariable String jobName,
            @RequestParam(required = false, defaultValue = "false") String forceNew,
            @RequestParam(required = false, defaultValue = "") String path,
            @RequestParam(value = "selectedFiles[]", required = false) String selectedFiles)
            throws SchedulerException {
        try {
            JobDetailImpl jobDetail;
            if (selectedFiles != null && !selectedFiles.isEmpty()) {
                jobDetail = createJobDetail(jobName, true, selectedFiles, path);
            } else {
                return "/jobList/"
                        + jobName
                        + "/view?message=You have to selected files to start job";
            }
            // start in 5 seconds
            long startTime = System.currentTimeMillis() + 5 * 1000L;
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(UUID.randomUUID().toString(), jobName)
                    .startAt(new Date(startTime)).build();
            schedulers.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.debug(e);
            return "/jobList/" + jobName + "/view?message=Trigger for job '"
                    + jobName + "' could not be started: " + e.getMessage();
        }

        return "/jobs";
    }

    @RequestMapping("/jobs/{jobName}/{instanceId}/{executionId}/stop")
    public String stopJobExecution(RedirectAttributes redirect,
            @PathVariable String jobName, @PathVariable long executionId) {
        try {
            jobOperator.stop(executionId);
            redirect.addFlashAttribute(ALERT_SUCCESS, JOB + jobName
                    + "' successfully stopped");
        } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
            log.debug(e);
            redirect.addFlashAttribute(ALERT_WARNING, JOB + jobName
                    + "' could not be stopped");
        }
        return REDIRECT_JOBS2;
    }
}
