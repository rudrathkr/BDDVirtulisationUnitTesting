package com.company.person.repository;

import com.company.person.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonRepository {

    private final DataSource dataSource;

    public PersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteAll() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().executeUpdate("DELETE FROM PERSON");
        }
    }

    public void insert(Person person) throws Exception {
        String sql = """
            INSERT INTO PERSON
            (FIRST_NAME, LAST_NAME, PROFESSION, LOCATION_X, LOCATION_Y, COMPANY_ORG, COMPANY_HQ)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, person.firstName());
            statement.setString(2, person.lastName());
            statement.setString(3, person.profession());
            statement.setInt(4, person.locationX());
            statement.setInt(5, person.locationY());
            statement.setString(6, person.companyOrg());
            statement.setString(7, person.companyHeadquarters());

            statement.executeUpdate();
        }
    }

    public int count() throws Exception {
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection
                     .createStatement()
                     .executeQuery("SELECT COUNT(*) FROM PERSON")) {

            resultSet.next();
            return resultSet.getInt(1);
        }
    }
}
