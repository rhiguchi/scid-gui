package jp.scid.gui.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModels;
import jp.scid.gui.model.ValueProxy;

public class ValueController<T> {
    private final ValueProxy<T> modelHolder;

    protected ValueController(MutableValueModel<T> model) {
        modelHolder = new ValueProxy<T>(model);
    }
    
    public static <T> ValueController<T> create(MutableValueModel<T> model) {
        return new ValueController<T>(model);
    }
    
    public static <T> ValueController<T> create(T initialValue) {
        return new ValueController<T>(ValueModels.newValueModel(initialValue));
    }
    
    public T getValue() {
        return modelHolder.get();
    }
    
    public void setValue(T newValue) {
        modelHolder.set(newValue);
    }
    
    public MutableValueModel<T> getModel() {
        return modelHolder.getSubject();
    }

    public void setModel(MutableValueModel<T> newSubject) {
        modelHolder.setSubject(newSubject);
    }
    
    public void bindFormattedTextField(JFormattedTextField field, Class<? extends T> valueClass) {
        // model to field
        new FormattedTextValueConnector<T>(field).setModel(modelHolder);
        // value of field to model
        new FormattedTextFieldValueChangeHandler(valueClass).installTo(field);
    }
    
    private class FormattedTextFieldValueChangeHandler implements PropertyChangeListener {
        private final Class<? extends T> valueClass;
        
        public FormattedTextFieldValueChangeHandler(Class<? extends T> valueClass) {
            this.valueClass = valueClass;
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            T newValue = valueClass.cast(e.getNewValue());
            setValue(newValue);
        }
        
        public void installTo(JFormattedTextField field) {
            field.addPropertyChangeListener("value", this);
        }
    }
}