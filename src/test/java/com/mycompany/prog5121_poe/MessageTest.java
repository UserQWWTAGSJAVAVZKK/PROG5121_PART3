/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121_poe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {

    private Message first;
    private Message second;
    private Message third;
    private Message fourth;
    private Message fifth;

    // Prepare the test messages before every test.
    @BeforeEach
    public void prepareMessages() {

        Message.resetArrays();

        // Test Message 1: Sent.
        first = new Message(
                1, "Developer", "+27834557896",
                "Did you get the cake?");

        // Test Message 2: Stored.
        second = new Message(
                2, "Developer", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        // Test Message 3: Disregarded.
        third = new Message(
                3, "Developer", "+27834484567",
                "Yohoooo, I am at your gate.");

        // Test Message 4: Sent.
        fourth = new Message(
                4, "0838884567", "+27834557896",
                "It is dinner time!");

        // Test Message 5: Stored.
        fifth = new Message(
                5, "Developer", "+27838884567",
                "Ok, I am leaving without you.");

        // Place each message into its correct array.
        first.SentMessage("send");
        second.SentMessage("store");
        third.SentMessage("disregard");
        fourth.SentMessage("send");
        fifth.SentMessage("store");
    }

    // Test 1: Check the sent messages array.
    @Test
    public void sentMessagesArrayContainsExpectedMessages() {

        String expected = "Did you get the cake?\n"
                + "It is dinner time!\n";

        assertEquals(expected, Message.printMessages());
        assertEquals(2, Message.getSentCount());
    }

    // Test 2: Check that the longest stored message is found.
    @Test
    public void longestStoredMessageIsDisplayed() {

        String expected = "Where are you? You are late! "
                + "I have asked you to be on time.";

        assertEquals(expected, Message.findLongestStoredMessage());
    }

    // Test 3: Check that searching finds the test message.
    @Test
    public void searchUsingMessageIdentifierFindsMessage() {

        String result = Message.searchMessageID("0838884567");

        assertTrue(result.contains("It is dinner time!"));
    }

    // Test 4: Check that all messages for a recipient are found.
    @Test
    public void recipientSearchReturnsAllMatchingMessages() {

        String result = Message.searchRecipient("+27838884567");

        assertTrue(result.contains(
                "Where are you? You are late! I have asked you to be on time."));

        assertTrue(result.contains(
                "Ok, I am leaving without you."));
    }

    // Test 5: Check that a stored message can be deleted using its hash.
    @Test
    public void messageCanBeDeletedUsingItsHash() {

        String hash = second.getMessageHash();

        String result = Message.deleteMessage(hash);

        assertEquals("Message successfully deleted.", result);

        assertFalse(Message.createReport().contains(
                "Where are you? You are late!"));
    }

    // Test 6: Check that the report contains stored message details.
    @Test
    public void reportDisplaysStoredMessageInformation() {

        String report = Message.createReport();

        assertTrue(report.contains(fifth.getMessageHash()));
        assertTrue(report.contains("+27838884567"));

        assertTrue(report.contains(
                "Ok, I am leaving without you."));

        assertTrue(report.contains(
                "Where are you? You are late! I have asked you to be on time."));
    }

    // Test 7: Check the disregarded messages array.
    @Test
    public void disregardedMessageIsStoredInCorrectArray() {

        assertEquals(1, Message.getDisregardedCount());
    }

    // Test 8: Check the stored messages array.
    @Test
    public void storedMessagesArrayContainsTwoMessages() {

        assertEquals(2, Message.getStoredCount());
    }
}
