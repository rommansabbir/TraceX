[![Release](https://jitpack.io/v/jitpack/android-example.svg)](https://jitpack.io/#rommansabbir/TraceX)

# TraceX

Error Tracing is FUN!

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

## How to initialize?:
Initialize `TraceX` from your `Application.onCreate()`
````
    TraceXProvider.register(TraceXConfig(this,
        autoRegisterForEachActivity = true,
        autoLogRuntimeExceptions = true
    ))
````

### Happy Coding....

---

### Contact me

[LinkedIn](https://www.linkedin.com/in/rommansabbir/)

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
