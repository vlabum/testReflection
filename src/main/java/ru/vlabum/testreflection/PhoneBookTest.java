package ru.vlabum.testreflection;

import ru.vlabum.testreflection.annotations.AfterSuite;
import ru.vlabum.testreflection.annotations.BeforeSuite;
import ru.vlabum.testreflection.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookTest {

    private int maxNumber = 100000;

    PhoneBook phoneBook;

    @BeforeSuite
    public final void beforeTests() {
        System.out.println("Hi!!!");
        phoneBook = new PhoneBook(false);
    }

    @AfterSuite
    public final void afterTests() {
        System.out.println("Seeya!!!");
    }

    @Test
    public final void useHashMap() {
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            final String name = "Name " + i;
            for (int j = 0; j < maxNumber; j++) {
                final String number = String.valueOf((i * maxNumber) + j);
                phoneBook.add(number, name);
            }
        }
        System.out.printf("HashMap Заполнение %d\n", System.nanoTime() - start);
        final String fname = "Name 10";
        start = System.nanoTime();
        final List<String> list = phoneBook.get(fname);
        System.out.printf("HashMap Поиск полным перебором %d\n", System.nanoTime() - start);
    }


    @Test(value = 8)
    public final void useLinkedHashMap() {
        final PhoneBook phoneBook = new PhoneBook(true);
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            final String name = "Name " + i;
            for (int j = 0; j < maxNumber; j++) {
                final String number = String.valueOf((i * maxNumber) + j);
                phoneBook.add(number, name);
            }
        }
        System.out.printf("LinkedHashMap Заполнение %d\n", System.nanoTime() - start);
        final String fname = "Name 10";
        start = System.nanoTime();
        final List<String> list = phoneBook.get(fname);
        System.out.printf("LinkedHashMap Поиск полным перебором %d\n", System.nanoTime() - start);
    }

    /**
     * Тест для PhoneBook, в котором используется LinkedHashMap
     */
    @Test(value = 4)
    public final void checkFind() {
        phoneBook = new PhoneBook();
        phoneBook.add("+79991111111", "Иванов");
        phoneBook.add("+79992222222", "Петров");
        phoneBook.add("+79993333333", "Петров");
        phoneBook.add("+79994444444", "Сидоров");
        final List<String> list = new ArrayList<>();
        list.add("+79992222222");
        list.add("+79993333333");
        if (list.equals(phoneBook.get("Петров"))) System.out.println("Эквивалентные");
        else System.out.println("Разные");
    }

}
