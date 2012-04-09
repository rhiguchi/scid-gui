package jp.scid.gui.model;

public class ValueHolder<T> extends AbstractMutableValueModel<T> {
    
    public ValueHolder() {
    }
    
    public T apply() {
        return get();
    }
    
    public void $colon$eq(T newValue) {
        set(newValue);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SimpleValueModel [value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
}