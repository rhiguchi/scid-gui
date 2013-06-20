package jp.scid.gui.model;

import java.awt.EventQueue;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class DocumentTextModel extends ValueModelAdapter<String, Document> {
    private final DocumentListener documentListener = new DocumentListener() {
        public void removeUpdate(DocumentEvent e) {
            updateValueLater();
        }
        public void insertUpdate(DocumentEvent e) {
            updateValueLater();
        }
        public void changedUpdate(DocumentEvent e) {
            // ignore
        }
    };
    
    void updateValueLater() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                subjectValueChange();
            }
        });
    }
    
    public DocumentTextModel() {
        super();
    }
    
    @Override
    protected void installUpdateListener(Document newSubject) {
        newSubject.addDocumentListener(documentListener);
    }
    
    @Override
    protected void uninstallUpdateListener(Document newSubject) {
        newSubject.removeDocumentListener(documentListener);
    }
    
    @Override
    protected String getValueFromSubject(Document document) {
        if (document == null)
            return "";
        
        try {
            return document.getText(0, document.getLength());
        }
        catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void updateSubject(Document document, String newValue) {
        if (document == null)
            return;
        try {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).replace(0, document.getLength(), newValue, null);
            }
            else {
                document.remove(0, document.getLength());
                document.insertString(0, newValue, null);
            }
        }
        catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }
    }
}
