package jp.scid.gui.control;

import javax.swing.JFormattedTextField;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModels;
import jp.scid.gui.model.ValueProxy;
import jp.scid.gui.model.connector.BeanPropertyConnector;

public class ValueController<T> {
    private final ValueProxy<T> modelHolder;

    private final Class<? extends T> valueClass;
    
    protected ValueController(MutableValueModel<T> model, Class<? extends T> valueClass) {
        modelHolder = new ValueProxy<T>(model);
        this.valueClass = valueClass;
    }
    
    protected ValueController(Class<? extends T> valueClass) {
        this(ValueModels.<T>newNullableValueModel(), valueClass);
    }
    
    public static <T> ValueController<T> create(MutableValueModel<T> model, Class<? extends T> valueClass) {
        return new ValueController<T>(model, valueClass);
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
    
    public void bindFormattedTextField(JFormattedTextField field) {
        // model to field
        new FormattedTextValueConnector<T>(field).setModel(modelHolder);
        // value of field to model
        BeanPropertyConnector.create(modelHolder, "value", valueClass).setSource(field);
    }
}