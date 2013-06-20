package jp.scid.gui.control;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.scid.gui.model.ValueModel;

/**
 * 単一モデルをデータモデルとする操作クラス。
 * @author Ryusuke Higuchi
 *
 * @param <V>
 */
public abstract class AbstractValueController<V> extends AbstractController<ValueModel<V>> {
    private final ChangeListener modelChangeListener = new ChangeListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void stateChanged(ChangeEvent e) {
            modelChange((ValueModel<V>) e.getSource());
        }
    };
    
    /**
     * プロパティ名に関係なくモデル内の値を取得して処理を行う。
     * @see #processValueChange(Object)
     */
    @Override
    protected void processPropertyChange(ValueModel<V> model, String property) {
        V newValue = model.get();
        processValueChange(newValue);
    }
    
    /**
     * モデルに変化通知が行われたときに、モデルの値の処理を行う。
     * @param newValue
     */
    protected abstract void processValueChange(V newValue);
    
    @Override
    protected void listenTo(ValueModel<V> model) {
        model.addValueChangeListener(modelChangeListener);
    }
    
    @Override
    protected void deafTo(ValueModel<V> model) {
        model.removeValueChangeListener(modelChangeListener);
    }
}
