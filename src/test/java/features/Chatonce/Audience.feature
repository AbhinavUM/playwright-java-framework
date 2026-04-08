Feature: Audience Rule Management

  Scenario: Create and delete audience rule
    Given I am on login page
    When I login with email "audience_rule@staticso2.com" and password "qwerty@123"
    Then I should land on home page
    Then I should see Bots icon on the left outer navigation
    When I click on Bots icon
    Then I should see the Bots page
    And I open Audience section
    And I create audience "contact status"
    And I add condition city is "Delhi"
    Then I should see audience "contact status"

    When I delete audience "contact status"
    Then audience should be removed