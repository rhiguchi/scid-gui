package jp.scid.gui.model;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ValueProxy<T> extends AbstractMutableValueModel<T> implements ChangeListener {
    MutableValueModel<T> subject;
    
    public ValueProxy(MutableValueModel<T> subject) {
        if (subject == null)
            throw new IllegalArgumentException("subject must not be null");
        
        subject.addChangeListener(this);
    }

    @Override
    public void set(T newValue) {
        super.set(newValue);
        
        subject.set(newValue);
        
        fireStateChange();
    }
    
    T getSubjectValue() {
        return getSubject().get();
    }
    
    public MutableValueModel<T> getSubject() {
        return subject;
    }
    
    public void setSubject(MutableValueModel<T> newSubject) {
        if (newSubject == null)
            throw new IllegalArgumentException("newSubject must not be null");
        
        deafTo(this.subject);
        
        this.subject = newSubject;
        
        listenTo(newSubject);
        
        subjectValueChange();
    }
    

    void listenTo(ValueModel<?> model) {
        model.addChangeListener(this);
    }
    
    void deafTo(ValueModel<?> model) {
        model.removeChangeListener(this);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        subjectValueChange();
    }
    
    public void subjectValueChange() {
        T subjectValue = getSubjectValue();
        
        fireValueChange(this.value, this.value = subjectValue);
    }
}
