package jp.scid.gui.control;

import jp.scid.gui.model.MutableValueModel;

public class ValueController<T> {
    MutableValueModel<T> model;

    public ValueController(MutableValueModel<T> model) {
        if (model == null)
            throw new IllegalArgumentException("model must not be null");

        this.model = model;
    }
}
