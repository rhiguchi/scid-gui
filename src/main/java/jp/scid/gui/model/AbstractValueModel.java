package jp.scid.gui.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class AbstractValueModel<T> implements ValueModel<T> {
    private List<ChangeListener> listeners = new LinkedList<ChangeListener>();
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireStateChange() {
        if (listeners.isEmpty())
            return;
        
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener l: listeners) {
            l.stateChanged(event);
        }
    }
    
    protected void fireValueChange(T oldValue, T newValue) {
        if (oldValue != null) {
            if (!oldValue.equals(newValue)) {
                fireStateChange();
            }
        }
        else if (newValue != null) {
            fireStateChange();
        }
    }
}

abstract class AbstractMutableValueModel<T> extends AbstractValueModel<T> implements MutableValueModel<T> {
    T value;
    
    @Override
    public T get() {
        return value;
    }
    
    @Override
    public void set(T newValue) {
        fireValueChange(this.value, this.value = newValue);
    }
}