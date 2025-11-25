package com.epam.junit.serenity.UITests;

import modals.GenericGoogleTestData;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.TestDataProvider;

import java.util.stream.Stream;

@ExtendWith(SerenityJUnit5Extension.class)
public class GoogleSearchTest extends BaseTest{

    private GenericGoogleTestData getTestData(int index) {
        return TestDataProvider.getTestData("googleSearchTestData", GenericGoogleTestData[].class)[index];
    }

    private static Stream<Object[]> getParameterizedTestData() {
        return TestDataProvider.getTestDataCollectionAsStream("googleSearchTestData", GenericGoogleTestData[].class);
    }


    @Test
    public void shouldTypeInSearchBox() {
        googleSearchSteps.openGoogleHomePage();
        googleSearchSteps.typeInSearchBox(getTestData(0).getSearchKeyword());
    }

    @Test
    public void should_type_and_enter_in_search_box() {
        googleSearchSteps.openGoogleHomePage();
        googleSearchSteps.typeAndEnter(getTestData(0).getSearchKeyword());
        // Add assertions as needed
    }

    @Test
    public void should_type_and_tab_in_search_box() {
        googleSearchSteps.openGoogleHomePage();
        googleSearchSteps.typeAndTab(getTestData(0).getSearchKeyword());
        // Add assertions as needed
    }

    @ParameterizedTest
    @MethodSource("getParameterizedTestData")
    public void should_click_search_button(GenericGoogleTestData testData) {
        googleSearchSteps.openGoogleHomePage();
        googleSearchSteps.typeInSearchBox(testData.getSearchKeyword());
        googleSearchSteps.clickSearchButton();
        // Add assertions as needed
    }
}
