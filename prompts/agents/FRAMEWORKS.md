# Frameworks & Core Libraries

## Mission
Adhere to the project's standard stack to ensure maintainability and consistency.

## Standard Stack & Implementation Skills
For "How-to" guides and code examples, activate the corresponding skill:

- **Dependency Injection**: Use **Toothpick**.
    - Skill: `framework-toothpick`
- **MVP (Model-View-Presenter)**: Use **Moxy**.
    - Skill: `framework-moxy`
- **Networking**: Use **Retrofit** and **Moshi**.
    - Skill: `network-retrofit`
- **UI (Android TV/Leanback)**: Use **View Binding** and **Glide**.
    - Skill: `ui-leanback`
- **Architecture**: Follow the **Repository Pattern**.
    - Skill: `architecture-core`
- **Testing**: Use **MockK** (no annotations) and **JUnit 4**.
    - Skill: `testing-mockk`

## Framework Constraints
- **Moxy**: NEVER use `@InjectPresenter`. Use the `moxyPresenter` delegate.
- **Retrofit**: NEVER use `.enqueue()`. Use Coroutines.
- **UI**: NEVER use `MotionLayout`. Use XML animations.
- **DI**: Prefer `@InjectConstructor` over manual injection.
