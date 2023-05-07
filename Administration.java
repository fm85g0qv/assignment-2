//ALL GOOD NOW!

import java.util.Scanner;
import java.io.*;
import java.util.List;
import java.util.ArrayList;


public class Administration {
    private final Scanner scanner;

    public Administration() {
        scanner = new Scanner(System.in);
    }

    public void createPlayer() {
        String username = "";
        String password = "";
        int chips = 0;
    
        while (true) {
            System.out.println("Enter username:");
            if (scanner.hasNext()) {
                String input = scanner.next();
                if (input.matches("[a-zA-Z0-9]+")) {
                    username = input;
                    break;
                } else {
                    System.err.println("Invalid input. Please enter a valid username.");
                }
            }
        }
    
        while (true) {
            System.out.println("Enter password:");
            if (scanner.hasNext()) {
                String input = scanner.next();
                if (input.matches("[a-zA-Z0-9]+")) {
                    password = Utility.getHash(input);
                    break;
                } else {
                    System.err.println("Invalid input. Please enter a valid password.");
                }
            }
        }
    
        while (true) {
            System.out.println("Enter starting chips:");
            if (scanner.hasNextInt()) {
                chips = scanner.nextInt();
                break;
            } else {
                System.err.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    
        try {
            // create a file output stream for writing to the binary file
            FileOutputStream fileOut = new FileOutputStream("players.bin", true);
    
            // create a data output stream for writing primitive data types to the file
            // output stream
            DataOutputStream dataOut = new DataOutputStream(fileOut);
    
            // write the player information to the file as primitive data types
            dataOut.writeUTF(username);
            dataOut.writeUTF(password);
            dataOut.writeInt(chips);
    
            // close the data output stream and file output stream
            dataOut.close();
            fileOut.close();
    
            System.out.println("Player created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating player.");
            e.printStackTrace();
        }
    }
    

    // public void createPlayer() {
    //     System.out.println("Enter username:");
    //     String username = scanner.next();
    //     System.out.println("Enter password:");
    //     String password = scanner.next();
    //     System.out.println("Enter starting chips:");
    //     int chips = scanner.nextInt();

    //     try {
    //         // create a file output stream for writing to the binary file
    //         FileOutputStream fileOut = new FileOutputStream("players.bin", true);

    //         // create a data output stream for writing primitive data types to the file
    //         // output stream
    //         DataOutputStream dataOut = new DataOutputStream(fileOut);

    //         // write the player information to the file as primitive data types
    //         dataOut.writeUTF(username);
    //         dataOut.writeUTF(Utility.getHash(password));
    //         dataOut.writeInt(chips);

    //         // close the data output stream and file output stream
    //         dataOut.close();
    //         fileOut.close();

    //         System.out.println("Player created successfully.");
    //     } catch (IOException e) {
    //         System.out.println("Error creating player.");
    //         e.printStackTrace();
    //     }
    // }

    public void viewPlayers() {
        int usernameWidth = 15;
        int passwordWidth = 64;
        int chipsWidth = 5;
        System.out.printf("%-" + usernameWidth + "s %-" + passwordWidth + "s %" + chipsWidth + "s\n",
            "USERNAME", "PASSWORD", "CHIPS");
        System.out.println("-".repeat(usernameWidth + passwordWidth + chipsWidth + 1));
        
        try {
            FileInputStream fileIn = new FileInputStream("players.bin");
            try (DataInputStream dataIn = new DataInputStream(fileIn)) {
                while (true) {
                    String username = dataIn.readUTF();
                    String password = dataIn.readUTF();
                    int chips = dataIn.readInt();
       
                    System.out.printf("%-" + usernameWidth + "s %-" + passwordWidth + "s %" + chipsWidth + "d\n",
                        username, password, chips);
                }
            }
        } catch (EOFException e) {
            System.out.println("-".repeat(usernameWidth + passwordWidth + chipsWidth + 1));
        } catch (IOException e) {
            System.out.println("Error viewing players.");
            e.printStackTrace();
        }
    }
    
    

    // public void viewPlayers() {
    //     System.out.println("USERNAME\tPASSWORD\t\t\t\t\t\t\t\t\tCHIPS");
    //     System.out.println("-----------------------------------------------------------------------------------------------------");
    
    //     try {
    //         FileInputStream fileIn = new FileInputStream("players.bin");
    //         try (DataInputStream dataIn = new DataInputStream(fileIn)) {
    //             while (true) {
    //                 String username = dataIn.readUTF();
    //                 String password = dataIn.readUTF();
    //                 int chips = dataIn.readInt();
   
    //                 System.out.printf("%s\t\t%s\t\t%d\n", username, password, chips);
    //             }
    //         }
    //     } catch (EOFException e) {
    //         System.out.println("-----------------------------------------------------------------------------------------------------");
    //     } catch (IOException e) {
    //         System.out.println("Error viewing players.");
    //         e.printStackTrace();
    //     }
    // }
    
    public void deletePlayer() {
        System.out.print("Enter username of player to delete (Type 'cancel' to quit.):");
        String usernameToDelete = scanner.next();
        boolean playerDeleted = false;

        if (usernameToDelete.equalsIgnoreCase("cancel")) {
            System.out.println("Delete player cancelled.\n");
            return;
        }

    
        try {
            FileInputStream fileIn = new FileInputStream("players.bin");
            FileOutputStream fileOut = new FileOutputStream("players.tmp");
    
            try (DataInputStream dataIn = new DataInputStream(fileIn);
                 DataOutputStream dataOut = new DataOutputStream(fileOut)) {
    
                while (true) {
                    String username = dataIn.readUTF();
                    String password = dataIn.readUTF();
                    int chips = dataIn.readInt();
    
                    if (username.equals(usernameToDelete)) {
                        System.out.printf("Player with username '%s' deleted.\n\n\n", username);
                        playerDeleted = true; // set the variable to true
                    } else {
                        dataOut.writeUTF(username);
                        dataOut.writeUTF(password);
                        dataOut.writeInt(chips);
                    }
                }
            } catch (EOFException e) {
                fileIn.close();
                fileOut.close();
    
                File oldFile = new File("players.bin");
                File newFile = new File("players.tmp");
                oldFile.delete();
                newFile.renameTo(oldFile);
    
                if (!playerDeleted) { // check if no player was deleted
                    System.out.printf("No player found with username '%s'.\n\n\n", usernameToDelete);
                    deletePlayer();
                }
            }
        } catch (IOException e) {
            System.out.println("Error deleting player.");
            e.printStackTrace();
        }
    }
    
    public void editChips() {
        System.out.println("Enter the username of the player you want to edit:");
        String username = scanner.next();
    
        try {
            RandomAccessFile raf = new RandomAccessFile("players.bin", "rw");
            boolean found = false;
            while (raf.getFilePointer() < raf.length()) {
                String u = raf.readUTF();
                String p = raf.readUTF();
                int c = raf.readInt();
                if (u.equals(username)) {
                    System.out.println("Current chips amount: " + c);
                    System.out.println("Enter new chips amount or type 'cancel' to exit:");
                    String input = scanner.next();
                    if (input.equalsIgnoreCase("cancel")) {
                        return;
                    }
                    int newChips = Integer.parseInt(input);
                    raf.seek(raf.getFilePointer() - 4);
                    raf.writeInt(newChips);
                    System.out.println("Chips updated successfully.");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Player not found.");
            }
            raf.close();
        } catch (IOException e) {
            System.out.println("Error editing chips.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number or 'cancel'.");
            editChips();
        }
    }

    public void resetPlayerPassword() {
        boolean validUsername = false;
        String username = "";
        while (!validUsername) {
            System.out.println("Enter the username of the player whose password you want to reset:");
            username = scanner.next();
            try {
                RandomAccessFile raf = new RandomAccessFile("players.bin", "rw");
                raf.seek(0);
                boolean found = false;
                while (raf.getFilePointer() < raf.length()) {
                    String fileUsername = raf.readUTF();
                    if (fileUsername.equals(username)) {
                        found = true;
                        break;
                    } else {
                        raf.readUTF();
                        raf.readInt();
                    }
                }
                if (found) {
                    validUsername = true;
                } else {
                    System.out.println("Invalid username. Please try again.");
                }
                raf.close();
            } catch (IOException e) {
                System.out.println("Error checking player username.");
                e.printStackTrace();
            }
        }
        boolean validPassword = false;
        String newPassword = "";
        while (!validPassword) {
            System.out.println("Enter the new password:");
            newPassword = scanner.next();
            if (newPassword.matches("[a-zA-Z0-9]+")) {
                validPassword = true;
            } else {
                System.out.println("Password must be alphanumeric. Please try again.");
            }
        }
    
        try {
            RandomAccessFile raf = new RandomAccessFile("players.bin", "rw");
            raf.seek(0);
            boolean found = false;
            while (raf.getFilePointer() < raf.length()) {
                String fileUsername = raf.readUTF();
                if (fileUsername.equals(username)) {
                    raf.writeUTF(Utility.getHash(newPassword));
                    System.out.println("Password reset successfully.");
                    System.out.println("New password for " + username + " is: " + newPassword);
                    found = true;
                    break;
                } else {
                    raf.readUTF();
                    raf.readInt();
                }
            }
            if (!found) {
                System.out.println("Player not found.");
            }
            raf.close();
        } catch (IOException e) {
            System.out.println("Error resetting player password.");
            e.printStackTrace();
        }
    }    
    
    
    
    // public void resetPlayerPassword() {
    //     System.out.println("Enter the username of the player whose password you want to reset:");
    //     String username = scanner.next();
    //     System.out.println("Enter the new password:");
    //     String newPassword = scanner.next();
    
    //     try {
    //         // open the file in read-write mode using RandomAccessFile
    //         RandomAccessFile raf = new RandomAccessFile("players.bin", "rw");
    
    //         // seek to the beginning of the file
    //         raf.seek(0);
    
    //         boolean found = false;
    
    //         // iterate over the players in the file
    //         while (raf.getFilePointer() < raf.length()) {
    //             String fileUsername = raf.readUTF();
    
    //             // check if the current player matches the username we're looking for
    //             if (fileUsername.equals(username)) {
    //                 // overwrite the existing password with the new one
    //                 raf.writeUTF(Utility.getHash(newPassword));
    
    //                 System.out.println("Password reset successfully.");
    //                 System.out.println("New password for " + username + " is: " + newPassword);
    //                 found = true;
    //                 break;
    //             } else {
    //                 // skip over the password and chips fields for this player
    //                 raf.readUTF();
    //                 raf.readInt();
    //             }
    //         }
    
    //         if (!found) {
    //             System.out.println("Player not found.");
    //         }
    
    //         raf.close();
    //     } catch (IOException e) {
    //         System.out.println("Error resetting player password.");
    //         e.printStackTrace();
    //     }
    // }
    
       

    public void changeAdminPassword() {
        System.out.println("Enter new password: ");
        String password = scanner.next();
        
        try {
            FileWriter fileWriter = new FileWriter("admin.txt");
            fileWriter.write(Utility.getHash(password));
            fileWriter.close();
            System.out.println("");
            System.out.println("Admin password changed successfully. New password is: " + password);
        } catch (IOException e) {
            System.out.println("Error changing admin password.");
            e.printStackTrace();
        }
    }
    

    public void start() {
        System.out.println("Welcome to the Administration module!");

        while (true) {
            
            System.out.println("1. Create a player");
            System.out.println("2. View all players");
            System.out.println("3. Delete a player");
            System.out.println("4. Issue more chips to a player");
            System.out.println("5. Reset player's password");
            System.out.println("6. Change administrator password");
            System.out.println("7. Logout.");

            System.out.print("Input a number to select an option: ");
            String choice = scanner.next();
            switch (choice) {
                case "1":
                    System.out.println("Create a player option selected. \n");
                    createPlayer();
                    break;
                case "2":
                    System.out.println("Viewing all players... \n");
                    viewPlayers();
                    break;
                case "3":
                    System.out.println("Delete a player option selected. \n");
                    viewPlayers();
                    deletePlayer();
                    break;
                case "4":
                    System.out.println("Issue more chips to a player selected. \n");
                    viewPlayers();
                    editChips();
                    break;
                case "5":
                    System.out.println("Reset player's password");
                    viewPlayers();
                    resetPlayerPassword(); 
                    break;
                case "6":
                    System.out.println("Change administrator password selected. \n");
                    changeAdminPassword();
                    break;
                case "7":
                    System.out.println("Logging out... \n");
                    GameModule gameModule = new GameModule();
                    gameModule.gameLoop();
                default:
                    System.out.println("Invalid Option, please try again.");
                    start();
            }
        }
    }
}
