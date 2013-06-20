package jp.scid.gui.model;

import static java.lang.String.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;

import ca.odell.glazedlists.impl.beans.BeanProperty;

public class BeanPropertyAdapter extends ValueModelAdapter<Object, Object> implements MutableValueModel<Object>, PropertyChangeListener {
    private final BeanProperty<Object> property;
    
    public BeanPropertyAdapter(Object bean, String propertyName, boolean readable, boolean writable) {
        super(bean);
        
        @SuppressWarnings("unchecked")
        Class<Object> beanClass = (Class<Object>) bean.getClass();
        this.property = new BeanProperty<Object>(beanClass, propertyName, readable, writable);
    }
    
    public <B> BeanPropertyAdapter(B bean, String propertyName) {
        this(bean, propertyName, true, true);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        fireValueChange(evt.getOldValue(), evt.getNewValue());
    }

    @Override
    protected void installUpdateListener(Object newSubject) {
        execute(newSubject, "addPropertyChangeListener", this);
    }

    @Override
    protected void uninstallUpdateListener(Object newSubject) {
        execute(newSubject, "removePropertyChangeListener", this);
    }

    @Override
    protected Object getValueFromSubject(Object subject) {
        return property.get(subject);
    }

    @Override
    protected void updateSubject(Object subject, Object newValue) {
        property.set(subject, newValue);
    }

    static void execute(Object target, String methodName, Object... arguments) throws IllegalStateException {
        Statement statement = new Statement(target, methodName, arguments);
        try {
            statement.execute();
        }
        catch (Exception e) {
            throw new IllegalStateException(format(
                    "Cannot execute %s on an object for %s", methodName, target.getClass()), e);
        }
    }
}