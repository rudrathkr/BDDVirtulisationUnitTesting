package com.company.person.bdd.hooks;

import com.company.person.bdd.config.VirtualizedTestContext;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

public class BddHooks {

    @BeforeAll
    public static void beforeAll() throws Exception {
        VirtualizedTestContext.start();
    }

    @AfterAll
    public static void afterAll() throws Exception {
        VirtualizedTestContext.stop();
    }
}
