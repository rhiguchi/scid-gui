package jp.scid.gui.model.connector;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModel;
import jp.scid.gui.model.ValueModels;

public abstract class ValueModelConnector<T, S> extends ValueConnector<T, ValueModel<? extends S>> {
    private final ChangeListener valueChangeListener = new ChangeListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void stateChanged(ChangeEvent e) {
            sourceChange((ValueModel<? extends S>) e.getSource());
        }
    };
    
    protected ValueModelConnector(MutableValueModel<T> target) {
        super(target);
    }
    
    protected ValueModelConnector() {
        super();
    }

    protected ValueModelConnector(T initialValue) {
        super(initialValue);
    }

    public static <S> ValueModelConnector<String, S> newFormatStringConnector(String formatString) {
        return new StringFormatConnector<S>(ValueModels.<String>newNullableValueModel(), formatString);
    }
    
    public static <S> ValueModelConnector<Boolean, S> newValueMatchConnector(Set<?> elementsForTrue) {
        return new ValueMatchConnector<S>(elementsForTrue);
    }
    
    abstract protected T convertModelValue(S sourceValue);
    
    @Override
    final protected T getModelValue(ValueModel<? extends S> source) {
        return convertModelValue(source.get());
    }
    
    @Override
    protected void installUpdateListener(ValueModel<? extends S> source) {
        source.addValueChangeListener(valueChangeListener);
    }

    @Override
    protected void uninstallUpdateListener(ValueModel<? extends S> source) {
        source.removeValueChangeListener(valueChangeListener);
    }
}

class StringFormatConnector<S> extends ValueModelConnector<String, S> {
    private final String formatString;
    
    public StringFormatConnector(MutableValueModel<String> target, String formatString) {
        super(target);
        
        if (formatString == null)
            throw new IllegalArgumentException("formatString must not be null");
        this.formatString = formatString;
    }

    @Override
    protected String convertModelValue(S sourceValue) {
        return String.format(formatString, sourceValue);
    }
}

class ValueMatchConnector<S> extends ValueModelConnector<Boolean, S> {
    private Set<?> selectableElements = Collections.emptySet();
    
    public ValueMatchConnector(Set<?> selectableElements) {
        super(ValueModels.newBooleanModel(false));
        if (selectableElements == null)
            throw new IllegalArgumentException("selectableElements must not be null");
        
        this.selectableElements = new HashSet<Object>(selectableElements);
    }
    
    public ValueMatchConnector(Object selectableElement) {
        this(Collections.singleton(selectableElement));
    }
    
    @Override
    protected Boolean convertModelValue(Object sourceValue) {
        return selectableElements.contains(sourceValue);
    }
}