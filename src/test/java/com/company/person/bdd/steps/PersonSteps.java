package com.company.person.bdd.steps;

import com.company.person.bdd.config.VirtualizedTestContext;
import com.company.person.bdd.mq.PersonMessageConsumer;
import com.company.person.bdd.mq.PersonMessageProducer;
import com.company.person.client.PersonRestClient;
import com.company.person.model.Person;
import com.company.person.repository.PersonRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.skyscreamer.jsonassert.JSONAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PersonSteps {

    private final PersonRepository repository =
            new PersonRepository(VirtualizedTestContext.dataSource());

    private final PersonRestClient restClient =
            new PersonRestClient(VirtualizedTestContext.restBaseUrl());

    private String actualJsonResponse;
    private String actualXmlResponse;

    @Given("Delete all persons from DB in Jersey flow")
    public void deleteAllPersonsFromDbInJerseyFlow() throws Exception {
        repository.deleteAll();
    }

    @Given("Database has no persons for jersey flow")
    public void databaseHasNoPersonsForJerseyFlow() throws Exception {
        repository.deleteAll();
    }

    @Given("User inserted a person information for jersey flow")
    public void userInsertedPersonInformationForJerseyFlow(DataTable dataTable) throws Exception {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            Person person = new Person(
                    row.get("FirstName").trim(),
                    row.get("LastName").trim(),
                    row.get("Profession").trim(),
                    Integer.parseInt(row.get("LocationX").trim()),
                    Integer.parseInt(row.get("LocationY").trim()),
                    row.get("CompanyOrg").trim(),
                    row.get("CompanyHeadQuarters").trim()
            );

            repository.insert(person);
        }
    }

    @Then("Check if there are {string} users in the DB for jersey flow")
    public void checkIfThereAreUsersInDbForJerseyFlow(String expectedCount) throws Exception {
        int actualCount = repository.count();
        assertEquals(Integer.parseInt(expectedCount), actualCount);
    }

    @Given("JSON for User {string} and {string} is retrieved")
    public void jsonForUserIsRetrieved(String firstName, String lastName) throws Exception {
        actualJsonResponse = restClient.getPersonJson(firstName.trim(), lastName.trim());
    }

    @Then("Validate if the JSON response is matching with {string}")
    public void validateIfJsonResponseIsMatchingWith(String expectedJsonFileName) throws Exception {
        String expectedJson = readExpectedFile(expectedJsonFileName);
        JSONAssert.assertEquals(expectedJson, actualJsonResponse, false);
    }

    @Given("XML for User {string} and {string} is retrieved")
    public void xmlForUserIsRetrieved(String firstName, String lastName) throws Exception {
        actualXmlResponse = restClient.getPersonXml(firstName.trim(), lastName.trim());
    }

    @Then("Validate if the XML response is matching with {string}")
    public void validateIfXmlResponseIsMatchingWith(String expectedXmlFileName) throws Exception {
        String expectedXml = readExpectedFile(expectedXmlFileName);

        Diff diff = DiffBuilder.compare(expectedXml)
                .withTest(actualXmlResponse)
                .ignoreWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), () -> "XML mismatch: " + diff);
    }

    @Given("Message queue is available")
    public void messageQueueIsAvailable() {
        assertNotNull(VirtualizedTestContext.activeMqBrokerUrl());
        assertNotNull(VirtualizedTestContext.personQueueName());
    }

    @When("Person message {string} is published")
    public void personMessageIsPublished(String message) throws Exception {
        PersonMessageProducer producer = new PersonMessageProducer(
                VirtualizedTestContext.activeMqBrokerUrl(),
                VirtualizedTestContext.personQueueName()
        );

        producer.publish(message);
    }

    @Then("Person message {string} should be consumed")
    public void personMessageShouldBeConsumed(String expectedMessage) throws Exception {
        PersonMessageConsumer consumer = new PersonMessageConsumer(
                VirtualizedTestContext.activeMqBrokerUrl(),
                VirtualizedTestContext.personQueueName()
        );

        String actualMessage = consumer.consume(5000);

        assertEquals(expectedMessage, actualMessage);
    }

    private String readExpectedFile(String fileName) throws Exception {
        return Files.readString(Path.of("src/test/resources/expected", fileName));
    }
}
