package org.mock.behavior;
public class ReturnBehavior<T> implements BehaviorRule {
    private final T value;

    public ReturnBehavior(T value) {
        this.value = value;
    }

    @Override
    public Object execute() {
        return value;
    }
}