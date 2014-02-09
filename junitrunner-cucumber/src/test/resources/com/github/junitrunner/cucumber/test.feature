Feature: Test

  Background: 
    Given background

  Scenario: Simple
    Given step 1
    Then step 2

  Scenario Outline: outline
    Given there are <start> cucumbers
    When I eat <eat> cucumbers
    Then I should have <left> cucumbers

    Examples: 
      | start | eat | left |
      | 12    | 5   | 7    |
      | 20    | 5   | 15   |
