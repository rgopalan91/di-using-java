package com.javadi;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CustomDITest {

    @Mock
    StringPropertyHandler stringPropertyHandler;

    @Test
    public void verifyTwoObjectsAfterSingletonObjectCreation() {
        CustomDI customDI = new CustomDI(stringPropertyHandler);
        customDI.addSingletonBean(TestObject.class);
        TestObject objectOne = customDI.createObject(TestObject.class);
        TestObject objectTwo = customDI.createObject(TestObject.class);

        assertEquals(objectOne, objectTwo, "Both the objects are same");
    }

    @Test
    public void verifyTwoObjectsAfterPrototypeObjectCreation() {
        CustomDI customDI = new CustomDI(stringPropertyHandler);
        customDI.addPrototypeBean(TestObject.class);
        TestObject objectOne = customDI.createObject(TestObject.class);
        TestObject objectTwo = customDI.createObject(TestObject.class);

        assertNotEquals(objectOne, objectTwo, "Both the objects are not same");
    }

    @Test
    public void verifySingletonObjectCreation() {
        CustomDI customDI = new CustomDI(stringPropertyHandler);
        customDI.addSingletonBean(TestObject.class);
        TestObject object = customDI.createObject(TestObject.class);
        assertNotNull(object);
    }

    @Test
    public void verifyPrototypeObjectCreation() {
        CustomDI customDI = new CustomDI(stringPropertyHandler);
        customDI.addPrototypeBean(TestObject.class);
        TestObject object = customDI.createObject(TestObject.class);
        assertNotNull(object);
    }

    @Test
    public void verifyObjectCreationWithDependency() {
        CustomDI customDI = new CustomDI(stringPropertyHandler);
        customDI.addSingletonBean(TestObject.class);
        customDI.addSingletonBean(DependentTestObject.class);
        DependentTestObject object = customDI.createObject(DependentTestObject.class);
        assertNotNull(object);
    }


}