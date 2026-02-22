---
name: java-apply-backlog-item
description: Apply exactly one item from audit/refactor-backlog.md as a small patch, then verify with Gradle.
allowed-tools: Read, Edit, Write, Glob, Grep, Bash
---
When invoked:
1) Ask the user to specify the backlog item ID/title OR pick the top "Quick win" item if none is provided.
2) Implement only that item (keep scope tight).
3) Run: ./gradlew test
4) If tests exist and pass, optionally run: ./gradlew check
5) Output a short changelog and what was verified.