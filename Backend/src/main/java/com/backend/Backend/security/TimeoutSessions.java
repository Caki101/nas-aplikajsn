package com.backend.Backend.security;

import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeoutSessions {
    static Map<String, Integer> sessions =  new HashMap<>();
    static Map<String, Timestamp> timeout = new HashMap<>();

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void timeoutMapItems() {
        // timeout after certain amount of time anyway
    }

    public static void addSession(String ip) {
        sessions.put(ip, 1);
    }

    public static void removeSession(String ip) {
        sessions.remove(ip);
    }

    public static boolean containsSession(String ip) {
        return sessions.containsKey(ip);
    }

    public static Integer getTries(String ip) {
        return sessions.get(ip);
    }

    public static void addTry(String ip) {
        sessions.put(ip, sessions.get(ip) + 1);
    }

    public static void addTimeout(String ip) {
        timeout.put(ip, new Timestamp(System.currentTimeMillis()));
    }

    public static void removeTimeout(String ip) {
        timeout.remove(ip);
    }

    public static boolean isTimedOut(String ip) {
        return timeout.containsKey(ip);
    }

    public static Timestamp getTimeout(String ip) {
        return timeout.get(ip);
    }
}
