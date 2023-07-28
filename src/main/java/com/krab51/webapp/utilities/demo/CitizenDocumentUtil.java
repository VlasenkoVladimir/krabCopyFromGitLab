package com.krab51.webapp.utilities.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.krab51.webapp.utilities.ResourceLoader.getResourceAsList;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

/**
 * Утилита для генерации корректных документов граждан России случайным образом
 */
public class CitizenDocumentUtil {
    private final Random random = new Random();
    private final Map<Integer, List<Authority>> authorities = new HashMap<>();

    public CitizenDocumentUtil() {
        requireNonNull(getResourceAsList("data/demo/authority/unit.csv")).forEach(s -> {
            String[] values = s.split("\\|", -1);
            Authority authority = new Authority(values[0], values[1]);
            List<Authority> authorityList = authorities.computeIfAbsent(authority.region, k -> new ArrayList<>());
            authorityList.add(new Authority(values[0], values[1]));
        });
    }

    /**
     * Генерирует случайным образом паспорт гражданина
     *
     * @param region Код региона России
     * @param okato  Код ОКАТО
     * @param date   Дата выдачи паспорта
     * @return Паспорт гражданина с данными об органе выдавшего документ
     */
    public Passport generateRandomPassport(int region, String okato, String date) {
        return new Passport(
                okato.substring(0, 2) + date.substring(2, 4), // TODO регион должен браться не по ОКАТО
                String.valueOf(RandomUtilites.getRandomNumber(100000, 999999)),
                generateRandomAuthority(region), date);
    }

    /**
     * Генерирует случайным образом орган выдающего документы
     *
     * @param region Код региона России
     * @return Орган выдающий документы
     */
    public Authority generateRandomAuthority(int region) {
        List<Authority> regionAuthorities = authorities.get(region);

        return regionAuthorities.get(random.nextInt(regionAuthorities.size()));
    }

    /**
     * Паспорт гражданина
     */
    public static class Passport {
        public final String series;
        public final String number;
        public final Authority authority;
        public final String date;

        public Passport(String series, String number, Authority authority, String date) {
            this.series = series;
            this.number = number;
            this.authority = authority;
            this.date = date;
        }
    }

    /**
     * Орган выдающий документы
     */
    public static class Authority {
        public final int region;
        public final int type;
        public final String code;
        public final String name;

        public Authority(String code, String name) {
            this.region = parseInt(code.substring(0, 2));
            this.type = parseInt(code.substring(2, 3));
            this.code = code;
            this.name = name;
        }
    }
}
