package org.pandaframework.ecs.component;

/**
 * @author Ranie Jade Ramiso
 */
public class ComponentPoolException extends Throwable {
    public ComponentPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentPoolException(Throwable cause) {
        super(cause);
    }
}
