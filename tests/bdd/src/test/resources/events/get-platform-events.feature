Feature: Get platform events

  Background:
    Given an authenticated user

  Scenario: get the events of a newly created platform
    Given an existing platform
    When I get the events of this platform
    Then 1 event is returned
    And event at index 0 is a PlatformCreatedEvent event type

  Scenario: create a new platform, change its version and read its events
    Given an existing platform
    And I update this platform, changing the platform version
    When I get the events of this platform
    Then 2 events are returned
    And event at index 0 is a PlatformUpdatedEvent event type
    And event at index 1 is a PlatformCreatedEvent event type

  Scenario: get the events of a platform that doesn't exist
    Given a platform that doesn't exist
    When I get the events of this platform
    Then 0 event is returned
