package jp.scid.gui.control;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import jp.scid.gui.control.BooleanModelBindings.ModelConnector;
import jp.scid.gui.model.ValueModel;

public class StringModelBindings extends AbstractValueModelBindigs<String> {

    public StringModelBindings(ValueModel<String> model) {
        super(model);
    }
    
    public ModelConnector bindToLabelText(final JLabel label) {
        return installModel(new AbstractPropertyConnector<String>() {
            @Override
            protected void valueChanged(String newValue) {
                label.setText(newValue);
            }
        });
    }
    
    public ModelConnector bindToButtonText(final AbstractButton button) {
        return installModel(new AbstractPropertyConnector<String>() {
            @Override
            protected void valueChanged(String newValue) {
                button.setText(newValue);
            }
        });
    }

    public ModelConnector bindToTextField(final JTextField field) {
        TextComponentBinding binding = new TextComponentBinding(field);
        return installModel(binding);
    }
}

abstract class AbstractValueModelBindigs<T> {
    protected final ValueModel<T> model;

    public AbstractValueModelBindigs(ValueModel<T> model) {
        if (model == null) throw new IllegalArgumentException("model must not be null");
        this.model = model;
    }
    
    <C extends AbstractPropertyConnector<T>> C installModel(C connector) {
        connector.setModel(model);
        return connector;
    }
    
    abstract static class AbstractPropertyConnector<T> extends ValueChangeHandler<T> implements ModelConnector {
        public void dispose() {
            setModel(null);
        }
    }
    
    public static class TextComponentBinding extends AbstractPropertyConnector<String> {
        private final JTextComponent component;
        
        public TextComponentBinding(JTextComponent component) {
            if (component == null)
                throw new IllegalArgumentException("component must not be null");
            this.component = component;
        }

        @Override
        protected void valueChanged(String newValue) {
            component.setText(newValue);
        }
    }
    
    public static class FormattedTextFieldBinding extends AbstractPropertyConnector<Object> {
        private final JFormattedTextField component;
        
        public FormattedTextFieldBinding(JFormattedTextField component) {
            if (component == null)
                throw new IllegalArgumentException("component must not be null");
            this.component = component;
        }
        
        @Override
        protected void valueChanged(Object newValue) {
            component.setValue(newValue);
        }
    }
    
    abstract static class ComponentPropertyConnector<T, C extends Component> extends AbstractPropertyConnector<T> {
        private final C component;
        
        protected ComponentPropertyConnector(C component) {
            super();
            if (component == null)
                throw new IllegalArgumentException("component must not be null");
            this.component = component;
        }
        
        abstract protected void applyValue(C component, T newValue);
        
        @Override
        protected void valueChanged(T newValue) {
            applyValue(component, newValue);
        }
    }
}