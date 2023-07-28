package com.krab51.webapp.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.System.getProperty;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.walk;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Вспомогательный класс для загрузки ресурсов
 */
public class ResourceLoader {
    private static final ClassLoader classLoader = getSystemClassLoader();

    /**
     * Рекурсивно загружает список файлов из указанной директории с необходимым расширением
     *
     * @param inputFolder Папка для загрузки спика файлов
     * @param extension   Расширение целевых файлов
     * @return Список файлов для обработки
     */
    public static List<Path> getInputFileList(String inputFolder, String extension) {
        try (Stream<Path> paths = walk(Paths.get(getProperty("user.dir").concat(inputFolder)))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().toLowerCase().endsWith("." + extension))
                    .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки списка файлов", e);
        }
    }

    /**
     * Загружает файл настроек
     *
     * @param filename Имя файла относительно исполняемой директории
     * @return Множество с параметрами настроек
     */
    public static Properties loadProperties(String filename) {
        Properties properties = new Properties();

        try (InputStream is = new FileInputStream(getProperty("user.dir").concat(filename))) {
            try (InputStreamReader isr = new InputStreamReader(is, UTF_8);
                 BufferedReader br = new BufferedReader(isr)) {
                properties.load(br);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла настроек", e);
        }

        return properties;
    }

    /**
     * Загружает содержимое файла в jar архиве как строку
     *
     * @param filename Имя файла относительно jar архива
     * @return Содержимое файла объединенное в строку
     */
    public static String getResourceAsString(String filename) {
        try (InputStream is = classLoader.getResourceAsStream(filename)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, UTF_8);
                 BufferedReader br = new BufferedReader(isr)) {
                return br.lines().collect(joining(lineSeparator()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла ресурсов", e);
        }
    }

    /**
     * Загружает содержимое файла в jar архиве как список
     *
     * @param filename Имя файла относительно jar архива
     * @return Содержимое файла объединенное построчно в список
     */
    public static List<String> getResourceAsList(String filename) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(requireNonNull(in), UTF_8))) {
            return br.lines().collect(toList());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла ресурсов " + filename, e);
        }
    }
}