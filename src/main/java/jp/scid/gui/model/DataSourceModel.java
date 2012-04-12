package jp.scid.gui.model;

import java.util.Collections;
import java.util.List;

public interface DataSourceModel<E> {
    public static DataSourceModel EMPTY = new DataSourceModel() {

        @Override
        public List getElements() {
            return Collections.emptyList();
        }

        @Override
        public boolean addElement(Object element) {
            return false;
        }

        @Override
        public boolean elementChange(Object element) {
            return false;
        }

        @Override
        public boolean removeElement(Object element) {
            return false;
        }
    };
    
    List<E> getElements();
    
    public boolean addElement(E element);
    
    public boolean elementChange(E element);
    
    public boolean removeElement(E element);
}
