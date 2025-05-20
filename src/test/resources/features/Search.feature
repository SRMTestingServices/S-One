Feature: Google Search

  @TSCID098767
  Scenario: Verify Google Title
    Given I open the browser and navigate to "https://www.google.com"
    Then the page title should be "Google"

  @ApiTest
  Scenario: Validate successful GET request
    Given I send a GET request to "/users/1" on base URI "https://jsonplaceholder.typicode.com"
    Then the response status code should be 200

  @ApiTest
  Scenario: Validate successful POST request
    Given I send a POST request to "/api/users" on base URI "https://reqres.in" with JSON body
    Then the POST response status code should be 201
    And the response should contain field "id"
