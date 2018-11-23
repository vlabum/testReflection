package ru.vlabum.testreflection;

import org.jetbrains.annotations.NotNull;
import ru.vlabum.testreflection.annotations.AfterSuite;
import ru.vlabum.testreflection.annotations.BeforeSuite;
import ru.vlabum.testreflection.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestRunner {

    private static int BEFORE_SUITE_RANGE = 0;

    private static int AFTER_SUITE_RANGE = 11;

    public static void start(@NotNull final Class pClass) {
        try {
            runTests(pClass);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void start(@NotNull final String className) {
        try {
            final Class pClass = Class.forName(className);
            runTests(pClass);
        } catch (
                ClassNotFoundException
                        | InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException e
        ) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление метода с аннотацией BeforeSuite
     * @param rangeMethods
     * @param method
     * @throws RuntimeException
     */
    private static void appendMethodBeforeSuite(
            @NotNull final Map<Integer, List<Method>> rangeMethods,
            @NotNull final Method method
    ) throws RuntimeException {
        if (rangeMethods.containsKey(BEFORE_SUITE_RANGE)) {
            throw new RuntimeException("Annotation @BeforeSuite must be specified no more than once");
        } else {
            final List<Method> lst = new ArrayList<>();
            lst.add(method);
            rangeMethods.put(BEFORE_SUITE_RANGE, lst);
        }
    }

    /**
     * Добавляет методы с аннотацией AfterSuite
     * @param rangeMethods
     * @param method
     * @throws RuntimeException
     */
    private static void appendMethodAfterSuite(
            @NotNull final Map<Integer, List<Method>> rangeMethods,
            @NotNull final Method method
    ) throws RuntimeException {
        if (rangeMethods.containsKey(AFTER_SUITE_RANGE)) {
            throw new RuntimeException("Annotation @AfterSuite must be specified no more than once");
        } else {
            final List<Method> lst = new ArrayList<>();
            lst.add(method);
            rangeMethods.put(AFTER_SUITE_RANGE, lst);
        }
    }

    /**
     * Добавляет методы с Аннотацией @Test
     * @param rangeMethods
     * @param method
     * @throws RuntimeException
     */
    private static void appendMethodTest(
            @NotNull final Map<Integer, List<Method>> rangeMethods,
            @NotNull final Method method
    ) throws RuntimeException {
        if (method.isAnnotationPresent(Test.class)) {
            final Test param = method.getAnnotation(Test.class);
            int range = param.value();
            if (range <= BEFORE_SUITE_RANGE) range = BEFORE_SUITE_RANGE + 1;
            if (range >= AFTER_SUITE_RANGE) range = AFTER_SUITE_RANGE - 1;
            if (rangeMethods.containsKey(range) && rangeMethods.get(range) != null) {
                rangeMethods.get(range).add(method);
            } else {
                List<Method> lst = new ArrayList<>();
                lst.add(method);
                rangeMethods.put(range, lst);
            }
        }
    }

    /**
     * Добавляет методы на запуск
     * @param rangeMethods
     * @param method
     * @throws RuntimeException
     */
    private static void appendMethod(
            @NotNull final Map<Integer, List<Method>> rangeMethods,
            @NotNull final Method method
    ) throws RuntimeException {
        if (method.isAnnotationPresent(BeforeSuite.class)) appendMethodBeforeSuite(rangeMethods, method);
        if (method.isAnnotationPresent(AfterSuite.class)) appendMethodAfterSuite(rangeMethods, method);
        if (method.isAnnotationPresent(Test.class)) appendMethodTest(rangeMethods, method);
    }

    /**
     * Выбирает методы и запускает их
     * @param pClass
     */
    private static void runTests(@NotNull final Class pClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        final Map<Integer, List<Method>> methods = new TreeMap<>();
        for (final Method m : pClass.getMethods()) {
            appendMethod(methods, m);
        }

        // создается экземпляр класса и запускаются тесты
        final Object obj = pClass.newInstance();
        for(Map.Entry<Integer, List<Method>> entry : methods.entrySet()) {
            for (final Method method : entry.getValue()) {
                System.out.println(method.toString());
                method.invoke(obj);
            }
        }
    }

}
