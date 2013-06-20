package jp.scid.gui.control;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToggleButton;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModels;

public class TableColumnEditor {
    private final Map<TableColumn, ButtonModel> visibilityMap = new HashMap<TableColumn, ButtonModel>();
    
    final TableColumnModel columnModel;
    
    /** visibility model for dialog */
    final MutableValueModel<Boolean> dialogVisibled = ValueModels.newBooleanModel(false);
    
    public TableColumnEditor(TableColumnModel columnModel) {
        this.columnModel = columnModel;
    }
    
    boolean columnVisibled(TableColumn column) {
        for (int index = columnModel.getColumnCount() - 1; index >= 0; index--) {
            TableColumn modelColumn = columnModel.getColumn(index);
            if (modelColumn.equals(column))
                return true;
        }
        
        return false;
    }
    
    void showColumn(TableColumn column) {
        if (!columnVisibled(column)) {
            columnModel.addColumn(column);
        }
    }
    
    void hideColumn(TableColumn column) {
        columnModel.removeColumn(column);
    }
    
    TableColumnModel getColumnModel() {
        return columnModel;
    }
    
    void reloadButtonModel() {
        // reload model selection
        for (Map.Entry<TableColumn, ButtonModel> e: visibilityMap.entrySet()) {
            TableColumn column = e.getKey();
            final boolean selected = columnVisibled(column);
            
            e.getValue().setSelected(selected);
        }
        
        dialogVisibled.set(true);
    }
    
    void commitEditing() {
        for (Map.Entry<TableColumn, ButtonModel> e: visibilityMap.entrySet()) {
            boolean visibled = e.getValue().isSelected();
            TableColumn column = e.getKey();
            
            if (visibled) {
                showColumn(column);
            }
            else {
                hideColumn(column);
            }
        }
    }
    
    // Action methods
    
    public void edit() {
        reloadButtonModel();
        dialogVisibled.set(false);        
    }
    
    public void hide() {
        dialogVisibled.set(false);        
    }
    
    // Bindings
    
    public void bindColumnVisibilityToggleButton(JToggleButton button, TableColumn column) {
        ButtonModel model = new JToggleButton.ToggleButtonModel();
        visibilityMap.put(column, model);
        
        button.setModel(model);
    }
    
    public void bindOkButton(JButton button) {
        button.addActionListener(new OkAction());
    }
    
    public void bindCancelButton(JButton button) {
        button.addActionListener(new CancelAction());
    }
    
    public void bindDialog(JDialog dialog) {
        ComponentPropertyConnector.connect(dialogVisibled, dialog, "visible");
    }
    
    // Actions
    
    class OkAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            commitEditing();
            hide();
        }
    }
    
    class CancelAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            hide();
        }
    }
}