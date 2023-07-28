package com.krab51.webapp.utilities.demo;

import org.jamel.dbf.DbfReader;
import org.jamel.dbf.exception.DbfException;
import org.jamel.dbf.processor.DbfRowProcessor;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.krab51.webapp.utilities.ResourceLoader.getResourceAsList;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

/**
 * Утилита для генерации корректных адресов России случайным образом
 */
public class CitizenAddressUtil {
    private static final Random random = new Random();
    private final Map<Integer, String> regions = new HashMap<>();
    private final Map<Integer, KladrObject> districts;
    private final Map<Integer, List<KladrObject>> index = new HashMap<>();

    public CitizenAddressUtil() {
        // Загружаем регионы
        requireNonNull(getResourceAsList("data/demo/address/regions.csv")).forEach(s -> {
            String[] values = s.split("\\|", -1);
            regions.put(parseInt(values[0]), values[1]);
        });

        // Загружаем справочник КЛАДР
        List<KladrObject> kladrObjects = new ArrayList<>();
        processDbf(new KladrProcessor(kladrObjects));

        // Создаем индексы значимости
        for (int i = 0; i < 3; i++) index.put(i, new ArrayList<>());

        // Проставляем названия регионов для городов и населенных пунктов КЛАДР
        kladrObjects.forEach(kladrObject -> {
            if (kladrObject.getOcatoCode() != null && kladrObject.getOcatoCode().length() == 11) {
                if (kladrObject.isCityOrLocality() && !kladrObject.isFederalCity()) {
                    kladrObject.regionName = regions.get(kladrObject.regionCode);

                    if (kladrObject.isCity()) index.get(1).add(kladrObject);
                    else if (kladrObject.isLocality()) index.get(2).add(kladrObject);
                } else if (kladrObject.isFederalCity()) {
                    index.get(0).add(kladrObject);
                }
            }
        });

        // Определяем что из объектов КЛАДР является районом
        districts = kladrObjects
                .stream()
                .filter(KladrObject::isDistrict)
                .collect(toMap(kladrObject -> kladrObject.regionDistrictCode, kladrObject -> kladrObject));

        // Для объектов не являющихся районами и городами федерального значения проставляем соответствующие районы
        kladrObjects.stream()
                .filter(KladrObject::isCityOrLocality)
                .forEach(kladrObject -> kladrObject.district = districts.get(kladrObject.regionDistrictCode));
    }

    /**
     * Генерирует случайным образом адрес гражданина
     *
     * @return Адрес гражданина на основе данных КЛАДР
     */
    public KladrObject generateRandomAddress() {
        int rnd = random.nextInt(10);
        List<KladrObject> targetList;

        if (rnd < 5) targetList = index.get(0);
        else if (rnd < 8) targetList = index.get(1);
        else targetList = index.get(2);

        return targetList.get(random.nextInt(targetList.size()));
    }

    private static void processDbf(DbfRowProcessor rowProcessor) throws DbfException {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("data/demo/address/kladr.dbf")) {
            try (DbfReader reader = new DbfReader(is)) {
                Object[] row;
                while ((row = reader.nextRecord()) != null) {
                    rowProcessor.processRow(row);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла адресов " + e);
        }
    }

    public static class KladrObject {
        private final String name;                       //  Наименование
        private final String shortObjectTypeName;        //  Сокращенное наименование типа объекта
        private final String code;                       //  Код СС РРР ГГГ ППП АА
        private final int regionCode;                    //  СС – код субъекта Российской Федерации (региона)
        private final int districtCode;                  //  РРР – код района;
        private final int regionDistrictCode;            //  Код региона и района для сопоставления районов
        private final int cityCode;                      //  ГГГ – код города;
        private final int localityCode;                  //  ППП – код населенного пункта;
        private final int actualCode;                    //  АА – признак актуальности
        private final int postalIndex;                   //  Почтовый индекс
        private final int ifnsCode;                      //  Код ИФНС
        private final int ifnsTerrytoryDeportamentCode;  //  Код территориального участка ИФНС
        private final String ocatoCode;                  //  Код ОКАТО
        private final int status;                        //  Статус объекта

        private String regionName;                       //  Название региона
        private KladrObject district;                    //  Район

        public KladrObject(
                String name,
                String shortObjectTypeName,
                String code,
                Integer postalIndex,
                Integer ifnsCode,
                Integer ifnsTerrytoryDeportamentCode,
                String ocatoCode,
                Integer status) {
            this.name = name;
            this.shortObjectTypeName = shortObjectTypeName;
            this.code = code;
            this.regionCode = parseInt(code.substring(0, 2));
            this.districtCode = parseInt(code.substring(2, 5));
            this.regionDistrictCode = parseInt(code.substring(0, 5));
            this.cityCode = parseInt(code.substring(5, 8));
            this.localityCode = parseInt(code.substring(8, 11));
            this.actualCode = parseInt(code.substring(11, 13));
            this.postalIndex = postalIndex;
            this.ifnsCode = ifnsCode;
            this.ifnsTerrytoryDeportamentCode = ifnsTerrytoryDeportamentCode;
            this.ocatoCode = ocatoCode;
            this.status = status;
        }

        public String getName() {
            return name == null ? "" : name.toUpperCase();
        }

        public int getRegionCode() {
            return regionCode;
        }

        public String getOcatoCode() {
            return ocatoCode == null ? "" : ocatoCode;
        }

        public String getRegionName() {
            return regionName == null ? "" : regionName.toUpperCase();
        }

        public String getDistrictName() {
            return district == null ? "" : district.name.toUpperCase();
        }

        public boolean isFederalCity() {
            return isRegion() && shortObjectTypeName.equals("г");
        }

        public boolean isRegion() {
            return districtCode == 0 && isHasRegionButNotCityOrLocality();
        }

        public boolean isDistrict() {
            return districtCode != 0 && isHasRegionButNotCityOrLocality();
        }

        public boolean isHasRegionButNotCityOrLocality() {
            return regionCode != 0 && !isCityOrLocality();
        }

        public boolean isCityOrLocality() {
            return isCity() || isLocality();
        }

        public boolean isCity() {
            return cityCode != 0 && localityCode == 0;
        }

        public boolean isLocality() {
            return localityCode != 0;
        }
    }

    private static class KladrProcessor implements DbfRowProcessor {
        private final List<KladrObject> kladrObjects;

        public KladrProcessor(List<KladrObject> kladrObjects) {
            this.kladrObjects = kladrObjects;
        }

        String getString(Object col) throws UnsupportedEncodingException {
            return new String((byte[]) col, "CP866").trim();
        }

        int getIntFromString(Object col) throws UnsupportedEncodingException {
            String value = getString(col);

            return value.isEmpty() ? 0 : parseInt(value);
        }

        @Override
        public void processRow(Object[] row) {
            KladrObject result = null;

            try {
                result = new KladrObject(
                        getString(row[0]),
                        getString(row[1]),
                        getString(row[2]),
                        getIntFromString(row[3]),
                        getIntFromString(row[4]),
                        getIntFromString(row[5]),
                        getString(row[6]),
                        getIntFromString(row[7])
                );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (result != null && result.actualCode == 0 && result.regionCode != 0 && result.regionCode <= 92)
                kladrObjects.add(result);
        }
    }
}