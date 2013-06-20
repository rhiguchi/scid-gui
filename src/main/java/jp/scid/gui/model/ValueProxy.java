package jp.scid.gui.model;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link ValueModel} の接続を行う。
 * @author Ryusuke Higuchi
 *
 * @param <T> 値の型。
 */
public class ValueProxy<T> extends ValueModelValueAdapter<T, T> {
    /**
     * 主題モデルを指定して、オブジェクトを作成する。
     * @param subjectModel 主題値モデル。
     * @see #setSubject(Object)
     */
    public ValueProxy(MutableValueModel<T> subjectModel) {
        setSubject(subjectModel);
    }

    @Override
    T convertValue(T subjectValue) {
        return subjectValue;
    }
    
    @Override
    protected void updateSubject(MutableValueModel<T> subject, T newValue) {
        subject.set(newValue);
    }
}

/**
 * {@link ValueModel} を別の型の {@link ValueModel} に変換する抽象定義。
 * 
 * @author Ryusuke Higuchi
 *
 * @param <S> 主題値の型。
 * @param <T> 変換後の値の型。
 */
abstract class ValueModelValueAdapter<T, S> extends ValueModelAdapter<T, MutableValueModel<S>> {
    private final ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            ChangeEvent event = new ChangeEvent(ValueModelValueAdapter.this);
            fireStateChange(event);
        }
    };

    @Override
    final protected T getValueFromSubject(MutableValueModel<S> subject) {
        return convertValue(subject.get());
    }
    
    abstract T convertValue(S subjectValue);
    
    /**
     * {@link MutableValueModel} の {@code value} 値の変化を監視する。
     */
    @Override
    protected final void installUpdateListener(MutableValueModel<S> newSubject) {
        newSubject.addValueChangeListener(changeListener);
    }
    
    /**
     * この {@link MutableValueModel} の変化監視を停止する。
     */
    @Override
    protected final void uninstallUpdateListener(MutableValueModel<S> newSubject) {
        newSubject.removeValueChangeListener(changeListener);
    }
}
