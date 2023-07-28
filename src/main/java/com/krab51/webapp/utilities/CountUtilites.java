package com.krab51.webapp.utilities;

import java.math.BigDecimal;

import static java.lang.Integer.parseInt;
import static pl.allegro.finance.tradukisto.MoneyConverters.RUSSIAN_BANKING_MONEY_VALUE;
import static pl.allegro.finance.tradukisto.ValueConverters.RUSSIAN_INTEGER;

/**
 * Утилиты для представления чисел в текстовом формате
 */
public class CountUtilites {

    public static String priceAsWords(BigDecimal price) {
        String[] rawText = RUSSIAN_BANKING_MONEY_VALUE.asWords(price).split("/")[0].split("руб. ");

        int rub = price.intValue() % 10;
        int kop = parseInt(rawText[1]) % 10;

        return rawText[0]
                + (rub == 1 ? "рубль" : rub > 1 && rub < 5 ? "рубля" : "рублей")
                + " " + rawText[1] + " "
                + (kop == 1 ? "копейка" : kop > 1 && kop < 5 ? "копейки" : "копеек");
    }

    public static String numberAsWords(int number) {
        return RUSSIAN_INTEGER.asWords(number);
    }
}