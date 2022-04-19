[![Release](https://jitpack.io/v/jitpack/android-example.svg)](https://jitpack.io/#rommansabbir/TraceX)

# TraceX

⚠️ Error Tracing is FUN! ⚠️

---

## Documentation

### Installation

Step 1. Add the JitPack repository to your build file .

```gradle
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```

Step 2. Add the dependency.

```gradle
    dependencies {
            implementation 'com.github.rommansabbir:TraceX:Tag'
    }
```

---

### Version available

| Releases
| ------------- |
| 1.0.0         |


# Usages

## Why TraceX?

`TraceX` is designed to monitor all **Uncaught Exception** occurs in app lifecycle by default. Also, client can disable auto monitoring **Uncaught Exception** by following the `TraceXConfig` and manually register an `Activity` for monitoring by calling `TraceX.registerActivity` API.

`TraceX` will automatically write a log to the app cache directory if the **Uncaught Exception** is an instance of `RuntimeException` based on `Config`. Client can also write a new log the the app cache directory by calling `TraceX.writeANewLog` API.

Also, client can get all logs written by `TraceX` or client itself by calling `TraceX.writeANewLog` API.

Client can also remove a list of managed or unmanaged logs from the cache directory that is written by `TraceX`. Or, simply remove all logs from cache directory written by `TraceX`.

**Motto of this library:**

As a developer we don't know on which device or on which constraint system will throw `Exception` or `RuntimeException` if the application is in **PRODUCTION**. If any **Uncaught Exception** occurs during app lifecycle we get to know about it via others **Logging library**.

But, as a developer you might want to know navigate the user to a specific page (eg. home page) when an fatal exception occurs which eventually kill the application process in the device. Before that page navigation, we can write a log to the app cache directory by following the **current Device Info, Current Thread, Throwable that occurred and a JSON object as additional info**. Or simply, we can write our own log to the cache directory at our own.

So that, we can get the list written logs from the cache directory when user run the application again, we can process the logs, like **SEND IT TO THE REMOTE SERVER** for bug fixing, analytics or simply ignore or remove the log from the cache directory.

**NOTE: Writing or Reading logs from cache directory follows Encryption/Decryption process by using StoreX.**

----

## How to initialize and Access?:
Initialize `TraceX` from your `Application.onCreate()`
````
    TraceXProvider.register(TraceXConfig(this,
        autoRegisterForEachActivity = true,
        autoLogRuntimeExceptions = true
    ))
````
To Access `TraceX` call **`TraceXProvider.INSTANCE`** which return an instance of **`TraceX`**

## Public APIs:

- `registerListener(listener: TraceXCallback?)`, To register or unregister listener

- `registerActivity(activity: Activity?): Boolean`, Manually register an actvity for Uncaught Error Event Hanlding and return `Boolean`. Also, `TraceXConfig.autoRegisterForEachActivity` should be `false`

- `writeANewLog(throwable: Throwable, additionalInfo: String = ""): Boolean`, To write a new encrypted log to the app cache directory and return `Boolean`.

- `getRecentCrashLogs(): MutableList<TraceXCrashLog>`, To get list of recent logs from app cache directory. **[Note: It's a CPU Intensive process, execute the operation with Coroutine/RxJava.]**

- `clearCrashLogs(list: MutableList<TraceXCrashLog>)`, To remove a given list of **`TraceXCrashLog`** from the app cache directory. **[Note: It's a CPU Intensive process, execute the operation with Coroutine/RxJava.]**

- `clearAllLogs(): Boolean`, To remove all logs from the app cache directory written by **`TraceX` **[Note: It's a CPU Intensive process, execute the operation with Coroutine/RxJava.]**
----

### **Remeber: All public APIs will throw `TraceXNotInitializedException` if **`TraceX`** is not initialized before accessing it's **`APIs`**.**

----

## `Checkout the sample app for the implementaion in detail`
----
### Happy Coding....

---

### Contact me

[LinkedIn](https://www.linkedin.com/in/rommansabbir/) | [Blog](https://rommansabbir.com/)

---

### License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

````html
Copyright (C) 2022 Romman Sabbir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````
