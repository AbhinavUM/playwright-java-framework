Feature: Login

  Scenario: Successful login
    Given I am on login page
    When I login with email "abhinav4376@gmail.com" and password "Qwerty@123"
    Then I should land on home page