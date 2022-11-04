package io.github.fabricio20.eventti;

public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();

}
