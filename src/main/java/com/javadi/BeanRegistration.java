package com.javadi;

public class BeanRegistration {
    private Class<?> classType;
    private CustomDI.BeanType beanCreationType;

    public BeanRegistration(Class<?> classType, CustomDI.BeanType beanCreationType) {
        this.classType = classType;
        this.beanCreationType = beanCreationType;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    public CustomDI.BeanType getBeanCreationType() {
        return beanCreationType;
    }

    public void setBeanCreationType(CustomDI.BeanType beanCreationType) {
        this.beanCreationType = beanCreationType;
    }
}
