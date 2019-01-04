package net.notfab.eventti;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class EventManagerTest {

    @Test
    public void addListenerShouldIgnoreNullParameter() throws Exception {
        EventManager eventManager = new EventManager();
        assert getListeners(eventManager).size() == 0;
        eventManager.addListener(null);
        assert getListeners(eventManager).size() == 0;
    }

    @Test
    public void addListenerShouldWork() throws Exception {
        EventManager eventManager = new EventManager();
        assert getListeners(eventManager).size() == 0;
        eventManager.addListener(new Listener() {});
        assert getListeners(eventManager).size() == 1;
    }

    @Test
    public void remListenerShouldIgnoreNullParameter() throws Exception {
        EventManager eventManager = new EventManager();
        assert getListeners(eventManager).size() == 0;
        eventManager.remListener(null);
    }

    @Test
    public void remListenerShouldWork() throws Exception {
        EventManager eventManager = new EventManager();
        Listener listener = new Listener() {};
        assert getListeners(eventManager).size() == 0;
        eventManager.addListener(listener);
        assert getListeners(eventManager).size() == 1;
        eventManager.remListener(listener);
        assert getListeners(eventManager).size() == 0;
    }

    @Test
    public void eventFiringShouldWork() {
        EventManager eventManager = new EventManager();
        AtomicBoolean wasFired = new AtomicBoolean(false);

        Event event = new ExampleEvent();
        Listener listener = new ExampleListener(() -> wasFired.set(true), () -> {});

        eventManager.addListener(listener);
        eventManager.fireSync(event);
        assert wasFired.get();
    }

    @Test
    public void eventFiringShouldRespectPriority() {
        EventManager eventManager = new EventManager();
        AtomicLong normalFire = new AtomicLong();
        AtomicLong highFire = new AtomicLong();

        Event event = new ExampleEvent();
        Listener listener = new ExampleListener(
                () -> normalFire.set(System.nanoTime()),
                () -> highFire.set(System.nanoTime()));

        eventManager.addListener(listener);
        eventManager.fireSync(event);
        assert highFire.get() < normalFire.get();
    }

    @SuppressWarnings("unchecked")
    private Set<Listener> getListeners(EventManager eventManager) throws IllegalAccessException, NoSuchFieldException {
        Field field = eventManager.getClass().getDeclaredField("listeners");
        field.setAccessible(true);
        return (Set<Listener>) field.get(eventManager);
    }

}