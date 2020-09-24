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
Configs.init();
boolean b =  Config.getBoolean("Key1"); // true
String str =  Config.getString("Key2"); // "ABC"
Integer number =  Config.getInteger("Key3"); // 42
String json =  Config.getString("jsonKey.SubKey"); // "VALUE"
```

You can also init a specific file :

```java
Configs.init("path/filename.conf");
```

## Maven
### Repository
File: <i>pom.xml</i>
```Xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
### Dependency
File: <i>pom.xml</i>
```Xml
<dependency>
    <groupId>com.github.d-william</groupId>
    <artifactId>commons-config</artifactId>
    <version>2.0.1</version>
</dependency>
```
