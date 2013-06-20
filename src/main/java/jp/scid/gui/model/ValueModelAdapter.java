package jp.scid.gui.model;

/**
 * ある主題値を {@link ValueModel} として扱うための接続用抽象クラス。
 * 
 * 主題値の変化は監視され、主題値が変更されるとこの値も変更される。
 * このモデルの値が変更された時は、主題値への適用を試みるが、
 * 実装はその変更を適用しないことも許される。
 * 
 * @author Ryusuke Higuchi
 *
 * @param <S> 主題値。
 * @param <T> 提供する値の型。
 */
public abstract class ValueModelAdapter<T, S> extends AbstractValueModel<T> implements MutableValueModel<T> {
    private S subject = null;
    
    protected ValueModelAdapter() {
    }
    
    protected ValueModelAdapter(S subject) {
        this();
        if (subject == null) throw new IllegalArgumentException("subject must not be null");
        
        installUpdateListener(subject);
    }
    
    @Override
    public T get() {
        return getSubjectValue();
    }

    @Override
    public void set(T newValue) {
        updateSubject(getSubject(), newValue);
    }
    
    /**
     * 主題値を取得する。
     * 
     * @return 主題値。
     */
    public S getSubject() {
        return subject;
    }
    
    /**
     * 新しい主題値を設定する。
     * 
     * 主題値の変化は監視され、主題値が変更されると、このモデルの変化としてリスナーに通知される。
     * 古い主題値は監視が解除される。
     * 
     * @param newSubject 新しい主題値。
     */
    public void setSubject(S newSubject) {
        T oldValue = null;
        
        if (this.subject != null) {
            uninstallUpdateListener(this.subject);
            oldValue = getValueFromSubject(this.subject);
        }
        
        this.subject = newSubject;
        
        T newValue = null;
        if (newSubject != null) {
            newValue = getValueFromSubject(newSubject);
            installUpdateListener(newSubject);
        }
        
        fireValueChange(oldValue, newValue);
    }
    
    /**
     * このモデルの値を主題値から取得し、更新を行う。
     * 
     * 主題値が {@code null} の時は、新しい値として {@code null} がこのモデルに適用される。
     * それ以外は、実装によって異なる値がこのモデルに適用される。
     * 
     * @see #getSubjectValue()
     */
    protected void subjectValueChange() {
        T newValue = getSubjectValue();
        updateSubject(getSubject(), newValue);
    }

    private T getSubjectValue() {
        S subj = getSubject();
        return getValueFromSubject(subj);
    }
    
    /**
     * 主題値からこのモデルが保持する型の値へ変換する。
     * 
     * 通常、このメソッドに {@code null} が渡されることはない。
     * 
     * @param subject 主題値。
     * @return 変換された値。
     */
    protected abstract T getValueFromSubject(S subject);
    
    /**
     * 主題値を変更する。
     * 
     * 適用する値が主題値の定義にそぐわない時は、実際に変更を行わなくても良い。
     * @param subject 新しい値が適用される主題値。
     * @param newValue 適用する値。
     */
    protected abstract void updateSubject(S subject, T newValue);
    
    /**
     * 主題値の変化を監視する処理を行う。
     * 
     * @param subject あたらしい主題値。
     */
    abstract void installUpdateListener(S subject);
    
    /**
     * 主題値の監視をやめる処理を行う。
     * 
     * @param subject 監視をやめる主題値。
     */
    abstract void uninstallUpdateListener(S subject);
}
