package jp.scid.gui.control;

import javax.swing.JTable;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;

public class ListController<E> {
    final EventList<E> source;

    final EventSelectionModel<E> selectionModel;

    public ListController(EventList<E> list) {
        this.source = list;

        selectionModel = new EventSelectionModel<E>(list);
    }

    public ListController() {
        this(new BasicEventList<E>());
    }

    public EventList<E> getSource() {
        return source;
    }

    public EventSelectionModel<E> getSelectionModel() {
        return selectionModel;
    }

    public void bindTable(JTable table, TableFormat<E> format) {
        EventTableModel<E> tableModel = createTableModel(format);

        table.setModel(tableModel);
    }

    protected EventTableModel<E> createTableModel(TableFormat<E> format) {
        EventTableModel<E> tableModel = new EventTableModel<E>(getSource(), format);
        return tableModel;
    }

    public void add() {
        int index = selectionModel.getMaxSelectionIndex() + 1;

        add(index);
    }

    public void add(int index) {
        E newElement = createElement();

        source.add(index, newElement);
    }

    protected E createElement() {
        throw new UnsupportedOperationException("must implement to create element");
    }
}
