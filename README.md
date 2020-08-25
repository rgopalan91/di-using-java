# Dependency Injection Using Java

## Design Considerations:

To create a basic dependency injection framework like Spring using only Java to support Singleton and Prototype bean scopes.

### Types of creation

Dependencies can be registered in two ways:

#### Singleton Type

When a dependency is registered as a singleton object, every time we ask for it, same instance will be received.

#### Transient Type

When a dependency is registered as a transient object, every time we ask for it, a new instance will be created.

### Getting Started

For 2 objects that depends on each other, below is the implementation to be done usinf this framework.

ServiceOne.java
```
public class ServiceOne {
    public ServiceOne(){

    }
}
```

ServiceTwo.java
```
public class ServiceTwo {
    private final ServiceOne serviceOne;
    private final String key;

    public ServiceTwo(ServiceOne serviceOne, @CustomValue("keyOne") String key) {
        this.serviceOne = serviceOne;
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
```

PropertyHandler.java
This class implements the StringPropertyHandler interface and it is used to obtain all the string dependencies of a class.

```
public class PropertyHandler implements StringPropertyHandler {
    public String getProperty(String key) {
        if ("keyOne".equals(key)) {
            return "valueOne";
        }
        return "default";

    }
}
```

ObjectFactory.java

Below is the factory which is responsible for creating all the objects
```
public class ObjectFactory {
    private static ObjectFactory objectFactory;
    private CustomDI customDI;

    private ObjectFactory() {
        customDI = new CustomDI(new PropertyHandler());
        register();
    }

    public static ObjectFactory getInstance() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }

        return objectFactory;
    }

    public void register() {
        customDI.addSingletonBean(ServiceTwo.class);
        customDI.addSingletonBean(ServiceOne.class);
    }

    public <T> T get(Class<T> className) {
        return customDI.createObject(className);
    }
}
```

After doing all the above steps, we just have to call the factory by informing the object that needs to be created.
```
ServiceTwo serviceTwo=ObjectFactory.getInstance().get(ServiceTwo.class);
System.out.println(serviceTwo.getKey());
```
valueOne will be printed as output for the key keyOne
### Limitations
1. All the registered objects should have one public constructor. 
2. Abstract classes will not be created.
3. Interfaces cannot be created.
4. Object dependency graph should not have circular dependencies
5. Exception handling is not implemented
