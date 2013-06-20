package jp.scid.gui.model.connector;

import java.awt.EventQueue;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import jp.scid.gui.model.MutableValueModel;

public class DocumentTextConnector extends ValueConnector<String, Document> implements DocumentListener {

    public DocumentTextConnector(MutableValueModel<String> target) {
        super(target);
    }

    private void updateLater(final Document document) {
        Runnable task = new Runnable() {
            public void run() {
                sourceChange(document);
            }
        };
        EventQueue.invokeLater(task);
    }
    
    @Override public void insertUpdate(DocumentEvent e) { updateLater(e.getDocument()); }
    @Override public void removeUpdate(DocumentEvent e) { updateLater(e.getDocument()); }
    @Override public void changedUpdate(DocumentEvent e) { updateLater(e.getDocument()); }

    @Override
    protected String getModelValue(Document source) {
        try {
            return source.getText(0, source.getLength());
        }
        catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void installUpdateListener(Document source) {
        source.addDocumentListener(this);
    }

    @Override
    protected void uninstallUpdateListener(Document source) {
        source.removeDocumentListener(this);
    }
}