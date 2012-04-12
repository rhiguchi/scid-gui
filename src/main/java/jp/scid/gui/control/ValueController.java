package jp.scid.gui.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModel;
import jp.scid.gui.model.ValueModels;

public class ValueController<T> implements PropertyChangeListener {
    protected MutableValueModel<T> model;

    public ValueController(MutableValueModel<T> model) {
        if (model == null) {
            model = createModel();
        }

        this.model = model;
    }
    
    protected MutableValueModel<T> createModel() {
        return ValueModels.newValueModel();
    }
    
    public MutableValueModel<T> getModel() {
        return model;
    }
    
    public void setModel(MutableValueModel<T> model) {
        this.model = model;
    }

    public void setValue(T newValue) {
        model.set(newValue);
    }

    public T getValue() {
        return model.get();
    }
    
    public void bindFormattedTextFieldValue(JFormattedTextField field) {
        ComponentConnecter.connect(field, getModel());
        field.addPropertyChangeListener("value", this);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final T newValue;
        
        if (evt.getSource() instanceof JFormattedTextField) {
            JFormattedTextField field = (JFormattedTextField) evt.getSource();
            newValue = (T) field.getValue();
        }
        else {
            return;
        }
        
        setValue(newValue);
    }
    
    public class Connection {
        final ComponentConnecter connecter;
        
        public Connection(ComponentConnecter connecter) {
            this.connecter = connecter;
        }

        public void dispose() {
            connecter.getComponent().removePropertyChangeListener("value", ValueController.this);
            connecter.setModel(null);
        }
    }
    
    public static class ComponentConnecter implements ChangeListener {
        final JFormattedTextField component;
        
        protected ValueModel<?> model;
        
        public ComponentConnecter(JFormattedTextField component) {
            this.component = component;
        }
        
        public static ComponentConnecter connect(JFormattedTextField component, ValueModel<?> model) {
            ComponentConnecter con = new ComponentConnecter(component);
            con.setModel(model);
            return con;
        }
        
        public JFormattedTextField getComponent() {
            return component;
        }
        
        public ValueModel<?> getModel() {
            return model;
        }
        
        public void setModel(ValueModel<?> newModel) {
            this.model = newModel;
            
            if (newModel != null) {
                updateValue(getComponent(), newModel.get());
            }
        }
        
        protected void updateValue(JFormattedTextField component, Object value) {
            component.setValue(value);
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            updateValue(getComponent(), getModel().get());
        }
    }
}
