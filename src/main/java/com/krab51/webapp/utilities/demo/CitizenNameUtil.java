package com.krab51.webapp.utilities.demo;

import java.util.List;
import java.util.Random;

import static com.krab51.webapp.utilities.ResourceLoader.getResourceAsList;
import static java.util.Objects.requireNonNull;

/**
 * Утилита для генерации корректных имен случайным образом
 */
public class CitizenNameUtil {
    private static final Random random = new Random();
    private static final List<String> firstMaleNames = getResourceAsList("data/demo/names/firstMaleNames");
    private static final List<String> firstFemaleNames = getResourceAsList("data/demo/names/firstFemaleNames");
    private static final List<String> lastNames = getResourceAsList("data/demo/names/lastNames");

    /**
     * Генерирует случайным образом пол гражданина
     *
     * @return Пол гражданина
     */
    public Gender generateRandomGender() {
        return random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
    }

    /**
     * Генерирует случайным образом полное имя гражданина в зависимости от пола
     *
     * @param gender Пол гражданина
     * @return Объект содержащий полное имя гражданина разбитое по полям
     */
    public CitizenName generateRandomName(Gender gender) {
        return new CitizenName(
                generateRandomFirstName(gender),
                generateRandomLastName(gender),
                generateRandomMiddleName(gender)
        );
    }

    /**
     * Генерирует случайным образом имя гражданина в зависимости от пола
     *
     * @param gender Пол гражданина
     * @return Имя гражданина в виде строки
     */
    public String generateRandomFirstName(Gender gender) {
        return switch (gender) {
            case MALE -> requireNonNull(firstMaleNames).get(random.nextInt(firstMaleNames.size()));
            case FEMALE -> requireNonNull(firstFemaleNames).get(random.nextInt(firstFemaleNames.size()));
        };
    }

    /**
     * Генерирует случайным образом фамилию гражданина в зависимости от пола
     *
     * @param gender Пол гражданина
     * @return Фамилия гражданина в виде строки
     */
    public String generateRandomLastName(Gender gender) {
        String lastName = requireNonNull(lastNames).get(random.nextInt(lastNames.size()));

        if (gender == Gender.FEMALE) lastName += "а";

        return lastName;
    }

    /**
     * Генерирует случайным образом отчество гражданина в зависимости от пола
     *
     * @param gender Пол гражданина
     * @return Отчество гражданина в виде строки
     */
    public String generateRandomMiddleName(Gender gender) {
        String middleMane = generateRandomFirstName(Gender.MALE);

        middleMane += gender == Gender.MALE ? "ович" : "овна";

        return middleMane;
    }

    /**
     * Пол гражданина
     */
    public enum Gender {
        MALE("M"),
        FEMALE("F");

        public final String abbr;

        Gender(String abbr) {
            this.abbr = abbr;
        }
    }

    /**
     * Полное имя гражданина
     */
    public static class CitizenName {
        public final String first;
        public final String last;
        public final String middle;

        public CitizenName(String first, String last, String middle) {
            this.first = first;
            this.last = last;
            this.middle = middle;
        }
    }
}
