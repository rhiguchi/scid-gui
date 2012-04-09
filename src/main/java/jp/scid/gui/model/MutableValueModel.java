package jp.scid.gui.model;

public interface MutableValueModel<T> extends ValueModel<T> {
    void set(T newValue);
}
