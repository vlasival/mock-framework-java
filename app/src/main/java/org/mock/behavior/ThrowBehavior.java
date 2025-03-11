package org.mock.behavior;
public class ThrowBehavior implements BehaviorRule {
    private final Throwable exception;

    public ThrowBehavior(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Object execute() throws Throwable {
        throw exception;
    }
}