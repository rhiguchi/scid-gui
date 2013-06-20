package jp.scid.gui.control;

import java.awt.Component;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import jp.scid.gui.model.ValueModel;

public class ComponentPropertyConnector<C extends Component, V> extends ViewValueConnector<C, V> {
    private final String propertyName;
    
    public ComponentPropertyConnector(C targetView, String propertyName) {
        super(targetView);
        if (propertyName == null)
            throw new IllegalArgumentException("propertyName must not be null");
        
        this.propertyName = propertyName;
    }

    public static <C extends Component, V> ComponentPropertyConnector<C, V> connect(
            ValueModel<V> model, C component, String propertyName) {
        ComponentPropertyConnector<C, V> conn = new ComponentPropertyConnector<C, V>(component, propertyName);
        conn.setModel(model);
        return conn;
    }
    
    @Override
    protected void updateView(final C target, final V modelValue) {
        Runnable updater = new Runnable() {
            public void run() {
                AbstractController.execute(
                        target, AbstractController.getSetterName(propertyName), modelValue);
            }
        };
        
        if (EventQueue.isDispatchThread()) {
            updater.run();
        }
        else try {
            EventQueue.invokeAndWait(updater);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}