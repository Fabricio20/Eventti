package net.notfab.eventti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<Listener> listeners = new CopyOnWriteArraySet<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void onDisable() {
        this.listeners.clear();
    }

    /* ------------------------------------------------ */

    /**
     * Registers a listener for incoming events.
     *
     * @param listener The listener to add.
     */
    public void addListener(Listener listener) {
        if (listener == null) return;
        this.listeners.add(listener);
    }

    /**
     * Removes a previously registered listener.
     *
     * @param listener The listener to remove.
     */
    public void remListener(Listener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Asynchronously fires an event, this will trigger all registered listeners
     * that have methods annotated with {@link EventHandler} that
     * contain a single parameter of the same type as the event
     * parameter, respecting their priority.
     *
     * @param event The event to fire.
     */
    public void fire(Event event) {
        this.executorService.submit(() -> fireSync(event));
    }

    /**
     * Fires an event, this will trigger all registered listeners
     * that have methods annotated with {@link EventHandler} that
     * contain a single parameter of the same type as the event
     * parameter, respecting their priority.
     *
     * @param event The event to fire.
     */
    public void fireSync(Event event) {
        Set<EventTuple> tuples = new TreeSet<>();
        for (Listener listener : this.listeners) {
            Class clazz = listener.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(EventHandler.class)) continue;
                if (method.getParameterCount() != 1) continue;
                if (!event.getClass().isAssignableFrom(method.getParameterTypes()[0])) continue;

                EventHandler eventHandler = method.getDeclaredAnnotation(EventHandler.class);
                tuples.add(new EventTuple(method, eventHandler, listener));
            }
        }
        for (EventTuple tuple : tuples) {
            Method method = tuple.getMethod();
            EventHandler handler = tuple.getHandler();
            Listener listener = tuple.getListener();
            if (event instanceof Cancellable
                    && handler.ignoreCancelled()
                    && ((Cancellable) event).isCancelled()) {
                continue;
            }
            try {
                method.invoke(listener, event);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                logger.error("Error firing event", ex);
            }
        }
    }

}