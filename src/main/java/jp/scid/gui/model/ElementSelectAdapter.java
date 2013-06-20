package jp.scid.gui.model;

public class ElementSelectAdapter<E> extends ValueModelValueAdapter<Boolean, E> {
    private final E trueElement;
    private final E falseElement;
    
    public ElementSelectAdapter(E trueElement, E falseElement) {
        super();
        this.trueElement = trueElement;
        this.falseElement = falseElement;
    }
    
    public ElementSelectAdapter(E trueElement) {
        this(trueElement, null);
    }

    @Override
    Boolean convertValue(E subjectValue) {
        if (subjectValue == null) {
            return trueElement == null;
        }
        return subjectValue.equals(trueElement);
    }

    @Override
    protected void updateSubject(MutableValueModel<E> subject, Boolean selected) {
        E newValue = selected ? trueElement : falseElement;
        subject.set(newValue);
    }

}
