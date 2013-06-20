package jp.scid.gui.model.connector;

import static java.lang.String.*;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.Statement;
import java.lang.reflect.InvocationTargetException;

import jp.scid.gui.model.MutableValueModel;

public class BeanPropertyConnector<T, S> extends ValueConnector<T, S> {
    private final PropertyChangeListener changeListener = new PropertyChangeListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (propertyName.equals(e.getPropertyName())) {
                sourceChange((S) e.getSource());
            }
        }
    };
    
    private final String propertyName;
    private final Class<? extends T> valueClass;
    
    public BeanPropertyConnector(MutableValueModel<T> target, String propertyName, Class<? extends T> propertyClass) {
        super(target);
        if (propertyName == null) throw new IllegalArgumentException("propertyName must not be null");
        if (propertyClass == null) throw new IllegalArgumentException("valueClass must not be null");
        
        this.propertyName = propertyName;
        this.valueClass = propertyClass;
    }

    public static <T, S> BeanPropertyConnector<T, S> create(
            MutableValueModel<T> target, String propertyName, Class<? extends T> propertyClass) {
        BeanPropertyConnector<T, S> c = new BeanPropertyConnector<T, S>(target, propertyName, propertyClass);
        return c;
    }
    
    @Override
    protected T getModelValue(S source) {
        try {
            PropertyDescriptor nameProp = new PropertyDescriptor(propertyName, source.getClass());
            Object value = nameProp.getReadMethod().invoke(source);
            return valueClass.cast(value);
        }
        catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void installUpdateListener(S source) {
        execute(source, "addPropertyChangeListener", changeListener);
    }

    @Override
    protected void uninstallUpdateListener(S source) {
        execute(source, "removePropertyChangeListener", changeListener);
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