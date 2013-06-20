package jp.scid.gui.control;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * {@link JTextComponent} およびそのサブクラスと値モデルを結合するためのクラス。
 * 
 * {@link Document} の変更は削除イベントと挿入イベントに分かれるため、
 * モデルからのアップデートは次の Event Dispatch Thread から遅延実行される。
 * @author Ryusuke Higuchi
 *
 * @param <T>
 * @param <V>
 */
abstract class TextComponentValueConnector<T extends JTextComponent, V> extends ViewValueConnector<T, V> {
    public TextComponentValueConnector(T view) {
        super(view);
    }
    
    abstract protected V getEditorValue(T editorView);
}
