package jp.scid.gui.control;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.scid.gui.model.ValueModel;

public abstract class ValueChangeHandler<T> {
    private final ChangeListener modelChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            modelChange();
        }
    };
    
    private ValueModel<T> model;
    
    abstract protected void valueChanged(T newValue);
    
    public void setModel(ValueModel<T> newModel) {
        if (model != null)
            uninstallChangeListener(model);
        
        model = newModel;
        
        if (newModel != null) {
            modelChange();
            installChangeListener(newModel);
        }
    }
    
    void modelChange() {
        T value = model.get();
        valueChanged(value);
    }

    void installChangeListener(ValueModel<T> model) {
        model.addValueChangeListener(modelChangeListener);
    }
    
    void uninstallChangeListener(ValueModel<T> model) {
        model.removeValueChangeListener(modelChangeListener);
    }
}
