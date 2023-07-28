package com.krab51.webapp.utilites;

import com.krab51.webapp.utilities.CountUtilites;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.krab51.webapp.utilities.CountUtilites.numberAsWords;
import static com.krab51.webapp.utilities.CountUtilites.priceAsWords;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = CountUtilites.class)
public class CountUtilitesTests {

    @Test
    void priceAsWords_PositiveTest() {
        assertEquals("сто рублей 10 копеек", priceAsWords(new BigDecimal("100.10")));
        assertEquals("сто один рубль 01 копейка", priceAsWords(new BigDecimal("101.01")));
        assertEquals("сто три рубля 03 копейки", priceAsWords(new BigDecimal("103.03")));
        assertEquals("сто шесть рублей 96 копеек", priceAsWords(new BigDecimal("106.96")));
        assertEquals("сто двадцать три тысячи четыреста пятьдесят шесть рублей 00 копеек", priceAsWords(new BigDecimal("123456")));
        assertEquals("сто одна тысяча четыреста пятьдесят шесть рублей 00 копеек", priceAsWords(new BigDecimal("101456")));
        assertEquals("сто пять тысяч четыреста пятьдесят шесть рублей 00 копеек", priceAsWords(new BigDecimal("105456")));
    }

    @Test
    void priceAsWords_NegativeTest() {
        BigDecimal testData1 = new BigDecimal("100.10");
        assertNotEquals("сто рубль 10 копеек", priceAsWords(testData1));
        assertNotEquals("сто рубля 10 копеек", priceAsWords(testData1));
        assertNotEquals("сто рублей 10 копейка", priceAsWords(testData1));
        assertNotEquals("сто рублей 10 копейки", priceAsWords(testData1));

        BigDecimal testData2 = new BigDecimal("101.01");
        assertNotEquals("сто один рубля 01 копейка", priceAsWords(testData2));
        assertNotEquals("сто один рублей 01 копейка", priceAsWords(testData2));
        assertNotEquals("сто один рубль 01 копеек", priceAsWords(testData2));
        assertNotEquals("сто один рубль 01 копейки", priceAsWords(testData2));

        BigDecimal testData3 = new BigDecimal("103.03");
        assertNotEquals("сто три рублей 03 копейки", priceAsWords(testData3));
        assertNotEquals("сто три рубль 03 копейки", priceAsWords(testData3));
        assertNotEquals("сто три рубля 03 копейка", priceAsWords(testData3));
        assertNotEquals("сто три рубля 03 копеек", priceAsWords(testData3));

        BigDecimal testData4 = new BigDecimal("106.96");
        assertNotEquals("сто шесть рубль 96 копеек", priceAsWords(testData4));
        assertNotEquals("сто шесть рубля 96 копеек", priceAsWords(testData4));
        assertNotEquals("сто шесть рублей 96 копейка", priceAsWords(testData4));
        assertNotEquals("сто шесть рублей 96 копейки", priceAsWords(testData4));

        BigDecimal testData5 = new BigDecimal("123456");
        assertNotEquals("сто двадцать три тысяча четыреста пятьдесят шесть рублей 00 копеек", priceAsWords(testData5));
        assertNotEquals("сто двадцать три тысяч четыреста пятьдесят шесть рублей 00 копеек", priceAsWords(testData5));
    }

    @Test
    void numberAsWords_PositiveTest() {
        assertEquals("сто двадцать три тысячи четыреста пятьдесят шесть", numberAsWords(123456));
    }

    @Test
    void numberAsWords_NegativeTest() {
        assertNotEquals("один миллион двести тридцать четыре тысячи пятьсот шестьдесят", numberAsWords(123456));
        assertNotEquals("двенадцать тысяч триста сорок пять", numberAsWords(123456));
    }
}