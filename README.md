# InfinityConfig
JSON to configuration

## Usage

application.conf :

```json
{
  "Key1": true,
  "Key2": "ABC",
  "Key3": 42,
  "jsonKey": {
    "SubKey": "VALUE"
  }
}
```
Java :

```java
Config.init();
String b =  Config.get("Key1"); // "true"
String str =  Config.get("Key2"); // "ABC"
String number =  Config.get("Key3"); // "42"
String json =  Config.get("jsonKey.SubKey"); // "VALUE"
```

You can also init a specific file :

```java
Config.init("path/filename.conf");
```

## Download

[commons-config-1.0.0.jar](https://github.com/d-william/InfinityConfig/releases/download/1.0.0/commons-config-1.0.0.jar)
