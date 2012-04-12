package jp.scid.gui.control;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModel;
import jp.scid.gui.model.ValueModels;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.SearchEngineTextMatcherEditor;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.SearchEngineTextFieldMatcherEditor;

public class ListController<E> {
    protected final EventList<E> listModel;

    protected final EventSelectionModel<E> selectionModel;

    protected final MutableValueModel<Boolean> canAdd = ValueModels.newBooleanModel(false);
    
    protected final AddAction addAction;
    
    protected final RemoveAction removeAction;
    
    public ListController(EventList<E> listModel) {
        if (listModel == null) {
            listModel = createModelEventList();
        }
        this.listModel = listModel;

        selectionModel = new EventSelectionModel<E>(listModel);
        
        addAction = createAddAction();
        addAction.setEnabled(canAdd());
        
        removeAction = createRemoveAction();
    }

    public ListController() {
        this(null);
    }

    public List<E> getSource() {
        return listModel;
    }

    public void setSource(List<? extends E> newSource) {
        EventList<E> list = getModel();
        listModel.clear();
        list.addAll(newSource);
    }

    EventList<E> getModel() {
        return listModel;
    }
    
    public void replaceSource(List<E> newSource, boolean updates) {
        GlazedLists.replaceAll(getModel(), newSource, updates);
    }
    
    public EventSelectionModel<E> getSelectionModel() {
        return selectionModel;
    }

    public void bindTable(JTable table, TableFormat<E> format) {
        EventTableModel<E> tableModel = createTableModel(format);

        table.setModel(tableModel);
        
        table.getActionMap().put("delete", removeAction);
        table.getActionMap().put("add", addAction);
    }

    protected EventTableModel<E> createTableModel(TableFormat<E> format) {
        EventTableModel<E> tableModel = new EventTableModel<E>(getModel(), format);
        return tableModel;
    }
    
    // Add
    public boolean canAdd() {
        return canAdd.get();
    }
    
    protected void setCanAdd(boolean newValue) {
        canAdd.set(newValue);
    }
    
    public void add() {
        int index = selectionModel.getMaxSelectionIndex() + 1;

        add(index);
    }

    public void add(int index) {
        E newElement = createElement();

        add(index, newElement);
    }
    
    public void add(int index, E element) {
        listModel.add(index, element);
    }

    protected E createElement() {
        throw new UnsupportedOperationException("must implement to create element");
    }
    
    // Move
    public void move(int[] indices, int dest) {
        // TODO
        for (int i = indices.length - 1; i >= 0; i--) {
            int sourceIndex = indices[i];
            
            List<E> subList = listModel.subList(dest, sourceIndex + 1);
            Collections.rotate(subList, 1);
        }
    }
    
    // Remove
    public void removeAt(int index) {
        listModel.remove(index);
    }
    
    public void remove() {
        // TODO
    }
    
    public void removeAt(int[] indices) {
        for (int i = indices.length - 1; i >= 0; i--) {
            int index = indices[i];
            removeAt(index);
        }
    }
    
    public void removeAll() {
        // TODO
    }
    
    protected EventList<E> createModelEventList() {
        return new BasicEventList<E>();
    }
    
    // Action factories
    protected AddAction createAddAction() {
        return new AddAction("Add");
    }
    
    protected RemoveAction createRemoveAction() {
        return new RemoveAction("Remove");
    }
    
    // Actions
    protected class AddAction extends AbstractAction {
        public AddAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            add();
        }
    }
    
    protected class RemoveAction extends AbstractAction {
        public RemoveAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            remove();
        }
    }
    
    public static class TextMatcherEditorController<E> implements ChangeListener {
        protected final SearchEngineTextMatcherEditor<E> matcherEditor;

        private ValueModel<String> filterText;
        
        public TextMatcherEditorController(SearchEngineTextMatcherEditor<E> matcherEditor) {
            super();
            this.matcherEditor = matcherEditor;
        }
        
        public void setFilterText(ValueModel<String> newModel) {
            if (this.filterText != null) {
                this.filterText.removeChangeListener(this);
            }
            this.filterText = newModel;
            
            if (newModel != null) {
                newModel.addChangeListener(this);
            }
        }

        public void refilter(String inputText) {
            matcherEditor.refilter(inputText);
        }
        
        protected void filterTextChanged() {
            String text = filterText.get();
            refilter(text);
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == filterText) {
                filterTextChanged();
            }
        }

        public void setFilterator(TextFilterator<? super E> filterator) {
            matcherEditor.setFilterator(filterator);
        }
    }
    
    
    public static class ListArrrangingController<E> {
        protected final EventList<E> sourceList;
        
        protected final SortedList<E> sortedList;
        
        protected final FilterList<E> filterList;
        
        
        
        
        public ListArrrangingController(EventList<E> sourceList) {
            this.sourceList = sourceList;
            
            sortedList = new SortedList<E>(sourceList, null);
            
            filterList = new FilterList<E>(sortedList);
            
        }

        public EventList<E> getSourceList() {
            return sourceList;
        }
        
        public EventList<E> getArrangedList() {
            return filterList;
        }
        
        public Comparator<? super E> getComparator() {
            return sortedList.getComparator();
        }

        public void setComparator(Comparator<? super E> comparator) {
            sortedList.setComparator(comparator);
        }

        protected void setMatcher(Matcher<? super E> matcher) {
            filterList.setMatcher(matcher);
        }

        protected void setMatcherEditor(MatcherEditor<? super E> editor) {
            filterList.setMatcherEditor(editor);
        }
        
        public SearchEngineTextMatcherEditor<E> bindFilterTextField(
                JTextField field, TextFilterator<? super E> textFilterator) {
            return new SearchEngineTextFieldMatcherEditor<E>(field, textFilterator);
        }
    }
}
