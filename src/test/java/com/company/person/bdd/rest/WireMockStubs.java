package com.company.person.bdd.rest;

import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class WireMockStubs {

    private WireMockStubs() {
    }

    public static void configureForLocalServer(int port) {
        WireMock.configureFor("localhost", port);
    }

    public static void stubPersonJson() {
        stubFor(get(urlPathEqualTo("/persons/json/Rudra/Thakur"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "firstName": "Rudra",
                              "lastName": "Thakur",
                              "profession": "IT",
                              "companyOrg": "Cognizant USA",
                              "companyHeadQuarters": "Akron"
                            }
                            """)));

        stubFor(get(urlPathEqualTo("/persons/json/Raj/Kumar"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "firstName": "Raj",
                              "lastName": "Kumar",
                              "profession": "Auto",
                              "companyOrg": "Cognizant London",
                              "companyHeadQuarters": "London"
                            }
                            """)));
    }

    public static void stubPersonXml() {
        stubFor(get(urlPathEqualTo("/persons/xml/Rudra/Thakur"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/xml")
                        .withBody("""
                            <person>
                                <firstName>Rudra</firstName>
                                <lastName>Thakur</lastName>
                                <profession>IT</profession>
                                <companyOrg>Cognizant USA</companyOrg>
                                <companyHeadQuarters>Akron</companyHeadQuarters>
                            </person>
                            """)));

        stubFor(get(urlPathEqualTo("/persons/xml/Raj/Kumar"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/xml")
                        .withBody("""
                            <person>
                                <firstName>Raj</firstName>
                                <lastName>Kumar</lastName>
                                <profession>Auto</profession>
                                <companyOrg>Cognizant London</companyOrg>
                                <companyHeadQuarters>London</companyHeadQuarters>
                            </person>
                            """)));
    }
}
