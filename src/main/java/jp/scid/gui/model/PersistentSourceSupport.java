package jp.scid.gui.model;

import java.util.List;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

abstract public class PersistentSourceSupport<E> {
    private final EventList<E> delegate;

    public PersistentSourceSupport(EventList<E> delegate) {
        this.delegate = delegate;
    }

    public void fetch() {
        List<E> elements = retrieveAll();
        GlazedLists.replaceAll(delegate, elements, true);
    }

    protected abstract List<E> retrieveAll();

    // insert
    abstract protected boolean insertToTable(E element);

    // Update
    abstract protected void updateToTable(E element);

    // Remove
    abstract protected boolean deleteFromTable(E element);
}
