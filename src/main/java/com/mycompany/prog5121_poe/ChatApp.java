
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121_poe;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class ChatApp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ================= REGISTRATION =================

        System.out.println("==============================");
        System.out.println("    QUICKCHAT REGISTRATION");
        System.out.println("==============================");

        System.out.print("Enter name: ");
        String name = input.nextLine();

        System.out.print("Enter surname: ");
        String surname = input.nextLine();

        System.out.print("Enter username: ");
        String userName = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cellphone number (+27718693002): ");
        String cellphone = input.nextLine();

        // Create the user account.
        Login user = new Login(userName, password, cellphone);

        // Display registration validation results.
        System.out.println("\n========== REGISTRATION RESULTS ==========");
        System.out.println(user.getUserNameMessage());
        System.out.println(user.getPasswordMessage());
        System.out.println(user.getCellPhoneMessage());

        // Stop if registration details are invalid.
        if (!user.registrationSuccessful()) {

            System.out.println("\nRegistration failed.");
            System.out.println("Please correct your details and try again.");

            input.close();
            return;
        }

        System.out.println("\nRegistration successful!");

        // ================= LOGIN =================

        System.out.println("\n========== QUICKCHAT LOGIN ==========");

        System.out.print("Enter username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Enter password: ");
        String enteredPassword = input.nextLine();

        // Check the login details.
        boolean loginSuccessful = user.loginUser(
                enteredUsername, enteredPassword);

        System.out.println(user.returnLoginStatus(loginSuccessful));

        // Only logged-in users can access QuickChat.
        if (!loginSuccessful) {
            input.close();
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        // Load messages previously saved in the JSON file.
        Message.loadStoredMessages();

        // ================= MESSAGE LIMIT =================

        int messageLimit;

        // Ask how many messages the user wants to process.
        do {

            System.out.print(
                    "How many messages would you like to enter? ");

            // Check that the input is a whole number.
            while (!input.hasNextInt()) {

                System.out.println("Please enter a whole number.");
                input.nextLine();

                System.out.print("Enter the number of messages: ");
            }

            messageLimit = input.nextInt();

            // Clear the newline left behind by nextInt().
            input.nextLine();

            if (messageLimit < 0) {
                System.out.println(
                        "Please enter zero or a positive number.");
            }

        } while (messageLimit < 0);

        // Tracks how many messages have been processed.
        int messagesProcessed = 0;

        // Controls whether the main menu continues.
        boolean running = true;

        // ================= MAIN MENU =================

        do {

            System.out.println("\n========== QUICKCHAT MENU ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option: ");

            String menuChoice = input.nextLine();

            switch (menuChoice) {

                // ============ OPTION 1: SEND MESSAGES ============

                case "1":

                    if (messagesProcessed >= messageLimit) {
                        System.out.println(
                                "You have reached your message limit.");
                        break;
                    }

                    // Continue until the requested number is processed.
                    while (messagesProcessed < messageLimit) {

                        System.out.println("\nMessage "
                                + (messagesProcessed + 1)
                                + " of " + messageLimit);

                        // Ask for the recipient.
                        System.out.print(
                                "Enter recipient number (+27...): ");
                        String recipient = input.nextLine();

                        // Ask for the message.
                        System.out.print("Enter message: ");
                        String messageText = input.nextLine();

                        // Create the message.
                        Message message = new Message(
                                messagesProcessed + 1,
                                cellphone,
                                recipient,
                                messageText);

                        // Validate the recipient number.
                        if (!message.checkRecipientCell().equals(
                                "Cell phone number successfully captured.")) {

                            System.out.println(
                                    message.checkRecipientCell());
                            System.out.println(
                                    "Please enter this message again.");

                            continue;
                        }

                        // Validate the message length.
                        if (!message.checkMessageLength().equals(
                                "Message ready to send.")) {

                            System.out.println(
                                    message.checkMessageLength());
                            System.out.println(
                                    "Please enter this message again.");

                            continue;
                        }

                        // Display the message ID and hash.
                        System.out.println("\nMessage ID: "
                                + message.getMessageID());

                        System.out.println("Message Hash: "
                                + message.getMessageHash());

                        // Ask the user what to do with the message.
                        String action;

                        do {

                            System.out.println("\n1. Send Message");
                            System.out.println("2. Disregard Message");
                            System.out.println("3. Store Message");
                            System.out.print("Choose an option: ");

                            action = input.nextLine();

                            if (!action.equals("1")
                                    && !action.equals("2")
                                    && !action.equals("3")) {

                                System.out.println(
                                        "Invalid choice. Please try again.");
                            }

                        } while (!action.equals("1")
                                && !action.equals("2")
                                && !action.equals("3"));

                        // Process the user's decision.
                        String result;

                        if (action.equals("1")) {
                            result = message.SentMessage("send");

                        } else if (action.equals("2")) {
                            result = message.SentMessage("disregard");

                        } else {
                            result = message.SentMessage("store");
                        }

                        System.out.println(result);

                        // Display details when the message was sent.
                        if (action.equals("1")
                                && result.equals(
                                        "Message successfully sent.")) {

                            System.out.println("\nMessage details:");

                            System.out.println("Message ID: "
                                    + message.getMessageID());

                            System.out.println("Message Hash: "
                                    + message.getMessageHash());

                            System.out.println("Recipient: "
                                    + message.getRecipient());

                            System.out.println("Message: "
                                    + message.getMessageText());
                        }

                        // Increase the count only if the action succeeded.
                        if (result.equals("Message successfully sent.")
                                || result.equals("Message disregarded.")
                                || result.equals(
                                        "Message successfully stored in memory.")) {

                            messagesProcessed++;

                        } else {

                            // If processing failed, allow the user to try again.
                            System.out.println(
                                    "The message was not processed.");
                        }

                        System.out.println("\nMessages processed: "
                                + messagesProcessed
                                + " of " + messageLimit);
                    }

                    break;

                // ============ OPTION 2: RECENT MESSAGES ============

                case "2":

                    System.out.println("Coming Soon.");
                    break;

                // ============ OPTION 3: QUIT ============

                case "3":

                    System.out.println("\nTotal messages sent: "
                            + Message.returnTotalMessages());

                    System.out.println("Thank you for using QuickChat.");

                    running = false;
                    break;

                // ============ OPTION 4: STORED MESSAGES ============

                case "4":

                    storedMessagesMenu(input);
                    break;

                default:

                    System.out.println(
                            "Invalid option. Choose 1, 2, 3 or 4.");
            }

        } while (running);

        input.close();
    }

    // ================= STORED MESSAGES MENU =================

    public static void storedMessagesMenu(Scanner input) {

        boolean menuRunning = true;

        do {

            System.out.println("\n========== STORED MESSAGES ==========");
            System.out.println("1. Display senders and recipients");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search using message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete message using message hash");
            System.out.println("6. Display full stored messages report");
            System.out.println("7. Return to main menu");
            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {

                // Display sender and recipient details.
                case "1":

                    System.out.println(
                            Message.displayStoredSendersAndRecipients());
                    break;

                // Display the longest stored message.
                case "2":

                    System.out.println("\nLongest stored message:");

                    System.out.println(
                            Message.findLongestStoredMessage());
                    break;

                // Search for a message using its ID.
                case "3":

                    System.out.print("Enter message ID: ");
                    String messageID = input.nextLine();

                    System.out.println(
                            Message.searchMessageID(messageID));
                    break;

                // Search for messages belonging to a recipient.
                case "4":

                    System.out.print("Enter recipient number: ");
                    String recipient = input.nextLine();

                    System.out.println(
                            Message.searchRecipient(recipient));
                    break;

                // Delete a message using its hash.
                case "5":

                    System.out.print("Enter message hash: ");
                    String hash = input.nextLine();

                    System.out.println(
                            Message.deleteMessage(hash));
                    break;

                // Display all stored message details.
                case "6":

                    System.out.println("\n========== MESSAGE REPORT ==========");

                    System.out.println(Message.createReport());
                    break;

                // Return to the main menu.
                case "7":

                    menuRunning = false;
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1 to 7.");
            }

        } while (menuRunning);
    }
}
