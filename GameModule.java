

import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class GameModule {
    private final Scanner scanner;
    private final User user;
    private final Dealer dealer;
    private final Player player;
    private final Deck deck;
    private String username;
    private int chips;

    public GameModule() {
        scanner = new Scanner(System.in);
        user = new User("admin", "admin", 100);
        deck = new Deck();
        dealer = new Dealer("Dealer", 100, deck);
        player = new Player(user.getUsername(), user.getChips());
    }

    public void shuffleHands() {
        // shuffle player's hand
        for (int i = 0; i < player.PHand().size(); i++) {
            int random = (int) (Math.random() * player.PHand().size());
            Card temp = player.PHand().get(i);
            player.PHand().set(i, player.PHand().get(random));
            player.PHand().set(random, temp);
        }

        // shuffle dealer's hand
        for (int i = 0; i < dealer.PHand().size(); i++) {
            int random = (int) (Math.random() * dealer.PHand().size());
            Card temp = dealer.PHand().get(i);
            dealer.PHand().set(i, dealer.PHand().get(random));
            dealer.PHand().set(random, temp);
        }

        // Add both player's hand and dealer's hand to the deck
        deck.getCards().addAll(dealer.PHand());
        deck.getCards().addAll(player.PHand());

        // Reset both player's hand and dealer's hand
        dealer.resetHand();
        player.resetHand();
    }

    public void GameLoopAgain() {
        System.out.println("Would you like to play again? (Y/N)");
        String playAgain = scanner.next();
        if (playAgain.equalsIgnoreCase("Y")) {
            // System.out.println("");
            // gameLoop();
            Main.main(null);
        } else if (playAgain.equalsIgnoreCase("N")) {
            System.out.println("Thank you for playing HIGHSUM!");
            System.exit(0);
        } else {
            System.out.println("Invalid input.");
            GameLoopAgain();
        }
    }

    private boolean authenticate(String username, String password) throws IOException {
        File playersFile = new File("players.bin");
        if (!playersFile.exists()) {
            System.err.println("Players file not found.");
            return false;
        }

        try (DataInputStream input = new DataInputStream(new FileInputStream(playersFile))) {
            while (input.available() > 0) {
                String playerUsername = input.readUTF();
                String playerPasswordHash = input.readUTF();
                int chips = input.readInt();

                if (playerUsername.equals(username) && Utility.getHash(password).equals(playerPasswordHash)) {
                    this.chips = chips;
                    return true;
                }
            }
        }

        return false;
    }

    private void adminLogin() throws IOException {
        File adminFile = new File("admin.txt");
        String adminPasswordHash;

        if (!adminFile.exists()) {
            // If admin.txt does not exist, create it and prompt user to set new password
            System.out.println("Admin password doesn't exist. Please set a new password.");
            System.out.print("New admin password: ");
            String newPassword = scanner.nextLine();
            adminPasswordHash = Utility.getHash(newPassword);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(adminFile))) {
                bw.write(adminPasswordHash);
                System.out.println("New admin password set.");
            } catch (IOException e) {
                System.err.println("Error creating admin password file.");
                e.printStackTrace();
                return;
            }
        } else {
            // If admin.txt exists, read the password hash from the file
            try (BufferedReader br = new BufferedReader(new FileReader(adminFile))) {
                adminPasswordHash = br.readLine().trim();
            }
        }

        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (Utility.getHash(password).equals(adminPasswordHash)) {
            Administration admin = new Administration();
            admin.start();
        } else {
            System.err.println("Incorrect admin password.");
        }
    }

    // private void adminLogin() throws IOException {
    // String adminPasswordHash;

    // try (BufferedReader br = new BufferedReader(new FileReader("admin.txt"))) {
    // adminPasswordHash = br.readLine().trim();
    // }

    // System.out.print("Enter admin password: ");
    // String password = scanner.nextLine();

    // if (Utility.getHash(password).equals(adminPasswordHash)) {
    // Administration admin = new Administration();
    // admin.start();
    // } else {
    // System.err.println("Incorrect admin password.");
    // }
    // }

    // public void start() {
    // System.out.println("Welcome to HIGHSUM!");

    // while (true) {
    // // authenticate user
    // System.out.print("\nUsername: ");
    // String username = scanner.next();
    // System.out.print("\nPassword: ");
    // String password = scanner.next();
    // User loggedInUser = authenticateUser(username, password);
    // if (loggedInUser != null) {
    // System.out.println("Login successful!");
    // player = new Player(loggedInUser.getUsername(), loggedInUser.getChips());
    // break;
    // } else {
    // System.out.println("Invalid login credentials.");
    // }
    // }
    // }

    // private User authenticateUser(String username, String password) {
    // // check if user is admin
    // if (username.equals("admin")) {
    // File file = new File("admin.txt");
    // String expectedHash = "";
    // try (Scanner fileScanner = new Scanner(file)) {
    // expectedHash = fileScanner.nextLine().trim();
    // } catch (FileNotFoundException e) {
    // System.out.println("Admin file not found.");
    // return null;
    // }
    // String hashedPassword = Utility.getHash(password);
    // if (hashedPassword.equals(expectedHash)) {
    // Administration admin = new Administration();
    // admin.start();
    // }
    // } else {
    // // check if user exists in players.bin
    // try (ObjectInputStream ois = new ObjectInputStream(new
    // FileInputStream("players.bin"))) {
    // while (true) {
    // User user = (User) ois.readObject();
    // if (user.getUsername().equals(username) && user.authenticate(password)) {
    // gameLoop();
    // }
    // }
    // } catch (EOFException e) {
    // // end of file reached
    // } catch (IOException | ClassNotFoundException e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }

    public void gameLoop() {
        // new code here
        System.out.println("Welcome to HIGHSUM!");

        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            if (username.equals("admin")) {
                try {
                    adminLogin();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            try {
                if (authenticate(username, password)) {
                    this.username = username;
                    break;
                } else {
                    System.err.println("Invalid username or password. Please try again.");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Player player = new Player(username, chips);

        // game loop

        player.resetChips(chips);
        dealer.resetChips(100);
        int currentPlayerChips = chips;
        int tableBet = 0;

        while (true) {

            dealer.shuffleDeck();
            dealer.dealCards(player, dealer);
            dealer.dealCards(player, dealer);

            // First Round
            System.out.print(
                    "-------------------------------------------------------------------------------- \n Dealer dealing cards - ROUND 1 \n -------------------------------------------------------------------------------- \n");
            System.out.println(dealer.getName());
            System.out.println("<HIDDEN CARD> " + dealer.PHand().get(1).toString());
            System.out.println(player.getName());
            System.out.println(player.PHand().get(0).toString() + player.PHand().get(1).toString());
            System.out.println("Hand Value: " + player.getHandValue());

            // The one with the greater get(1) card gets to bet first
            if (player.PHand().get(1).getValue().ordinal() > dealer.PHand().get(1).getValue().ordinal()) {
                System.out.println(player.getName() + " you get to bet first. You have " + currentPlayerChips
                        + " chips. Dealer has " + dealer.getChips() + " chips.");
                int bet;

                while (true) {
                    try {
                        System.out.print("Player call, state bet: ");
                        bet = scanner.nextInt();
                        if (bet > currentPlayerChips || bet > dealer.getChips()) {
                            System.out.println(
                                    "You cant bet chips you dont have or chips that the dealer cant match. You have "
                                            + currentPlayerChips + " chips. Dealer has " + dealer.getChips()
                                            + " chips.");
                        } else if (bet <= currentPlayerChips) {
                            int removeChips = currentPlayerChips - bet;
                            currentPlayerChips = removeChips;
                            // player.removeChips(bet);
                            dealer.removeChips(bet);
                            System.out.println("Player bets " + bet + " chips.");
                            // print the bet on the table
                            tableBet += (bet + bet);
                            System.out.println("Bet on table: " + tableBet);
                            System.out.println("You have " + currentPlayerChips + " chips left.");
                            System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.next(); // take in the invalid input to avoid an infinite loop
                    }
                }

            } else {
                System.out.println("Dealer gets to bet first.");

                // int maxBet = currentPlayerChips;
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips. [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    } else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips. [Y] or [N]");
                        scanner.next();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. ");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                } catch (Exception e) {
                    System.out.println("An error occurred. Please try again.");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                }
                dealer.removeChips(bet);
                int removeChips = currentPlayerChips - bet;
                currentPlayerChips = removeChips;
                // player.removeChips(bet);

                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + currentPlayerChips + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
            }

            // Round 2
            dealer.dealCards(player, dealer);
            System.out.print(
                    "-------------------------------------------------------------------------------- \n Dealer dealing cards - ROUND 2 \n -------------------------------------------------------------------------------- \n");
            System.out.println(dealer.getName());
            System.out.println(
                    "<HIDDEN CARD> " + dealer.PHand().get(1).toString() + dealer.PHand().get(2).toString());
            System.out.println(player.getName());
            System.out.println(player.PHand().get(0).toString() + player.PHand().get(1).toString()
                    + player.PHand().get(2).toString());
            System.out.println("Hand Value: " + player.getHandValue());

            // The one with the greater get(2) card gets to bet first

            if (player.PHand().get(2).getValue().ordinal() > dealer.PHand().get(2).getValue().ordinal()) {
                System.out.println(player.getName() + " you get to bet first.");
                int bet;
                while (true) {
                    System.out.println("Press 'B' to bet, or 'Q' to quit the game. You have " + currentPlayerChips
                            + " chips. Dealer has " + dealer.getChips() + " chips.");
                    String choice = scanner.next();
                    if (choice.equalsIgnoreCase("B")) {
                        // while (true) {
                        try {
                            System.out.print("Player call, state bet: ");
                            bet = scanner.nextInt();
                            if (bet > currentPlayerChips || bet > dealer.getChips()) {
                                System.out.println(
                                        "You cant bet chips you dont have or chips that the dealer cant match. You have "
                                                + currentPlayerChips + " chips. Dealer has " + dealer.getChips()
                                                + " chips.");
                            } else if (bet <= currentPlayerChips) {
                                int removeChips = currentPlayerChips - bet;
                                currentPlayerChips = removeChips;
                                // player.removeChips(bet);
                                dealer.removeChips(bet);
                                System.out.println("Player bets " + bet + " chips.");
                                // print the bet on the table
                                tableBet += (bet + bet);
                                System.out.println("Bet on table: " + tableBet);
                                System.out.println("You have " + currentPlayerChips + " chips left.");
                                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.next(); // take in the invalid input to avoid an infinite loop
                        } catch (Exception e) {
                            System.out.println("An error occurred. Please try again.");
                            scanner.next(); // take in the invalid input to avoid an infinite loop
                        }
                        // }
                    } else if (choice.equalsIgnoreCase("Q")) {
                        dealer.addChips(tableBet);
                        System.out.println("Dealer gets the bet! He wins " + tableBet + " chips for a total of "
                                + dealer.getChips() + " chips.");
                        System.out.println("Thank you for playing HIGHSUM!");
                        // Start the program over again
                        gameLoop();
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            } else {
                System.out.println("Dealer gets to bet first.");
                // int maxBet = currentPlayerChips;
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips. [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    } else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips. [Y] or [N]");
                        scanner.next();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. ");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                } catch (Exception e) {
                    System.out.println("An error occurred. Please try again.");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                }
                dealer.removeChips(bet);
                int removeChips = currentPlayerChips - bet;
                currentPlayerChips = removeChips;
                // player.removeChips(bet);
                // System.out.println("Dealer bets " + bet + " chips.");
                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + currentPlayerChips + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
            }

            // Round 3
            dealer.dealCards(player, dealer);
            System.out.print(
                    "-------------------------------------------------------------------------------- \n Dealer dealing cards - ROUND 3 \n -------------------------------------------------------------------------------- \n");
            System.out.println(dealer.getName());
            System.out.println("<HIDDEN CARD> " + dealer.PHand().get(1).toString()
                    + dealer.PHand().get(2).toString() + dealer.PHand().get(3).toString());
            System.out.println(player.getName());
            System.out.println(player.PHand().get(0).toString() + player.PHand().get(1).toString()
                    + player.PHand().get(2).toString() + player.PHand().get(3).toString());
            System.out.println("Hand Value: " + player.getHandValue());

            // The one with the greater get(3) card gets to bet first
            if (player.PHand().get(3).getValue().ordinal() > dealer.PHand().get(3).getValue().ordinal()) {
                System.out.println(player.getName() + " you get to bet first.");
                int bet;
                while (true) {
                    System.out.println("Press 'B' to bet, or 'Q' to quit the game. You have " + currentPlayerChips
                            + " chips. Dealer has " + dealer.getChips() + " chips.");
                    String choice = scanner.next();
                    if (choice.equalsIgnoreCase("B")) {
                        // while (true) {
                        try {
                            System.out.print("Player call, state bet: ");
                            bet = scanner.nextInt();
                            if (bet > currentPlayerChips || bet > dealer.getChips()) {
                                System.out.println(
                                        "You cant bet chips you dont have or chips that the dealer cant match. You have "
                                                + currentPlayerChips + " chips. Dealer has " + dealer.getChips()
                                                + " chips.");
                            } else if (bet <= currentPlayerChips) {
                                int removeChips = currentPlayerChips - bet;
                                currentPlayerChips = removeChips;
                                // player.removeChips(bet);
                                dealer.removeChips(bet);
                                System.out.println("Player bets " + bet + " chips.");
                                // print the bet on the table
                                tableBet += (bet + bet);
                                System.out.println("Bet on table: " + tableBet);
                                System.out.println("You have " + currentPlayerChips + " chips left.");
                                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.next(); // take in the invalid input to avoid an infinite loop
                        } catch (Exception e) {
                            System.out.println("An error occurred. Please try again.");
                            scanner.next(); // take in the invalid input to avoid an infinite loop
                        }
                        // }
                    } else if (choice.equalsIgnoreCase("Q")) {
                        dealer.addChips(tableBet);
                        System.out.println("Dealer gets the bet! He wins " + tableBet + " chips for a total of "
                                + dealer.getChips() + " chips.");
                        System.out.println("Thank you for playing HIGHSUM!");
                        // Start the program over again
                        gameLoop();
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            } else {
                System.out.println("Dealer gets to bet first.");
                // int maxBet = currentPlayerChips;
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips. [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    } else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips. [Y] or [N]");
                        scanner.next();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. ");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                } catch (Exception e) {
                    System.out.println("An error occurred. Please try again.");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                }
                dealer.removeChips(bet);
                int removeChips = currentPlayerChips - bet;
                currentPlayerChips = removeChips;
                // player.removeChips(bet);
                // System.out.println("Dealer bets " + bet + " chips.");
                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + currentPlayerChips + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
            }

            // Round 4.0
            dealer.dealCards(player, dealer);
            System.out.print(
                    "-------------------------------------------------------------------------------- \n Dealer dealing cards - ROUND 4 \n -------------------------------------------------------------------------------- \n");
            System.out.println(dealer.getName());
            System.out.println("<HIDDEN CARD> " + dealer.PHand().get(1).toString()
                    + dealer.PHand().get(2).toString() + dealer.PHand().get(3).toString()
                    + dealer.PHand().get(4).toString());
            System.out.println(player.getName());
            System.out.println(player.PHand().get(0).toString() + player.PHand().get(1).toString()
                    + player.PHand().get(2).toString() + player.PHand().get(3).toString()
                    + player.PHand().get(4).toString());
            System.out.println("Hand Value: " + player.getHandValue());

            // The one with the greater get(3) card gets to bet first
            if (player.PHand().get(3).getValue().ordinal() > dealer.PHand().get(3).getValue().ordinal()) {
                System.out.println(player.getName() + " you get to bet first.");
                int bet;
                while (true) {
                    System.out.println("Press 'B' to bet, or 'Q' to quit the game. You have " + currentPlayerChips
                            + " chips left. Dealer has " + dealer.getChips() + " chips left.");
                    String choice = scanner.next();
                    if (choice.equalsIgnoreCase("B")) {
                        // while (true) {
                        try {
                            System.out.print("Player call, state bet: ");
                            bet = scanner.nextInt();
                            if (bet > currentPlayerChips || bet > dealer.getChips()) {
                                System.out.println(
                                        "You cant bet chips you dont have or chips that the dealer cant match. You have "
                                                + currentPlayerChips + " chips. Dealer has " + dealer.getChips()
                                                + " chips.");
                            } else if (bet <= currentPlayerChips) {
                                int removeChips = currentPlayerChips - bet;
                                currentPlayerChips = removeChips;
                                // player.removeChips(bet);
                                dealer.removeChips(bet);
                                System.out.println("Player bets " + bet + " chips.");
                                // print the bet on the table
                                tableBet += (bet + bet);
                                System.out.println("Bet on table: " + tableBet);
                                System.out.println("You have " + currentPlayerChips + " chips left.");
                                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.next(); // take in the invalid input to avoid an infinite loop
                        } catch (Exception e) {
                            System.out.println("An error occurred. Please try again.");
                            scanner.next(); // take in the invalid input to avoid an infinite loop
                        }
                        // }
                    } else if (choice.equalsIgnoreCase("Q")) {
                        dealer.addChips(tableBet);
                        System.out.println("Dealer gets the bet! He wins " + tableBet + " chips for a total of "
                                + dealer.getChips() + " chips.");
                        System.out.println("Thank you for playing HIGHSUM!");
                        // Start the program over again
                        gameLoop();
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            } else {
                System.out.println("Dealer gets to bet first.");
                // int maxBet = currentPlayerChips;
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? You have " + currentPlayerChips + " chips left. [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    } else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? You have " + currentPlayerChips
                                + " chips left. [Y] or [N]");
                        scanner.next();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. ");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                } catch (Exception e) {
                    System.out.println("An error occurred. Please try again.");
                    scanner.next(); // take in the invalid input to avoid an infinite loop
                }
                dealer.removeChips(bet);
                int removeChips = currentPlayerChips - bet;
                currentPlayerChips = removeChips;
                // player.removeChips(bet);
                // System.out.println("Dealer bets " + bet + " chips.");
                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + currentPlayerChips + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
            }

            // Round 4
            dealer.dealCards(player, dealer);
            System.out.print(
                    "-------------------------------------------------------------------------------- \n Dealer reveals hidden cards \n -------------------------------------------------------------------------------- \n");
            System.out.println(dealer.getName());
            System.out.println(dealer.PHand().get(0).toString() + dealer.PHand().get(1).toString()
                    + dealer.PHand().get(2).toString() + dealer.PHand().get(3).toString()
                    + dealer.PHand().get(4).toString());
            System.out.println(player.getName());
            System.out.println(player.PHand().get(0).toString() + player.PHand().get(1).toString()
                    + player.PHand().get(2).toString() + player.PHand().get(3).toString()
                    + player.PHand().get(4).toString());
            System.out.println("YOUR FINAL HAND VALUE: " + player.getHandValue());
            System.out.println("DEALER'S FINAL HAND VALUE: " + dealer.getHandValue());
            if (player.getHandValue() > dealer.getHandValue()) {
                System.out.println("Player wins!");
                player.addChips(tableBet);
                int PlayerWinnerBet = currentPlayerChips + tableBet;
                System.out.println("Player gets the bet! He wins " + tableBet + " chips for a total of "
                        + PlayerWinnerBet + " chips.");
                System.out.println("You have " + PlayerWinnerBet + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                System.out.println("GAME END!");
                shuffleHands();
                tableBet = 0;
                System.out.println("Dealer shuffles used cards and place behind the deck.");
            } else if (player.getHandValue() < dealer.getHandValue()) {
                System.out.println("Dealer wins!");
                dealer.addChips(tableBet);
                System.out.println("Dealer gets the bet! He wins " + tableBet + " chips for a total of "
                        + dealer.getChips() + " chips.");
                System.out.println("You have " + currentPlayerChips + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                System.out.println("GAME END!");
                shuffleHands();
                System.out.println("Dealer shuffles used cards and place behind the deck.");
                tableBet = 0;
            } else {
                System.out.println("It's a tie!");
                System.out.println("No one gets the bet! " + tableBet + " chips are returned to the players.");
                tableBet = 0;

                System.out.println("You have " + currentPlayerChips + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                System.out.println("GAME END!");
                shuffleHands();
                System.out.println("Dealer shuffles used cards and place behind the deck.");
            }
            GameLoopAgain();
        }

    }

}
