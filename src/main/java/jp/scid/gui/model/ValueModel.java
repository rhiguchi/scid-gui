package jp.scid.gui.model;

import javax.swing.event.ChangeListener;

public interface ValueModel<T> {
    T get();
    
    void addChangeListener(ChangeListener listener);
    
    void removeChangeListener(ChangeListener listener);
}
