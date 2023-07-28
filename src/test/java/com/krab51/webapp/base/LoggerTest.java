package com.krab51.webapp.base;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.AssertionFailureBuilder.assertionFailure;

public abstract class LoggerTest<T> {
    protected ListAppender<ILoggingEvent> logWatcher;
    private Class<T> type;
    private int idx;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setup() {
        type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(type)).addAppender(logWatcher);
    }

    @AfterEach
    void teardown() {
        ((Logger) LoggerFactory.getLogger(type)).detachAndStopAllAppenders();
        idx = 0;
    }

//    protected String getNextLogMessage() {
//        String result = logWatcher.list.get(idx).getFormattedMessage();
//        idx++;
//        return result;
//    }

    protected void assertLogEquals(Level level, String msg) {
        ILoggingEvent event = logWatcher.list.get(idx);
        idx++;

        if (!(level == event.getLevel() && msg.equals(event.getFormattedMessage()))) {
            assertionFailure()
                    .expected("Level: " + level.levelStr + ", Msg: " + msg)
                    .actual("Level: " + event.getLevel() + ", Msg: " + event.getFormattedMessage())
                    .buildAndThrow();
        }
    }
}