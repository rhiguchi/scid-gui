package jp.scid.gui.model;

public class ValueModels {
    private ValueModels() {
    }
    
    public static <T> MutableValueModel<T> newValueModel() {
        return new ValueHolder<T>();
    }
    
    public static MutableValueModel<Boolean> newBooleanModel(boolean initialValue) {
        return new NonNullValueModel<Boolean>(initialValue);
    }
}
