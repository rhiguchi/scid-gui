package jp.scid.gui.model.connector;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModels;

public abstract class ValueConnector<T, S> {
    private final MutableValueModel<T> target;
    private S source;

    protected ValueConnector(MutableValueModel<T> target) {
        if (target == null) throw new IllegalArgumentException("target must not be null");
        this.target = target;
    }
    
    protected ValueConnector(T initialValue) {
        this(ValueModels.newValueModel(initialValue));
    }
    
    protected ValueConnector() {
        this(ValueModels.<T>newNullableValueModel());
    }

    public MutableValueModel<T> getTargetModel() {
        return target;
    }

    protected void setValue(T newValue) {
        target.set(newValue);
    }
    
    public void sourceChange(S source) {
        T newValue = getModelValue(source);
        setValue(newValue);
    }
    
    abstract protected T getModelValue(S source);
    
    public void setSource(S source) {
        if (this.source != null) {
            uninstallUpdateListener(this.source);
        }
        
        this.source = source;
        
        if (source != null) {
            installUpdateListener(source);
            sourceChange(source);
        }
    }
    
    abstract protected void installUpdateListener(S source);
    
    abstract protected void uninstallUpdateListener(S source);
}