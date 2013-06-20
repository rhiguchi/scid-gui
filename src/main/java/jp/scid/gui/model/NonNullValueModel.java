package jp.scid.gui.model;

public class NonNullValueModel<T> extends AbstractMutableValueModel<T> {
    /**
     * {@code null} を設定しようとする時の振る舞い。
     * @author Ryusuke Higuchi
     *
     */
    public static enum NullValueSettingStrategy {
        /** モデルの値の更新を行わない */
        IGNORE {
            @Override
            <T> void updateValue(NonNullValueModel<T> model) {
                // Do nothing
            }
        },
        /** {@code null} 用の値が設定される。 */
        REPLACE_INITIAL_VALUE {
            @Override
            <T> void updateValue(NonNullValueModel<T> model) {
                model.set(model.defaultValue);
            }
        },
        /** 例外を送出する。 */
        THROWS_EXCEPTION {
            @Override
            <T> void updateValue(NonNullValueModel<T> model) {
                throw new IllegalArgumentException("newValue must not be null");
            }
        },
        ;
        
        abstract <T> void updateValue(NonNullValueModel<T> model);
    }
    
    protected final T defaultValue;
    NullValueSettingStrategy nullValueSettingStrategy = NullValueSettingStrategy.IGNORE;
    
    public NonNullValueModel(T defaultValue) {
        super(defaultValue);
        if (defaultValue == null)
            throw new IllegalArgumentException("initialValue must not be null");
        
        this.defaultValue = defaultValue;
    }
    
    public NonNullValueModel(T defaultValue, NullValueSettingStrategy strategy) {
        this(defaultValue);
        
        if (strategy == null)
            throw new IllegalArgumentException("strategy must not be null");
        
        this.nullValueSettingStrategy = strategy;
    }
    
    public NullValueSettingStrategy getNullValueSettingStrategy() {
        return nullValueSettingStrategy;
    }
    
    public void setNullValueSettingStrategy(NullValueSettingStrategy nullValueSettingStrategy) {
        if (nullValueSettingStrategy == null)
            throw new IllegalArgumentException("nullValueSettingStrategy must not be null");
        this.nullValueSettingStrategy = nullValueSettingStrategy;
    }
    
    @Override
    public final void set(T newValue) {
        if (newValue == null) {
            nullValueSettingStrategy.updateValue(this);
        }
        else {
            super.set(newValue);
        }
    }
}
