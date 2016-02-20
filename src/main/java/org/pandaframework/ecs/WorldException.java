package org.pandaframework.ecs;

/**
 * @author Ranie Jade Ramiso
 */
public class WorldException extends Throwable {
    public WorldException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorldException(Throwable cause) {
        super(cause);
    }
}
