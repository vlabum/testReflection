package ru.vlabum.testreflection;

import java.util.*;

public class PhoneBook {

    private Map<String, String> book = new LinkedHashMap<>();

    public PhoneBook() { }

    public PhoneBook(boolean isLinked) {
        book = isLinked ? book : new HashMap<>();
    }

    public void add(String number, String surname) {
        book.put(number, surname);
    }

    /**
     * Поиск номеров по фамилии
     * @param surname фамилия
     * @return ArrayList из номеров телефонов
     */
    public List<String> get(String surname) {
        List<String> list = new ArrayList<>();
        if (surname == null) return list;
        for (Map.Entry<String, String> m : book.entrySet()) {
            if (surname.equals(m.getValue())) list.add(m.getKey());
        }
        return list;
    }

}
