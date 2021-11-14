package com.programming.junittesting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// tests should start with @Test
class ContactManagerTest {
    @Test
    public void shouldCreateContact(){
        ContactManager contactManager = new ContactManager();
        contactManager.addContact("John", "Doe", "0123456789");
        // test if the method is not empty, if contact created
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        // expected to have exactly 1 contact
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
    }
}