package br.com.mercadolivre.dnaanalyzer.pojo.test;

import br.com.mercadolivre.dnaanalyzer.test.BaseUnitTest;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterBasedOnInheritance;
import com.openpojo.reflection.impl.PojoClassFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

public class PojoTest extends BaseUnitTest {

    @Test
    public void testMethodsDto() {

        final List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(
            "br.com.mercadolivre.dnaanalyzer.dto", new FilterBasedOnInheritance(Serializable.class));

        this.invokeMethods(pojoClasses);
    }

    @Test
    public void testMethodsModel() {

        final List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(
            "br.com.mercadolivre.dnaanalyzer.model", new FilterBasedOnInheritance(Serializable.class));

        this.invokeMethods(pojoClasses);
    }

    private void invokeMethods(List<PojoClass> pojoClasses) {
        pojoClasses.forEach(pojoClass -> {
            final Class<?> clazz = pojoClass.getClazz();

            try {
                this.invokeToString(clazz);
                this.invokeEquals(clazz);
                this.invokeHashCode(clazz);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void invokeToString(Class<?> testClass) throws Exception {
        final Method method = testClass.getDeclaredMethod("toString");

        method.invoke(testClass.getDeclaredConstructor().newInstance());
    }

    private void invokeEquals(Class<?> testClass) throws Exception {
        final Method method = testClass.getDeclaredMethod("equals", Object.class);
        final Object instance = testClass.getDeclaredConstructor().newInstance();
        final Object otherInstance = testClass.getDeclaredConstructor().newInstance();

        method.invoke(instance, otherInstance);
    }

    private void invokeHashCode(Class<?> testClass) throws Exception {
        final Method method = testClass.getDeclaredMethod("hashCode");

        method.invoke(testClass.getDeclaredConstructor().newInstance());
    }

}
