package jp.scid.gui.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class AbstractValueModel<T> implements ValueModel<T> {
    private List<ChangeListener> listeners = new LinkedList<ChangeListener>();
    
    @Override
    public void addValueChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeValueChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    void fireValueChange(T oldValue, T newValue) {
        if (listeners.isEmpty())
            return;
        
        if (oldValue == null) {
            if (newValue == null) {
                return;
            }
        }
        else if (oldValue.equals(newValue)) {
            return;
        }
        
        ChangeEvent event = new ValueChangeEvent(this, oldValue, newValue);
        fireStateChange(event);
    }

    protected void fireStateChange(ChangeEvent event) {
        for (ChangeListener l: listeners) {
            l.stateChanged(event);
        }
    }
    
    static class ValueChangeEvent extends ChangeEvent {
        private final Object oldValue;
        private final Object newValue;
        
        public ValueChangeEvent(Object source, Object oldValue, Object newValue) {
            super(source);
            
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
        
        public Object getOldValue() {
            return oldValue;
        }
        
        public Object getNewValue() {
            return newValue;
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