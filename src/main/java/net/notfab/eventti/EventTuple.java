package net.notfab.eventti;

import java.lang.reflect.Method;

class EventTuple implements Comparable {

    private Method method;
    private EventHandler handler;
    private Listener listener;

    EventTuple(Method method, EventHandler handler, Listener listener) {
        this.method = method;
        this.handler = handler;
        this.listener = listener;
    }

    Method getMethod() {
        return this.method;
    }

    EventHandler getHandler() {
        return this.handler;
    }

    Listener getListener() {
        return this.listener;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof EventTuple)) return -1;
        EventTuple eventTuple = (EventTuple) o;
        return Integer.compare(eventTuple.getHandler().priority()
                .getWeight(), this.getHandler().priority().getWeight());
    }

}