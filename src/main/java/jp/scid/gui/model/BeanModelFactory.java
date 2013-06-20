package jp.scid.gui.model;

public class BeanModelFactory {
    private final Object bean;

    public BeanModelFactory(Object bean) {
        if (bean == null) throw new IllegalArgumentException("bean must not be null");
        
        this.bean = bean;
    }
    
    public ValueModel<?> createModel(String propertyName) {
        if (propertyName == null)
            throw new IllegalArgumentException("propertyName must not be null");
        
        return new BeanPropertyAdapter(bean, propertyName, true, false);
    }
}
