# Eventti ![Build Status](https://github.com/Fabricio20/Eventti/actions/workflows/build.yml/badge.svg)
**Eventti** is a simple Java event framework which allows users to fire events and have listeners act upon them.

**Features**:
<ul>
<li>Async event firing (by default!)</li>
<li>Cancellable events</li>
<li>Event listener priority</li>
<li>Thread Safe</li>
</ul>

### Installation

**Note**: Eventti uses SLF4J as a logging framework.

Maven:

```xml

<dependency>
    <groupId>io.github.fabricio20</groupId>
    <artifactId>eventti</artifactId>
    <version>1.4.0</version>
</dependency>
```
Gradle:
```bash
implementation 'io.github.fabricio20:eventti:1.4.0'
```

### Usage

**1** - An Event can be any class with any signature, feel free to add any methods or parameters you may need.

Normal event:
```java
public class ExampleEvent {}
```

Cancellable event:
```java
public class ExampleEvent implements Cancellable {
    
    private boolean cancelled = false;
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
}
```

**2** - Create a listener which handles your event.

```java
public class ExampleListener implements Listener {
    
    @EventHandler
    public void onExampleEvent(ExampleEvent exampleEvent) {
        // Do something
    }
    
    @EventHandler(priority = ListenerPriority.HIGH)
    public void onExampleEventHigh(ExampleEvent exampleEvent) {
        // This executes before onExampleEvent
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onExampleEventHigh(ExampleEvent exampleEvent) {
        // Assuming ExampleEvent is cancellable, this method is not
        // called if the event was previously cancelled by another
        // listener.
    }
    
}
```

**3** -  Create an EventManager instance for your project and add a listener.

```java
public class Example {
    
    private final EventManager eventManager = new EventManager();
    
    public Example() {
        eventManager.addListener(new ExampleListener());
    }
    
}
```

**4** - Fire events.

```java
public class Example {
    
    private final EventManager eventManager = new EventManager();
    
    public Example() {
        eventManager.addListener(new ExampleListener());
    }
    
    public void fireSomeEvents() {
        eventManager.fire(new ExampleEvent()); // Async
        eventManager.fireSync(new ExampleEvent()); // Sync (Blocking)
    }
    
}
```

### Contributors

- Fabricio20 [Maintainer]

### License
This project is licensed under the MIT License, for more information, please check the LICENSE file.
