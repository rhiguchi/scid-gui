package jp.scid.gui.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

import jp.scid.gui.model.MutableValueModel;
import jp.scid.gui.model.ValueModel;
import jp.scid.gui.model.ValueModels;

@Deprecated
public class TaskProgressController implements PropertyChangeListener {
    protected final MutableValueModel<Boolean> running;
    protected final MutableValueModel<Integer> progress;
    
    public TaskProgressController(MutableValueModel<Boolean> running, MutableValueModel<Integer> progress) {
        this.running = running;
        this.progress = progress;
    }
    
    public TaskProgressController() {
        this(ValueModels.newBooleanModel(false), ValueModels.newIntegerModel(0));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingWorker<?, ?> task = (SwingWorker<?, ?>) evt.getSource();
        
        if ("state".equals(evt)) {
            updateRunning(task);
        }
        else if ("progress".equals(evt)) {
            updateProgress(task);
        }
    }
    
    protected void updateRunning(SwingWorker<?, ?> task) {
        switch (task.getState()) {
        case STARTED:
            setRunning(true);
            break;
        case DONE:
            setRunning(false);
            break;
        }
    }
    
    protected void updateProgress(SwingWorker<?, ?> task) {
        int value = task.getProgress();
        
        progress.set(value);
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public void setRunning(boolean newValue) {
        running.set(newValue);
    }
    
    public void listenTo(SwingWorker<?, ?> task) {
        task.addPropertyChangeListener(this);
    }
    
    public void deafTo(SwingWorker<?, ?> task) {
        task.removePropertyChangeListener(this);
    }
}
