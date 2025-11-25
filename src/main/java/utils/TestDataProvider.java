package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import helpers.EnvironmentHelper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestDataProvider {
    /**
     * Gets the test data from the resource folder.
     *
     * @param testDataId test data Id, file name without extension (.json)
     * @param clazz
     * @param <T>
     * @return
     */
    public synchronized static <T> T getTestData(String testDataId, Class<T> clazz) {
        String json = getJson(testDataId);
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, typeOfT, context) -> LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).create().fromJson(json, clazz);
    }


    /**
     * Gets the test data from the JSON file from resource folder
     * File should contain an array
     * returns parameters for parameterized test execution
     *
     * @param <T>
     * @param testDataId test data Id, file name without extension (.json)
     * @param clazz
     * @return
     */
    public synchronized static <T> Collection<Object[]> getTestDataCollection(String testDataId, Class<T[]> clazz) {
        T[] testDataArray = TestDataProvider.getTestData(testDataId, clazz);
        return Arrays.asList(testDataArray).stream().map(testDataExample -> new Object[]{testDataExample}).collect(Collectors.toList());
    }

    /**
     * Gets the test data from the JSON file from resource folder
     * File should contain an array
     * returns parameters for parameterized test execution
     *
     * @param <T>
     * @param testDataId test data Id, file name without extension (.json)
     * @param clazz
     * @return
     */
    public synchronized static <T> Stream<T> getTestDataStream(String testDataId, Class<T[]> clazz) {
        T[] testDataArray = TestDataProvider.getTestData(testDataId, clazz);
        return Arrays.stream(testDataArray);
    }

    public synchronized static <T> Stream<Object[]> getTestDataCollectionAsStream(String testDataId, Class<T[]> clazz) {
        T[] testDataArray = TestDataProvider.getTestData(testDataId, clazz);
        return Arrays.asList(testDataArray).stream().map(testDataExample -> new Object[]{testDataExample}).collect(Collectors.toList()).stream();
    }

    public synchronized static <T> Stream<Object[]> getTestDataAsStreamObject(String testDataId, Class<T[]> clazz) {
        T[] testDataArray = TestDataProvider.getTestDataInLocalDateFormat(testDataId, clazz);
        return Arrays.stream(testDataArray).map(testDataExample -> new Object[]{testDataExample}).collect(Collectors.toList()).stream();
    }

    /**
     * Transforms plain array of elements to Collection<Object[]>
     * returns parameters for parameterized test execution
     *
     * @param <T>
     * @return
     */
    public synchronized static <T> Collection<Object[]> getTestDataCollection(T... testDataItems) {
        return Arrays.asList(testDataItems).stream().map(testDataExample -> new Object[]{testDataExample}).collect(Collectors.toList());
    }

    /**
     * Transforms plain array of elements to Collection<Object[]>
     * returns parameters for parameterized test execution
     *
     * @param <T>
     * @return
     */
    public synchronized static <T, V> Collection<Object[]> getTestDataCollection(List<T> testDataItemsForParameter1, List<V> testDataItemsForParameter2) {
        return IntStream.range(0, Math.min(testDataItemsForParameter1.size(), testDataItemsForParameter2.size())).mapToObj(index -> new Object[]{testDataItemsForParameter1.get(index), testDataItemsForParameter2.get(index)}).collect(Collectors.toList());
    }

    /**
     * Gets the test data from the JSON file from resource folder
     * File should contain an array
     * returns
     * parameters for parameterized test execution
     * two parameters for one test execution
     * first parameter is taken from json file with name testDataIdFirFirstParameter
     * second parameter is taken from list valuesForSecondParameter
     * Count of elements in the test data collection equals to count from second parameter
     *
     * @param testDataIdFirFirstParameter test data Id, file name without extension (.json)
     * @param clazzForFirstParameter      class of first parameter
     * @param valuesForSecondParameter    list of values for second parameter
     * @return
     */
    public synchronized static <T, V> Collection<Object[]> getTestDataCollection(String testDataIdFirFirstParameter, Class<T[]> clazzForFirstParameter, List<V> valuesForSecondParameter) {
        T[] testDataArray = TestDataProvider.getTestData(testDataIdFirFirstParameter, clazzForFirstParameter);
        int itemsCount = valuesForSecondParameter.size();
        return IntStream.range(0, itemsCount).mapToObj(index -> {
            int indexForFirstParameter = index % testDataArray.length;
            int indexForSecondParameter = index % valuesForSecondParameter.size();
            return new Object[]{testDataArray[indexForFirstParameter], valuesForSecondParameter.get(indexForSecondParameter)};
        }).collect(Collectors.toList());
    }

    public synchronized static <T> T getTestData(String testDataId, Class<T> clazz, boolean isAPI) {
        String json = getJsonAPI(testDataId);
        return new Gson().fromJson(json, clazz);
    }

    @SneakyThrows
    private static String getJson(String testDataId) {
        String environment = EnvironmentHelper.getEnvironment();
        String folder = environment.toLowerCase(Locale.ROOT).contains("uat") ? "uat" : environment.toLowerCase(Locale.ROOT).contains("prod") ? "prod" : "integ";

        String fileName = "testdata/" + folder + "/" + testDataId + ".json";
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is); BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    @SneakyThrows
    private static String getJsonAPI(String testDataId) {
        String fileName = "testdata/".concat(testDataId).concat(".json");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is); BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static <T> T getTestDataInLocalDateFormat(String testDataId, Class<T> clazz) {
        String json = getJson(testDataId);
        return new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<Object>) (jsonElement, typeOfT, context) -> LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))).create().fromJson(json, clazz);
    }

    public synchronized static <T> Stream<Object[]> getRandomTestData(String testDataId, Class<T[]> clazz, long limit) {
        T[] testDataArray = TestDataProvider.getTestData(testDataId, clazz);
        List<T> dataAsList = new ArrayList<>(Arrays.asList(testDataArray));
        Collections.shuffle(dataAsList);
        return dataAsList.stream().limit(limit).map(data -> new Object[]{data});
    }

    public synchronized static <T> T getOneToMTestData(String testDataId, Class<T> clazz) {
        String json = getJson(testDataId);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, context) -> {
            String dateStr = jsonElement.getAsString();
            List<DateTimeFormatter> formatters = Arrays.asList(DateTimeFormatter.ISO_DATE_TIME, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(dateStr, formatter);
                } catch (Exception ignored) {
                }
            }
            throw new JsonParseException("Unsupported date format " + dateStr);
        }).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, context) -> {
            String dateStr = jsonElement.getAsString();
            List<DateTimeFormatter> formatters = Arrays.asList(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (Exception ignored) {
                }
            }
            throw new JsonParseException("Unsupported date format " + dateStr);
        }).create();
        return gson.fromJson(json, clazz);
    }
}
