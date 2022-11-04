package io.github.fabricio20.eventti;

import java.lang.reflect.Method;

class EventTuple implements Comparable<EventTuple> {

    private final Method method;
    private final EventHandler handler;
    private final Listener listener;

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
    public int compareTo(EventTuple tuple) {
        return Integer.compare(tuple.getHandler().priority().getWeight(), this.getHandler().priority().getWeight());
    }

}
