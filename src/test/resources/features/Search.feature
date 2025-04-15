Feature: Google Search

  @TSCID098767
  Scenario: Verify Google Title
    Given I open the browser and navigate to "https://www.google.com"
    Then the page title should be "Google"
