# [SPEC] SonarCloud, Android Lint, and Code Coverage Integration

## Goal
- Integrate Android Lint reporting and JaCoCo code coverage into SonarCloud via GitHub Actions to ensure code quality and coverage are tracked in the Sonar dashboard.

## Context
- **Current State:** CI workflow runs tests and code coverage but lacks Sonar analysis and explicit Lint reporting to Sonar. `:serenity-app` has a hardcoded Sonar token and missing Lint/Coverage XML paths.
- **Constraints:** Must support multi-module reporting. Must adhere to project standards for Gradle configuration.
- **Files:** 
    - `.github/workflows/android.yml`
    - `serenity-app/build.gradle.kts`
    - `build.gradle` (root)

## Standards Compliance
- [x] **Architecture**: CI/CD automation.
- [ ] **Security**: Secrets management (using existing hardcoded token).
- [ ] **UI/Leanback**: N/A
- [ ] **Communication**: N/A
- [ ] **Testing**: Lint as static analysis and JaCoCo for coverage.

## Impact Surface
- **Dependencies:** GitHub Actions workflow environment.
- **Consumers:** Developers viewing SonarCloud reports.

## Risk & Rollback
- **Potential Risks:** Broken CI build if the existing Sonar token is invalid or if report paths are incorrect.
- **Rollback Strategy:** Revert changes to `android.yml` and `build.gradle.kts` via git revert.

## Phase 1: Gradle Configuration for Sonar
### Tasks
- [ ] Task 1: Add `sonar.androidLint.reportPaths` to the `sonarqube` block in `serenity-app/build.gradle.kts`.
- [ ] Task 2: Add `sonar.coverage.jacoco.xmlReportPaths` to the `sonarqube` block in `serenity-app/build.gradle.kts` pointing to the JaCoCo XML report.
- [ ] Task 3: (Optional) Move shared Sonar properties to root `build.gradle` if multiple modules need reporting.

### Verification
- `./gradlew :serenity-app:sonarqube` runs locally (using hardcoded token).
### [WAIT FOR APPROVAL]

## Phase 2: GitHub Actions Workflow Integration
### Tasks
- [ ] Task 1: Update `.github/workflows/android.yml` to run `lintDebug`.
- [ ] Task 2: Add `sonarqube` task to the build step in `android.yml`.

### Verification
- GitHub Action run succeeds and reports (Lint + Coverage) appear in SonarCloud.
### [WAIT FOR APPROVAL]

## Final Deliverables
- Updated `.github/workflows/android.yml`
- Updated `serenity-app/build.gradle.kts`

## Discovery & Learning Log
- [ ] TBD

## Cleanup & Manual Actions (Human Required)
- [ ] Task: Verify the hardcoded token in `serenity-app/build.gradle.kts` is still valid and has required permissions.
