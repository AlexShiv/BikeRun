package ru.bacca.bikerun.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduleService {

    private static final Logger LOGGER = LogManager.getLogger(ScheduleService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void testSchedule() {
        LOGGER.info("The time is now {}", dateFormat.format(new Date()));
    }
}
