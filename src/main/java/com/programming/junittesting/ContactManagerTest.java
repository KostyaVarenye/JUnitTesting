package com.programming.junittesting;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// tests should start with @Test
/* Test cycle in JUnit5 looks like this:
* @BeforeAll - methods with this annotation executed before any of the rest, should be marked as static = USED FOR INIT TASKS
* @BeforeEach - runs before each test annotated with this = USED FOR INIT TASKS
* @Test - the tests
* @AfterEach - runs after each test = USED FOR CLEANUP TASKS
* @AfterAll - runs after all of the tests = USED FOR CLEANUP TASKS
* */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContactManagerTest {
    // public static can be omitted in @BeforeAll & @AfterAll if @TestInstance() annotation is added but in this case JUnit will instantiate the class only once
    ContactManager contactManager;

    //    @BeforeAll
    //    public static void setupAll(){
    //        System.out.println("Should Print Before All Tests");
    //    }
    @BeforeAll
    public void setupAll(){
        System.out.println("Should Print Before All Tests");
    }

    //new copy of ContactManager will be created with each test
    @BeforeEach
    public void setup(){
        contactManager = new ContactManager();

    }
    // function for duplicate code of onlyWindows/onlyMac
    public void shouldCreate(){
        contactManager.addContact("John", "Doe", "0123456789");
        // test if the method is not empty, if contact created
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        // expected to have exactly 1 contact
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
        // find if was created successfully and matching
        Assertions.assertTrue(contactManager.getAllContacts().stream()
                .anyMatch(contact -> contact.getFirstName().equals("John") &&
                        contact.getLastName().equals("Doe") &&
                        contact.getPhoneNumber().equals("0123456789")));
    }
    @Test
    @DisplayName("Should Create Contact Only on WINDOWS")
    @EnabledOnOs(value = OS.WINDOWS, disabledReason = "Enabled only on WINDOWS OS")
    public void shouldCreateContactOnlyOnWindows(){
        shouldCreate();
    }

    @Test
    @DisplayName("Should Not Create Contact When First Name is Null")
    public void shouldThrowRuntimeExceptionWhenFirstNameIsNull() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact(null, "Doe", "0123456789");
        });
    }

    @Test
    @DisplayName("Should Not Create Contact When Last Name is Null")
    public void shouldThrowRuntimeExceptionWhenLastNameIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact("Joe", null, "0123456789");
        });
    }

    @Test
    @DisplayName("Should Not Create Contact When Phone Number is Null")
    public void shouldThrowRuntimeExceptionWhenPhoneNumberIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact("Joe", "Doe", null);
        });
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Should Execute After Each Test");
    }

    @AfterAll
    public void tearDownAll() {
        System.out.println("Should be executed at the end of the @Test");
    }

    @Test
    @DisplayName("Should Create Contact Only on MAC OS")
    @EnabledOnOs(value = OS.MAC, disabledReason = "Enabled only on MacOS")
    public void shouldCreateContactOnlyOnMac(){
        shouldCreate();
    }

    //assumption that were testing on DEV with parameter passed in the Edit Configurations if fails, the tests won't fail
    @Test
    @DisplayName("Test Contact Creation on Developer Machine")
    public void shouldTestContactCreationOnDEV() {
        Assumptions.assumeTrue("DEV".equals(System.getProperty("ENV")));
        contactManager.addContact("John", "Doe", "0123456789");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());

    }
    //@Test is missing because its a repeated test, assigned value = times to repeat, name = to write in tests with auto parameters of current and total reps

    @RepeatedTest(value = 5,
            name= "Repeating Contact Creation Test {currentRepetition} of {totalRepetitions}")
    @DisplayName("Should Create Contact 5 Times")
    public void shouldCreateContactFiveTimes(){
        contactManager.addContact("John", "Doe", "0123456789");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    /* Tests with parameters. substitutes @Test
    * Data is provided using @ValueSource - Strings, Ints, Longs, Shorts, Floats, Doubles, Bytes, Chars, classes.
    * @CsvSource */

    @ParameterizedTest
    @ValueSource(strings = {"0123456789", "0123756789", "0173456789" })
    @DisplayName("Should Create Contact and Test 3 Different Strings with ValueSource")
    public void shouldTestContactCreationUsingValueSource( String phoneNumber){
        contactManager.addContact("John", "Doe", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    private static List<String> phoneNumberList() {
        return Arrays.asList("0123456789", "0223756789", "0373456789");
    }
    // Method source, injecting a list of phoneNumbers ^
    @ParameterizedTest
    @DisplayName("Test MethodSource")
    @MethodSource("phoneNumberList")
    public void shouldTestContactCreationUsingMethodSource( String phoneNumber){
        contactManager.addContact("John", "Doe", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    @ParameterizedTest
    @DisplayName("Test CsvSource - Number Should Match the Required Format")
    @CsvSource({"0123456789", "0223756789", "0373456789"})
    public void shouldTestContactCreationUsingCsvSource(String phoneNumber){
        contactManager.addContact("John", "Doe", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    @ParameterizedTest
    @DisplayName("Test CsvFileSource - Number Should Match the Required Format From CSV")
    @CsvFileSource(resources = "/data.csv")
    public void shouldTestContactCreationUsingCsvFileSource(String phoneNumber){
        contactManager.addContact("John", "Doe", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

}