package jp.scid.gui.model;

import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;

public class DataSourceEventList<E> extends TransformedList<E, E> {

    private DataSourceModel<E> dataSource;
    
    public DataSourceEventList(EventList<E> eventDelegate) {
        super(eventDelegate);
        
        eventDelegate.addListEventListener(this);
        
        dataSource = DataSourceModel.EMPTY;
    }
    
    public DataSourceEventList() {
        this(new BasicEventList<E>());
    }

    public DataSourceModel<E> getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(DataSourceModel<E> dataSource) {
        this.dataSource = dataSource;
        
        fetch();
    }
    
    public void fetch() {
        List<E> elements = dataSource.getElements();
        source.clear();
        source.addAll(elements);
    }
    
    @Override
    protected boolean isWritable() {
        return true;
    }

    @Override
    public void listChanged(ListEvent<E> listChanges) {
        updates.forwardEvent(listChanges);
    }

    @Override
    public void add(int index, E element) {
        dataSource.addElement(element);
        
        super.add(index, element);
    }
    
    @Override
    public E remove(int index) {
        E removing = get(index);
        
        dataSource.removeElement(removing);
        
        return super.remove(index);
    }

    public void update(E element) {
        dataSource.elementChange(element);
    }
}
