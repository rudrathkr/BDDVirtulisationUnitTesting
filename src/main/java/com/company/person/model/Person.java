package com.company.person.model;

public record Person(
        String firstName,
        String lastName,
        String profession,
        int locationX,
        int locationY,
        String companyOrg,
        String companyHeadquarters
) {
}
