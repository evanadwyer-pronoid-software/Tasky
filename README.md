# Tasky

Tasky is a lite version of Google Calendar that allows users to create, view, edit, and delete events, tasks, and reminders. It works as an offline-first app and allows for collaboratation across users by inviting them to events.

## Build Status

[![Build Status](https://app.bitrise.io/app/9d0e7ce1-df31-499b-9a23-7bcfb7c7d688/status.svg?token=qDqFmwjMOhEluPAexsnASw&branch=master)](https://app.bitrise.io/app/9d0e7ce1-df31-499b-9a23-7bcfb7c7d688)

## Gradle Module Dependency Graph

This graph represents the general dependencies between modules of the app.

## Branch Naming Conventions

Branches will be named in the following format:

[MAJOR_RELEASE].[WEEK_NUMBER].[BRANCH_NUMBER]-[TOPIC]
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
* Hilt
* KSP
* Timber
* Ktlint