Feature: Validate person information using local virtualized integrations

  @Regression
  Scenario: Check if person details are deleted in DB for jersey flow
    Given Delete all persons from DB in Jersey flow
    Then Check if there are "0" users in the DB for jersey flow

  @Regression
  Scenario: Insert and count records for jersey flow
    Given Database has no persons for jersey flow
    And User inserted a person information for jersey flow
      |FirstName|LastName|Profession|LocationX|LocationY|CompanyOrg      |CompanyHeadQuarters|
      |Rudra     |Thakur   |IT        |100      |150      |Cognizant USA   |Akron          |
      |Raj      |Kumar   |Auto      |200      |250      |Cognizant London|London             |
    Then Check if there are "2" users in the DB for jersey flow

  @JulyRelease
  Scenario Outline: Validate the person records in XML format
    Given XML for User "<FirstName>" and "<LastName>" is retrieved
    Then Validate if the XML response is matching with "<ExpectedXMLFileName>"

    Examples:
      |FirstName|LastName|ExpectedXMLFileName|
      |Rudra     |Thakur   |Response1.xml       |
      |Raj      |Kumar   |Response2.xml       |

  @JulyRelease
  Scenario Outline: Validate the person records in JSON format
    Given JSON for User "<FirstName>" and "<LastName>" is retrieved
    Then Validate if the JSON response is matching with "<ExpectedJSONFileName>"

    Examples:
      |FirstName|LastName|ExpectedJSONFileName|
      |Rudra     |Thakur   |Response1.json       |
      |Raj      |Kumar   |Response2.json       |

  @Messaging
  Scenario: Publish and consume person message using virtualized ActiveMQ
    Given Message queue is available
    When Person message "Rudra Thakur created" is published
    Then Person message "Rudra Thakur created" should be consumed

  @Mongo
  Scenario: Insert and count records using virtualized MongoDB
    Given Mongo collection has no persons
    And User inserted person information into Mongo collection
      |FirstName|LastName|Profession|LocationX|LocationY|CompanyOrg      |CompanyHeadQuarters|
      |Rudra    |Thakur  |IT        |100      |150      |Cognizant USA   |Akron              |
      |Raj      |Kumar   |Auto      |200      |250      |Cognizant London|London             |
    Then Check if there are "2" users in the Mongo collection

  @Mongo
  Scenario: Find company organization by person name using virtualized MongoDB
    Given Mongo collection has no persons
    And User inserted person information into Mongo collection
      |FirstName|LastName|Profession|LocationX|LocationY|CompanyOrg      |CompanyHeadQuarters|
      |Raj      |Kumar   |Auto      |200      |250      |Cognizant London|London             |
    Then Company organization for Mongo user "Raj" and "Kumar" should be "Cognizant London"
