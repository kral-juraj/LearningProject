---
name: java-verify
description: Run Gradle verification tasks and summarize failures.
allowed-tools: Bash
---
When invoked:
1) Run:
    - ./gradlew test
    - ./gradlew check
2) If check fails due to missing plugins/tasks, report what tasks exist:
    - ./gradlew tasks
3) Summarize failing tests, modules, and the first actionable error per failure.