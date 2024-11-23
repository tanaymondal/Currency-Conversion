* This is a Currency Conversion project using `Compose Multi Platform` targeting Android, iOS with MVVM
  architecture.
* This projects is made for `PayPay` Mobile Coding Challenge

* Below components are used to achieve the functionality:
  - `Datastore` : For storing the timestamp of last API call. If timestamp difference is more
    than 30 minutes, we again fetch from API, else we fetch from database.
  - `Room Databse` : For storing list of currencies once we get from API.
  - `Koin` : For dependency injection.
  - `Ktor` : For API calling.

* © Tanay Mondal

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…