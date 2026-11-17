

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Student
 */


package com.mycompany.prog5121_poe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    // Message information.
    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // Maximum number of messages allowed in each array.
    private static final int MAX_MESSAGES = 1000;

    // Arrays for the different message statuses.
    public static Message[] sentMessages = new Message[MAX_MESSAGES];
    public static Message[] storedMessages = new Message[MAX_MESSAGES];
    public static Message[] disregardedMessages = new Message[MAX_MESSAGES];

    // Arrays containing the IDs and hashes of processed messages.
    public static String[] messageIDs = new String[MAX_MESSAGES];
    public static String[] messageHashes = new String[MAX_MESSAGES];

    // Counters for each array.
    private static int sentCount = 0;
    private static int storedCount = 0;
    private static int disregardedCount = 0;
    private static int allCount = 0;

    // JSON file used to save stored messages.
    private static final String FILE_NAME = "messages.json";

    // Gson handles converting Java messages into JSON and back.
    private static final Gson gson =
            new GsonBuilder().setPrettyPrinting().create();

    // Constructor creates a message and generates its ID and hash.
    public Message(int messageNumber, String sender,
                   String recipient, String messageText) {

        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageID = generateID();
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // Generate a random 10-digit message ID.
    private String generateID() {

        Random random = new Random();

        long id = 1000000000L
                + (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(id);
    }

    // Check that the message ID has 10 digits.
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    // Check that the recipient has the correct South African format.
    public String checkRecipientCell() {

        if (recipient != null && recipient.matches("\\+27\\d{9}")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted.";
    }

    // Check that the message is not empty and does not exceed 250 characters.
    public String checkMessageLength() {

        if (messageText != null
                && !messageText.trim().isEmpty()
                && messageText.length() <= 250) {

            return "Message ready to send.";
        }

        return "Message is too long or empty. "
                + "Please enter a message of up to 250 characters.";
    }

    // Create a hash using the first two ID digits,
    // message number, first word and last word.
    public String createMessageHash() {

        if (messageText == null || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord.toUpperCase()
                + ":" + lastWord.toUpperCase();
    }

    // Decide whether to send, store or disregard the message.
    public String SentMessage(String choice) {

        // Validate the message before processing it.
        if (!checkMessageID()
                || !checkRecipientCell().equals(
                        "Cell phone number successfully captured.")
                || !checkMessageLength().equals("Message ready to send.")) {

            return "Message could not be processed. Check the details.";
        }

        // Send the message.
        if (choice.equalsIgnoreCase("send")) {

            if (sentCount >= MAX_MESSAGES) {
                return "Sent messages array is full.";
            }

            status = "Sent";

            sentMessages[sentCount] = this;
            sentCount++;

            addToDetails();

            return "Message successfully sent.";
        }

        // Store the message.
        if (choice.equalsIgnoreCase("store")) {

            if (storedCount >= MAX_MESSAGES) {
                return "Stored messages array is full.";
            }

            status = "Stored";

            storedMessages[storedCount] = this;
            storedCount++;

            addToDetails();

            // Save the updated stored messages array to JSON.
            if (!saveStoredMessages()) {

                storedCount--;
                storedMessages[storedCount] = null;

                return "Message could not be saved.";
            }

            return "Message successfully stored in memory.";
        }

        // Disregard the message.
        if (choice.equalsIgnoreCase("disregard")) {

            if (disregardedCount >= MAX_MESSAGES) {
                return "Disregarded messages array is full.";
            }

            status = "Disregarded";

            disregardedMessages[disregardedCount] = this;
            disregardedCount++;

            addToDetails();

            return "Message disregarded.";
        }

        return "Invalid message option.";
    }

    // Add the message ID and hash to the details arrays.
    private void addToDetails() {

        if (allCount < MAX_MESSAGES) {

            messageIDs[allCount] = messageID;
            messageHashes[allCount] = messageHash;

            allCount++;
        }
    }

    // Save stored messages into the JSON file.
    public static boolean saveStoredMessages() {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            List<Message> messages = new ArrayList<>();

            // Copy messages from the array into a list.
            for (int i = 0; i < storedCount; i++) {
                messages.add(storedMessages[i]);
            }

            // Convert the list into JSON.
            gson.toJson(messages, writer);

            return true;

        } catch (IOException exception) {

            System.out.println("Could not save stored messages.");
            return false;
        }
    }

    // Load stored messages from the JSON file when the program starts.
    public static void loadStoredMessages() {

        try (FileReader reader = new FileReader(FILE_NAME)) {

            Type listType =
                    new TypeToken<ArrayList<Message>>() {}.getType();

            List<Message> messages = gson.fromJson(reader, listType);

            // Clear the arrays before loading the saved data.
            resetArrays();

            if (messages != null) {

                for (Message message : messages) {

                    if (storedCount < MAX_MESSAGES) {

                        storedMessages[storedCount] = message;
                        storedCount++;

                        messageIDs[allCount] = message.messageID;
                        messageHashes[allCount] = message.messageHash;
                        allCount++;
                    }
                }
            }

        } catch (IOException exception) {

            // If no JSON file exists yet, start with empty arrays.
            resetArrays();
        }
    }

    // Display the sender and recipient of all stored messages.
    public static String displayStoredSendersAndRecipients() {

        String result = "";

        for (int i = 0; i < storedCount; i++) {

            result += "Sender: " + storedMessages[i].sender
                    + "\nRecipient: " + storedMessages[i].recipient
                    + "\n\n";
        }

        if (result.isEmpty()) {
            return "There are no stored messages.";
        }

        return result;
    }

    // Find the longest message in the stored messages array.
    public static String findLongestStoredMessage() {

        String longestMessage = "";

        for (int i = 0; i < storedCount; i++) {

            if (storedMessages[i].messageText.length()
                    > longestMessage.length()) {

                longestMessage = storedMessages[i].messageText;
            }
        }

        return longestMessage;
    }

    // Search messages by generated message ID.
    // The sender search also supports the supplied developer test value.
    public static String searchMessageID(String searchValue) {

        for (int i = 0; i < storedCount; i++) {

            Message message = storedMessages[i];

            if (message.messageID.equals(searchValue)
                    || message.sender.equals(searchValue)) {

                return "Recipient: " + message.recipient
                        + "\nMessage: " + message.messageText;
            }
        }

        for (int i = 0; i < sentCount; i++) {

            Message message = sentMessages[i];

            if (message.messageID.equals(searchValue)
                    || message.sender.equals(searchValue)) {

                return "Recipient: " + message.recipient
                        + "\nMessage: " + message.messageText;
            }
        }

        return "Message not found.";
    }

    // Search for all sent and stored messages belonging to a recipient.
    public static String searchRecipient(String recipient) {

        String result = "";

        // Search stored messages.
        for (int i = 0; i < storedCount; i++) {

            if (storedMessages[i].recipient.equals(recipient)) {
                result += storedMessages[i].messageText + "\n";
            }
        }

        // Search sent messages.
        for (int i = 0; i < sentCount; i++) {

            if (sentMessages[i].recipient.equals(recipient)) {
                result += sentMessages[i].messageText + "\n";
            }
        }

        if (result.isEmpty()) {
            return "No messages found for this recipient.";
        }

        return result;
    }

    // Delete a stored message by using its message hash.
    public static String deleteMessage(String hash) {

        for (int i = 0; i < storedCount; i++) {

            if (storedMessages[i].messageHash.equals(hash)) {

                // Move later messages one position to the left.
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                }

                // Remove the duplicate last entry.
                storedMessages[storedCount - 1] = null;
                storedCount--;

                // Rebuild the ID and hash arrays after deletion.
                rebuildDetails();

                // Save the updated messages.
                if (!saveStoredMessages()) {
                    return "Message deleted in memory, but could not update JSON.";
                }

                return "Message successfully deleted.";
            }
        }

        return "Message not found.";
    }

    // Rebuild the ID and hash arrays using all current messages.
    private static void rebuildDetails() {

        messageIDs = new String[MAX_MESSAGES];
        messageHashes = new String[MAX_MESSAGES];
        allCount = 0;

        for (int i = 0; i < sentCount; i++) {
            addDetailsFromMessage(sentMessages[i]);
        }

        for (int i = 0; i < storedCount; i++) {
            addDetailsFromMessage(storedMessages[i]);
        }

        for (int i = 0; i < disregardedCount; i++) {
            addDetailsFromMessage(disregardedMessages[i]);
        }
    }

    // Add one message's ID and hash to the details arrays.
    private static void addDetailsFromMessage(Message message) {

        if (allCount < MAX_MESSAGES) {
            messageIDs[allCount] = message.messageID;
            messageHashes[allCount] = message.messageHash;
            allCount++;
        }
    }

    // Display a report of all stored messages and their details.
    public static String createReport() {

        String report = "";

        for (int i = 0; i < storedCount; i++) {

            Message message = storedMessages[i];

            report += "Message ID: " + message.messageID
                    + "\nMessage Hash: " + message.messageHash
                    + "\nSender: " + message.sender
                    + "\nRecipient: " + message.recipient
                    + "\nMessage: " + message.messageText
                    + "\nStatus: " + message.status
                    + "\n-------------------------\n";
        }

        if (report.isEmpty()) {
            return "There are no stored messages.";
        }

        return report;
    }

    // Display all sent message text.
    public static String printMessages() {

        String result = "";

        for (int i = 0; i < sentCount; i++) {
            result += sentMessages[i].messageText + "\n";
        }

        return result;
    }

    // Return the total number of sent messages.
    public static int returnTotalMessages() {
        return sentCount;
    }

    // Reset arrays and counters.
    public static void resetArrays() {

        sentMessages = new Message[MAX_MESSAGES];
        storedMessages = new Message[MAX_MESSAGES];
        disregardedMessages = new Message[MAX_MESSAGES];

        messageIDs = new String[MAX_MESSAGES];
        messageHashes = new String[MAX_MESSAGES];

        sentCount = 0;
        storedCount = 0;
        disregardedCount = 0;
        allCount = 0;
    }

    // Getter methods allow other classes to access private information.
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getStatus() {
        return status;
    }

    public static int getStoredCount() {
        return storedCount;
    }

    public static int getDisregardedCount() {
        return disregardedCount;
    }

    public static int getSentCount() {
        return sentCount;
    }
}


