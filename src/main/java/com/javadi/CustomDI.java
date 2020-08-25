package com.javadi;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CustomDI {
    public enum BeanType {
        SINGLETON,
        PROTOTYPE
    }

    private HashMap<String, Object> objects;
    private HashMap<String, BeanRegistration> registeredBeans;
    private StringPropertyHandler stringPropertyHandler;

    public CustomDI(StringPropertyHandler stringPropertyHandler) {
        registeredBeans = new HashMap<>();
        objects = new HashMap<>();
        this.stringPropertyHandler = stringPropertyHandler;
    }

    private void register(String key, Type classType, BeanType beanType) {
        try {
            Class<?> deliveredClass = Class.forName(classType.getTypeName());

            if (!registeredBeans.containsKey(key)) {
                registeredBeans.put(key, new BeanRegistration(deliveredClass, beanType));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T, TExtension extends T> void addPrototypeBean(Class<T> requestedClass, Class<TExtension> createdClass) {
        register(requestedClass.getTypeName(), createdClass, BeanType.PROTOTYPE);
    }

    public <T> void addPrototypeBean(Class<T> className) {
        this.addPrototypeBean(className, className);
    }

    public <T, TExtension extends T> void addSingletonBean(Class<T> requestedClass, Class<TExtension> createdClass) {
        register(requestedClass.getTypeName(), createdClass, BeanType.SINGLETON);
    }

    public <T> void addSingletonBean(Class<T> className) {
        this.addSingletonBean(className, className);
    }

    private <T> boolean isRegistered(Class<T> className) {
        return registeredBeans.containsKey(className.getTypeName());
    }

    private <T> T getPreviouslyCreatedObject(Type className, BeanType creationType) {
        switch (creationType) {
            case PROTOTYPE:
                return getPrototypeObject(className.getTypeName());
            case SINGLETON:
                return getSingletonObject(className.getTypeName());
            default:
                return null;
        }
    }

    private <T> T getPrototypeObject(String classTypeName) {
        return null;
    }

    private <T> T getSingletonObject(String classTypeName) {
        return (T) objects.get(classTypeName);
    }

    private void registerNewObject(String classTypeName, BeanType beanCreationType, Object object) {
        if (beanCreationType == BeanType.SINGLETON) {
            objects.put(classTypeName, object);
        }
    }

    private <T> T createObject(Class<T> className, HashSet<String> dependencyList) {
        if (!isRegistered(className)) {
            System.out.println("Class Not Registered");
        }

        BeanRegistration beanRegistration = registeredBeans.get(className.getTypeName());
        T existentObject = getPreviouslyCreatedObject(beanRegistration.getClassType(), beanRegistration.getBeanCreationType());
        if (existentObject != null) return existentObject;

        String classTypeName = className.getTypeName();

        if (dependencyList == null) {
            dependencyList = new HashSet<>();
        }

        if (dependencyList.contains(classTypeName)) {
            System.out.println("Circular Dependency occurred while creating a class");
        }

        dependencyList.add(classTypeName);

        try {
            Constructor<?>[] constructors = beanRegistration.getClassType().getConstructors();

            for (Constructor<?> constructor : constructors) {
                if (Modifier.isPublic(constructor.getModifiers())) {
                    List<Object> constructorParameters = new ArrayList<>();

                    //obtain  all constructor parameters
                    for (Parameter parameter : constructor.getParameters()) {
                        if (parameter.getType() == String.class) {
                            CustomValue annotation = parameter.getAnnotation(CustomValue.class);
                            String property = annotation.value();

                            String objectParameter = stringPropertyHandler.getProperty(property);
                            constructorParameters.add(objectParameter);
                        } else {
                            T objectParameter = (T) createObject(parameter.getType(), dependencyList);
                            constructorParameters.add(objectParameter);
                        }
                    }

                    //create new object
                    T myObj = (T) constructor.newInstance(constructorParameters.toArray());

                    registerNewObject(classTypeName, beanRegistration.getBeanCreationType(), myObj);

                    return myObj;
                }
            }


        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T createObject(Class<T> className) {
        return createObject(className, null);
    }
}
