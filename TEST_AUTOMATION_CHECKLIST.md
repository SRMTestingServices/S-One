# ✅ Test Automation Standards & Checklist

This checklist defines the mandatory practices and coding guidelines for all team members contributing to the BDD automation framework. Following these ensures high-quality, maintainable, and scalable automation code.

---

## 🔹 Test-Specific Guidelines

| ID   | Description |
|------|-------------|
| T01  | All tests must be environment-agnostic. If any test cannot meet this, it must be reviewed and documented in the PR with the Automation Core Team. |
| T02  | `try-catch` blocks should not suppress failures that must fail the test. |
| T03  | Screenshots should be captured on failure and optionally on key validation steps to support evidence. |
| T04  | No changes to base class files unless explicitly approved by the Automation Core Team. |
| T05  | Do not use a `Scenario Outline` for a single example. Convert it to a regular `Scenario`. |
| T06  | Step definitions should be modular and manageable in size. Split when complexity increases. |
| T07  | No false positives—ensure tests fail when they should. |
| T08  | Test data should be reviewed and confirmed by the Automation Team before being added to the framework. |
| T09  | Only `THEN` steps should contain assertions. `GIVEN` and `WHEN` steps should only set context or perform actions. |
| T10  | Use hard or soft assertions depending on the purpose of the test. |
| T11  | Step definitions should be designed for reusability across multiple scenarios. |
| T12  | Test cases should include meaningful tags like `@Smoke`, `@Regression`, `@TSCID` for filtering and CI execution. |
| T13  | Avoid duplication of test logic across scenarios. Refactor and reuse shared steps. |

---

## 🧩 Page Object Model (POM) Guidelines

| ID   | Description |
|------|-------------|
| P01  | All POMs should follow the existing structure and naming conventions. |
| P02  | Basic element interaction methods should not contain conditional logic. |
| P03  | Each element should have its own method for interaction. |
| P04  | Assertions or validation logic should not be placed in POMs. |
| P05  | Method names should follow the pattern `action + element type`, e.g., `clickLoginButton()`. |
| P06  | Locators should follow the naming pattern `what it is + element type`, e.g., `usernameInput`, `submitButton`. |
| P07  | Each page class should include a meaningful JavaDoc or comment describing its purpose and scope. |
| P08  | Each POM should have a consistent way to verify the page has loaded (e.g., `isLoaded()` method). |

---

## 🔧 Code-Level Guidelines

| ID   | Description |
|------|-------------|
| C01  | Follow formatter rules or use tools like Prettier / Checkstyle where applicable. |
| C02  | Avoid commented or unused code unless it is documented with a valid reason. |
| C03  | Avoid using `System.out.println`; use a proper logging framework instead. |
| C04  | Avoid unnecessary `Thread.sleep()`; use explicit or fluent waits instead. |
| C05  | Method names should clearly describe their purpose or action. |
| C06  | Avoid methods with more than 5 or 6 parameters. Use data objects if necessary. |
| C07  | Maintain consistency in file structure with similar existing files. |
| C08  | Place reusable methods and functions in shared utility classes. |
| C09  | Ensure logging, reporting, and error messages are clear and helpful for debugging. |
| C10  | Follow consistent and agreed-upon naming conventions across the codebase. |
| C11  | Ensure the code is unit testable and peer reviewed before merging. |
| C12  | Avoid hardcoded values unless strictly necessary for the test. Use configuration files or constants. |
| C13  | Do not use abbreviations that aren't universally understood. |
| C14  | Do not use deprecated code or APIs in new implementations. |
| C15  | Use `final` for constants and avoid magic strings/values. |
| C16  | N/A (C17 was likely meant to be C16 and can be removed or updated). |

---

## 🔁 Pull Request (PR) Standards

| ID   | Description |
|------|-------------|
| PR01 | Do not include driver files, logs, or test output files in the PR. |
| PR02 | Ensure the test suite runs successfully on a local environment before raising the PR. |
| PR03 | Reset defaults like config values, environment variables, or test tags before raising the PR. |
| PR04 | Branches should be created from the `main` or `development` branch as per the team workflow. |
| PR05 | Resolve any merge conflicts before creating the PR. |
| PR06 | Include evidence of successful local and sanity suite runs (e.g., screenshots or logs). |
| PR07 | Every PR must be peer-reviewed and approved by at least one member of the Automation Core Team. |

---

## 📌 Notes

- This checklist should evolve based on team feedback and technology updates.
- Violations of these standards should be flagged during code review.
- Encourage discussions in case of exceptions or special test needs.

