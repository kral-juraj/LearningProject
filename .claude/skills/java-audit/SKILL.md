---
name: java-audit
description: Run a read-only security + SOLID/architecture audit and produce reports under /audit.
allowed-tools: Read, Glob, Grep
---
When invoked:
1) Use the java-audit-lead approach.
2) Scan repository structure (multi-module if present).
3) Produce:
    - audit/security-report.md
    - audit/design-review.md
    - audit/refactor-backlog.md
4) In backlog, group items:
    - Quick wins (low risk)
    - Medium refactors
    - High-risk refactors (require tests/spec)
5) Always reference concrete files/classes/methods.