# Testing Guidelines (MockK Only)

## Mission
Ensure high reliability and regression safety through standardized, decoupled unit tests using MockK and JUnit 4.

## Core Rules
- **MockK Mandatory**: Use MockK for all mocking. No other libraries (e.g., Mockito).
- **NO Annotations**: Do not use `@MockK` or `@RelaxedMockK`. Initialize mocks explicitly via `mockk(relaxed = true)` as class properties.
- **Tear Down**: Always call `clearAllMocks()` and `Toothpick.reset()` in `@After`.
- **Standard JUnit 4**: Use standard JUnit 4 for tests that do not require dependency injection (constructor injection).

## DI in Tests (InjectingTest)
- **Requirement**: Extend `us.nineworlds.serenity.test.InjectingTest` ONLY if the class under test uses field-level injection.
- **Setup**: Override `setUp` and call `super.setUp()` as the first action.
- **Implementation Guide**: See `testing-mockk` skill for implementation examples and patterns.
