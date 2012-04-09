package jp.scid.gui.model;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


abstract public class AbstractValueTransformer<S, T> extends AbstractValueModel<T> implements ChangeListener {

    ValueModel<S> subject;
    
    T value;
    
    void set(T value) {
        this.value = value;
    }
    
    public ValueModel<S> getSubject() {
        return subject;
    }
    
    public void setSubject(ValueModel<S> newSubject) {
        if (this.subject != null) {
            this.subject.removeChangeListener(this);
        }
        
        this.subject = newSubject;
        
        if (newSubject != null) {
            newSubject.addChangeListener(this);
        }
        
        updateWithSubject();
    }

    void listenTo(ValueModel<?> model) {
        model.addChangeListener(this);
    }
    
    void deafTo(ValueModel<?> model) {
        model.removeChangeListener(this);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        updateWithSubject();
    }
    
    void updateWithSubject() {
        ValueModel<S> s = getSubject();
        if (s != null) {
            S sVal = s.get();
            T newValue = convertFromSubject(sVal);
            set(newValue);
        }
    }
    
    abstract protected T convertFromSubject(S subjectValue);
}
