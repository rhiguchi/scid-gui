package jp.scid.gui.control;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.scid.gui.model.BeanModelFactory;
import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModel;

public abstract class ComponentController<C extends Component> implements PropertyChangeListener {
    private final C component;
    private final Map<String, PropertyModelChangeHandler> propertyModelMap;
    
    BeanModelFactory modelFactory;
    
    protected ComponentController(C component) {
        this.component = component;
        propertyModelMap = new HashMap<String, PropertyModelChangeHandler>();
    }
    
    public static <C extends Component> ComponentController<C> newController(C component) {
        DefaultComponentController<C> ctrl = new DefaultComponentController<C>(component);
        return ctrl;
    }
    
    public void listenToComponentPropertyChange() {
        component.addPropertyChangeListener(this);
    }
    
    public  void deafToComponentPropertyChange() {
        component.removePropertyChangeListener(this);
    }
    
    public C getComponent() {
        return component;
    }
    
    public void setPropertyModel(String propertyName, ValueModel<?> newModel) {
        if (propertyName == null)
            throw new IllegalArgumentException("componentProperty must not be null");
        
        PropertyModelChangeHandler oldHandler = propertyModelMap.remove(propertyName);
        if (oldHandler != null) {
            oldHandler.release();
        }
        
        if (newModel != null) {
            PropertyModelChangeHandler handler = new PropertyModelChangeHandler(propertyName, newModel);
            oldHandler = propertyModelMap.put(propertyName, handler);
            handler.activate();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        PropertyModelChangeHandler handler = propertyModelMap.get(propertyName);
        if (handler == null)
            return;
        
        Object value = AbstractController.getBeanPropertyValue(component, propertyName);
        handler.setModelValue(value);
    }
    
    void updateProperty(String propertyName, Object newValue) {
        AbstractController.execute(component, AbstractController.getSetterName(propertyName), newValue);
    }
    
    static class DefaultComponentController<C extends Component> extends ComponentController<C> {
        public DefaultComponentController(C component) {
            super(component);
        }
    }
    
    class PropertyModelChangeHandler implements ChangeListener {
        private final String propertyName;
        private final ValueModel<?> model;
        
        public PropertyModelChangeHandler(String propertyName, ValueModel<?> model) {
            this.propertyName = propertyName;
            this.model = model;
        }
        
        public void activate() {
            updateProperty(propertyName, model.get());
            model.addValueChangeListener(this);
        }
        
        public void release() {
            model.removeValueChangeListener(this);
        }
        
        @SuppressWarnings("unchecked")
        public void setModelValue(Object newValue) {
            ((MutableValueModel<Object>) model).set(newValue);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            ValueModel<?> model = (ValueModel<?>) e.getSource();
            updateProperty(propertyName, model.get());
        }
    }
}
