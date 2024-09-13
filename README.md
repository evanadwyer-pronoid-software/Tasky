# Tasky
Tasky is a lite version of Google Calendar that allows users to create, view, edit, and delete events, tasks, and reminders. It works as an offline-first app and allows for collaboratation across users by inviting them to events.

Made as part of Philipp Lackner's [10 week mentorhsip program](https://pl-coding.com/drop-table-mentoring/)

## Build Status
[![Build Status](https://app.bitrise.io/app/9d0e7ce1-df31-499b-9a23-7bcfb7c7d688/status.svg?token=qDqFmwjMOhEluPAexsnASw)](https://app.bitrise.io/app/9d0e7ce1-df31-499b-9a23-7bcfb7c7d688)
[![CodeQL](https://github.com/evanadwyer-pronoid-software/Tasky/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/evanadwyer-pronoid-software/Tasky/actions/workflows/github-code-scanning/codeql)
[![ktlint](https://img.shields.io/badge/ktlint%20code--style-%E2%9D%A4-FF4081)](https://pinterest.github.io/ktlint/)

![Photo mode export (5)](https://github.com/user-attachments/assets/3aa25407-7dbf-4a72-90b2-4bbc12a83726)
![Photo mode export (4)](https://github.com/user-attachments/assets/06c6973d-1c6c-4d98-8136-36ac660200d2)


## Gradle Module Dependency Graph
This graph represents the general dependencies between modules of the app.
[![image](https://github.com/evanadwyer-pronoid-software/Tasky/assets/170752499/b2f0fcf2-7230-4e71-a3fa-732f7ded67d4)](https://miro.com/app/board/uXjVKAumJ6s=/)

## Branch Naming Conventions
Branches will be named in the following format:

[MAJOR_RELEASE].[WEEK_NUMBER].[BRANCH_NUMBER]_[TOPIC]
* Major Release -> stable version of app (will be 0 until end of MVP)
* Week Number -> The week of the mentorship program when the branch is created, with weeks starting on Fridays
* Branch Number -> The number of that branch for the week
* Topic -> Features, layers, gradle, bug fix, DB, etc.

## Chosen Technologies and Libraries
This implementation of Tasky will make use of the following technologies and libraries:
* Kotlin
* Jetpack Compose
* Ktor
* Room DB
* Kotlin Coroutines and Flows
* Splashscreen API
* Material3
* Coil
* Kotlin Serialization
* JUnit4 and JUnit5
* WorkManager
* AlarmManager
* NotificationManager
* Hilt
* KSP
* Timber
* Ktlint
* kotlinx datetime

## Contributing
When cloning this project, please run `./gradlew addKtlintFormatGitPreCommitHook` so that KtLint can automatically format your commits :)
