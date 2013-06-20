package jp.scid.gui.model;

import java.util.Collections;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import jp.scid.gui.model.connector.ValueConnector;
import jp.scid.gui.model.connector.ValueModelConnector;

public class ValueModels {
    private ValueModels() {
    }
    
    public static <T> MutableValueModel<T> newNullableValueModel() {
        return new SimpleValueModel<T>();
    }
    
    public static <T> NonNullValueModel<T> newValueModel(T initialValue) {
        return new NonNullValueModel<T>(initialValue);
    }
    
    public static <T> NonNullValueModel<List<T>> newListModel() {
        return new NonNullValueModel<List<T>>(Collections.<T>emptyList());
    }
    
    public static NonNullValueModel<Boolean> newBooleanModel(boolean initialValue) {
        return new NonNullValueModel<Boolean>(initialValue);
    }
    
    public static NonNullValueModel<Integer> newIntegerModel(int initialValue) {
        return new NonNullValueModel<Integer>(initialValue);
    }
    
    public static ValueModel<Object> newTreeSelectedNodeObject(TreeSelectionModel model) {
        TreeSelectedNodeConnector connector = new TreeSelectedNodeConnector();
        connector.setSource(model);
        return connector.getTargetModel();
    }

    public static ValueModel<Boolean> newInstanceCheckModel(ValueModel<? extends Object> base, Class<?> testClass) {
        InstanceMatchConnector conn = new InstanceMatchConnector(testClass);
        conn.setSource(base);
        return conn.getTargetModel();
    }
    
    public static <T> MutableValueModel<Boolean> newSelectionBooleanModel(MutableValueModel<T> adaptee, T selectionValue) {
        ElementSelectAdapter<T> adapter = new ElementSelectAdapter<T>(selectionValue);
        adapter.setSubject(adaptee);
        return adapter;
    }
    
    public static ValueModel<Boolean> newNegationBooleanModel(ValueModel<Boolean> adaptee) {
        ValueModelConnector<Boolean, Boolean> connector =
                ValueModelConnector.newValueMatchConnector(Collections.singleton(Boolean.FALSE));
        connector.setSource(adaptee);
        return connector.getTargetModel(); 
    }
    
    public static <T> ValueModel<String> newFormatStringModel(ValueModel<T> adaptee, String format) {
        ValueModelConnector<String, T> connector = ValueModelConnector.newFormatStringConnector(format);
        connector.setSource(adaptee);
        return connector.getTargetModel(); 
    }
    
    public static ValueModel<Boolean> newListElementsExistenceModel(ListModel source) {
        ListCountModelAdapter adapter = new ListCountModelAdapter(newIntegerModel(0));
        adapter.setSource(source);
        
        NumberThresholdModel<Integer> model = new NumberThresholdModel<Integer>(0);
        model.setSource(adapter.getTargetModel());
        
        return model.getTargetModel();
    }
    
    static class ListCountModelAdapter extends ValueConnector<Integer, ListModel> implements ListDataListener {
        public ListCountModelAdapter(MutableValueModel<Integer> target) {
            super(target);
        }
        
        @Override
        protected Integer getModelValue(ListModel source) {
            return source.getSize();
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            sourceChange((ListModel) e.getSource());
        }
        
        @Override
        public void intervalRemoved(ListDataEvent e) {
            sourceChange((ListModel) e.getSource());
        }
        
        @Override
        public void contentsChanged(ListDataEvent e) {
            sourceChange((ListModel) e.getSource());
        }
        
        @Override
        protected void installUpdateListener(ListModel source) {
            source.addListDataListener(this);
        }
        
        @Override
        protected void uninstallUpdateListener(ListModel source) {
            source.removeListDataListener(this);
        }
    }
}

class NumberThresholdModel<T extends Number & Comparable<T>> extends ValueModelConnector<Boolean, T> {
    private final T thresholdValue;
    
    public NumberThresholdModel(T thresholdValue) {
        super(ValueModels.newBooleanModel(false));
        
        if (thresholdValue == null) throw new IllegalArgumentException("thresholdValue must not be null");
        this.thresholdValue = thresholdValue;
    }
    
    @Override
    protected Boolean convertModelValue(T sourceValue) {
        return thresholdValue.compareTo(sourceValue) < 0;
    }
}

class InstanceMatchConnector extends ValueModelConnector<Boolean, Object> {
    private Class<?> testClass;
    
    public InstanceMatchConnector(Class<?> testClass) {
        super(ValueModels.newBooleanModel(false));
        
        if (testClass == null) throw new IllegalArgumentException("testClass must not be null");
        this.testClass = testClass;
    }
    
    @Override
    protected Boolean convertModelValue(Object sourceValue) {
        return testClass.isInstance(sourceValue);
    }
}

class TreeSelectedNodeConnector extends ValueConnector<Object, TreeSelectionModel> implements TreeSelectionListener {
    public TreeSelectedNodeConnector() {
        super(ValueModels.newNullableValueModel());
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        sourceChange((TreeSelectionModel) e.getSource());
    }

    @Override
    protected Object getModelValue(TreeSelectionModel source) {
        TreePath path = source.getLeadSelectionPath();
        if (path == null) {
            return null;
        }
        if (path.getLastPathComponent() instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
        }
        return null;
    }

    @Override
    protected void installUpdateListener(TreeSelectionModel source) {
        source.addTreeSelectionListener(this);
    }

    @Override
    protected void uninstallUpdateListener(TreeSelectionModel source) {
        source.removeTreeSelectionListener(this);
    }
}
