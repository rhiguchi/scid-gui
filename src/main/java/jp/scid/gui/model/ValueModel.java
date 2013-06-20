package jp.scid.gui.model;

import javax.swing.event.ChangeListener;

public interface ValueModel<T> {
    T get();
    
    void addValueChangeListener(ChangeListener listener);
    
    void removeValueChangeListener(ChangeListener listener);
}
