package utils;


import reporting.TestStep;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TestContextUtil {
    private static final ThreadLocal<Map<String, Object>> testContext = ThreadLocal.withInitial(() -> {
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("testSteps", new ArrayList<TestStep>());
        map.put("stepCounter", new AtomicInteger(1));
        return map;
    });

    public static Map<String, Object> getTestContext() {
        return testContext.get();
    }

    public static List<TestStep> getCurrentTestSteps() {
        return (List<TestStep>) testContext.get().get("testSteps");
    }

    public static AtomicInteger getStepCounter() {
        return (AtomicInteger) testContext.get().get("stepCounter");
    }
}
