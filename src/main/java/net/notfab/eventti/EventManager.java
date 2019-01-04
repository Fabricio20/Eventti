package net.notfab.eventti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class EventManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<Listener> listeners = new HashSet<>();

    public void onDisable() {
        this.listeners.clear();
    }

    /* ------------------------------------------------ */

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void remListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void callEvent(Event event) {
        Set<Object[]> priority_HIGHEST = new HashSet<>();
        Set<Object[]> priority_HIGH = new HashSet<>();
        Set<Object[]> priority_NORMAL = new HashSet<>();
        Set<Object[]> priority_LOW = new HashSet<>();
        Set<Object[]> priority_LOWEST = new HashSet<>();
        for (Listener listener : this.listeners) {
            Class clazz = listener.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(EventHandler.class)) continue;
                if (method.getParameterCount() != 1) continue;
                if (Event.class.isAssignableFrom(method.getParameterTypes()[0])) continue;

                EventHandler eventHandler = method.getDeclaredAnnotation(EventHandler.class);
                if (event instanceof Cancellable) {
                    if (eventHandler.ignoreCancelled()) continue;
                }
                Object[] tuple = new Object[]{method, eventHandler, listener};
                switch (eventHandler.priority()) {
                    case LOWEST:
                        priority_LOWEST.add(tuple);
                    case LOW:
                        priority_LOW.add(tuple);
                    case NORMAL:
                        priority_NORMAL.add(tuple);
                    case HIGH:
                        priority_HIGH.add(tuple);
                    case HIGHEST:
                        priority_HIGHEST.add(tuple);
                    default:
                        throw new IllegalArgumentException("Invalid listener priority");
                }
            }
        }
        priority_HIGHEST.forEach(tuple -> callEvent(event, tuple));
        priority_HIGH.forEach(tuple -> callEvent(event, tuple));
        priority_NORMAL.forEach(tuple -> callEvent(event, tuple));
        priority_LOW.forEach(tuple -> callEvent(event, tuple));
        priority_LOWEST.forEach(tuple -> callEvent(event, tuple));
    }

    private void callEvent(Event event, Object[] tuple) {
        try {
            Method method = (Method) tuple[0];
            EventHandler eventHandler = (EventHandler) tuple[1];
            Listener listener = (Listener) tuple[2];
            if (event instanceof Cancellable) {
                if (eventHandler.ignoreCancelled() && ((Cancellable) event).isCancelled()) return;
            }
            method.invoke(listener, event);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            logger.error("Error firing event", ex);
        }
    }

}
