package com.company.person.bdd.config;

import com.company.person.bdd.db.TestDatabaseInitializer;
import com.company.person.bdd.mq.EmbeddedActiveMqConfig;
import com.company.person.bdd.rest.WireMockStubs;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class VirtualizedTestContext {

    private static boolean started = false;

    private static DataSource dataSource;
    private static WireMockServer wireMockServer;
    private static EmbeddedActiveMqConfig activeMqConfig;

    private VirtualizedTestContext() {
    }

    public static synchronized void start() throws Exception {
        if (started) {
            return;
        }

        startDatabase();
        startRestVirtualization();
        startMessageQueue();

        started = true;
    }

    private static void startDatabase() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();

        // Change MODE to MySQL, PostgreSQL, MSSQLServer, DB2, Derby, HSQLDB, etc. as needed.
        ds.setURL("jdbc:h2:mem:persondb;MODE=Oracle;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");

        dataSource = ds;

        TestDatabaseInitializer.runScript(dataSource, "db/schema.sql");
    }

    private static void startRestVirtualization() {
        wireMockServer = new WireMockServer(options().port(9090));
        wireMockServer.start();

        WireMockStubs.configureForLocalServer(9090);
        WireMockStubs.stubPersonJson();
        WireMockStubs.stubPersonXml();
    }

    private static void startMessageQueue() throws Exception {
        activeMqConfig = new EmbeddedActiveMqConfig();
        activeMqConfig.start();
    }

    public static synchronized void stop() throws Exception {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }

        if (activeMqConfig != null) {
            activeMqConfig.stop();
        }

        started = false;
    }

    public static DataSource dataSource() {
        return dataSource;
    }

    public static String restBaseUrl() {
        return "http://localhost:9090";
    }

    public static String activeMqBrokerUrl() {
        return EmbeddedActiveMqConfig.BROKER_URL;
    }

    public static String personQueueName() {
        return "person.events";
    }
}
