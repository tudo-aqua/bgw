---
title: Finalize Release {{ tools.context.ref }}
---

The GitHub Actions workflow just pushed a draft release to Sonatype OSS and closed the repository. No irrevocable
changes have been made. Please review the staging repository at https://oss.sonatype.org/#stagingRepositories and

- drop the repository and re-run the workflow if the release is defective, or
- release the repository to create a Maven Central publication.