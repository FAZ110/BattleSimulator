package org.personal.engine;

public class ConsoleLogger implements GameObserver {
    @Override
    public void onEvent(String message) {
        System.out.println(" > " + message);
    }
}
