// 7907175
// Jeric Dave Ubay-Ubay Nufable
// University of Wollongong
// CSIT 121
// Bachelors of Business in Information Technology
// Due Date 23/4/2023

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileNotFoundException;

public class GameModule {
    private final Scanner scanner;
    private final User user;
    private final Dealer dealer;
    private final Player player;
    private final Deck deck;

    public GameModule() {
        scanner = new Scanner(System.in);
        user = new User("admin", Utility.getHash("admin"), 100);
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
            System.out.println("");
            gameLoop();
        } else if (playAgain.equalsIgnoreCase("N")) {
            System.out.println("Thank you for playing HIGHSUM!");
            System.exit(0);
        } else {
            System.out.println("Invalid input.");
            GameLoopAgain();
        }
    }

    public void start() {
        System.out.println("Welcome to HIGHSUM!");

        while (true) {
            // authenticate user
            System.out.print("\nUsername: ");
            String username = scanner.next();
            System.out.print("\nPassword: ");
            String password = scanner.next();
            
            // read the hash from admin.txt
            File file = new File("admin.txt");
            String expectedHash = "";
            try (Scanner fileScanner = new Scanner(file)) {
                expectedHash = fileScanner.nextLine().trim();
            } catch (FileNotFoundException e) {
                System.out.println("Admin file not found.");
                continue;
            }
            
            // Check if user is admin
            // username and password is admin
            String hashedPassword = Utility.getHash(password);
            if (username.equals("admin") && hashedPassword.equals(expectedHash)) {
                System.out.print("\nAdmin Login successful! Starting Administration Module...\n\n");
                Administration admin = new Administration();
                admin.start();
            }
            System.out.println("Invalid login credentials.");
        }
    }

    public void gameLoop() {
        // game loop

        player.resetChips(100);
        dealer.resetChips(100);
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
                System.out.println(player.getName() + " you get to bet first.");
                int bet;
                while (true) {
                    try {
                        System.out.print("Player call, state bet: ");
                        bet = scanner.nextInt();
                        if (bet > player.getChips()) {
                            System.out.println("You don't have enough chips to bet that amount. You have "
                                    + player.getChips() + " chips.");
                        } else if (bet <= player.getChips()) {
                            player.removeChips(bet);
                            dealer.removeChips(bet);
                            System.out.println("Player bets " + bet + " chips.");
                            // print the bet on the table
                            tableBet += (bet + bet);
                            System.out.println("Bet on table: " + tableBet);
                            System.out.println("You have " + player.getChips() + " chips left.");
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
                
                int maxBet = player.getChips();
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    }
                    else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? [Y] or [N]");
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
                player.removeChips(bet);
                
                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + player.getChips() + " chips left.");
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
                    System.out.println("Press 'B' to bet, or 'Q' to quit the game.");
                    String choice = scanner.next();
                    if (choice.equalsIgnoreCase("B")) {
                        // while (true) {
                        try {
                            System.out.print("Player call, state bet: ");
                            bet = scanner.nextInt();
                            if (bet > player.getChips()) {
                                System.out.println("You don't have enough chips to bet that amount. You have "
                                        + player.getChips() + " chips.");
                            } else if (bet <= player.getChips()) {
                                player.removeChips(bet);
                                dealer.removeChips(bet);
                                System.out.println("Player bets " + bet + " chips.");
                                // print the bet on the table
                                tableBet += (bet + bet);
                                System.out.println("Bet on table: " + tableBet);
                                System.out.println("You have " + player.getChips() + " chips left.");
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
                        start();
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            } else {
                System.out.println("Dealer gets to bet first.");
                int maxBet = player.getChips();
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    }
                    else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? [Y] or [N]");
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
                player.removeChips(bet);
                // System.out.println("Dealer bets " + bet + " chips.");
                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + player.getChips() + " chips left.");
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
                    System.out.println("Press 'B' to bet, or 'Q' to quit the game.");
                    String choice = scanner.next();
                    if (choice.equalsIgnoreCase("B")) {
                        // while (true) {
                        try {
                            System.out.print("Player call, state bet: ");
                            bet = scanner.nextInt();
                            if (bet > player.getChips()) {
                                System.out.println("You don't have enough chips to bet that amount. You have "
                                        + player.getChips() + " chips.");
                            } else if (bet <= player.getChips()) {
                                player.removeChips(bet);
                                dealer.removeChips(bet);
                                System.out.println("Player bets " + bet + " chips.");
                                // print the bet on the table
                                tableBet += (bet + bet);
                                System.out.println("Bet on table: " + tableBet);
                                System.out.println("You have " + player.getChips() + " chips left.");
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
                        start();
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            } else {
                System.out.println("Dealer gets to bet first.");
                int maxBet = player.getChips();
                int bet = 10; // dealer bets 10
                System.out.println("Dealer bets " + bet + " chips.");
                System.out.println("Do you want to follow? [Y] or [N]");
                String followOption = scanner.next();
                try {
                    if (followOption.equalsIgnoreCase("Y")) {

                    }
                    else if (followOption.equalsIgnoreCase("N")) {
                        System.out.println("Thanks for playing HighSum!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("Try again");
                        System.out.println("Do you want to follow? [Y] or [N]");
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
                player.removeChips(bet);
                // System.out.println("Dealer bets " + bet + " chips.");
                tableBet += (bet + bet);
                System.out.println("Bet on table: " + tableBet);
                System.out.println("You have " + player.getChips() + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
            }

                        // Round 4.0
                        dealer.dealCards(player, dealer);
                        System.out.print(
                                "-------------------------------------------------------------------------------- \n Dealer dealing cards - ROUND 4 \n -------------------------------------------------------------------------------- \n");
                        System.out.println(dealer.getName());
                        System.out.println("<HIDDEN CARD> " + dealer.PHand().get(1).toString()
                                + dealer.PHand().get(2).toString() + dealer.PHand().get(3).toString() + dealer.PHand().get(4).toString());
                        System.out.println(player.getName());
                        System.out.println(player.PHand().get(0).toString() + player.PHand().get(1).toString()
                                + player.PHand().get(2).toString() + player.PHand().get(3).toString() + player.PHand().get(4).toString());
                        System.out.println("Hand Value: " + player.getHandValue());
            
                        // The one with the greater get(3) card gets to bet first
                        if (player.PHand().get(3).getValue().ordinal() > dealer.PHand().get(3).getValue().ordinal()) {
                            System.out.println(player.getName() + " you get to bet first.");
                            int bet;
                            while (true) {
                                System.out.println("Press 'B' to bet, or 'Q' to quit the game.");
                                String choice = scanner.next();
                                if (choice.equalsIgnoreCase("B")) {
                                    // while (true) {
                                    try {
                                        System.out.print("Player call, state bet: ");
                                        bet = scanner.nextInt();
                                        if (bet > player.getChips()) {
                                            System.out.println("You don't have enough chips to bet that amount. You have "
                                                    + player.getChips() + " chips.");
                                        } else if (bet <= player.getChips()) {
                                            player.removeChips(bet);
                                            dealer.removeChips(bet);
                                            System.out.println("Player bets " + bet + " chips.");
                                            // print the bet on the table
                                            tableBet += (bet + bet);
                                            System.out.println("Bet on table: " + tableBet);
                                            System.out.println("You have " + player.getChips() + " chips left.");
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
                                    start();
                                } else {
                                    System.out.println("Invalid input.");
                                }
                            }
                        } else {
                            System.out.println("Dealer gets to bet first.");
                            int maxBet = player.getChips();
                            int bet = 10; // dealer bets 10
                            System.out.println("Dealer bets " + bet + " chips.");
                            System.out.println("Do you want to follow? [Y] or [N]");
                            String followOption = scanner.next();
                            try {
                                if (followOption.equalsIgnoreCase("Y")) {
            
                                }
                                else if (followOption.equalsIgnoreCase("N")) {
                                    System.out.println("Thanks for playing HighSum!");
                                    System.exit(0);
                                }
            
                                else {
                                    System.out.println("Try again");
                                    System.out.println("Do you want to follow? [Y] or [N]");
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
                            player.removeChips(bet);
                            // System.out.println("Dealer bets " + bet + " chips.");
                            tableBet += (bet + bet);
                            System.out.println("Bet on table: " + tableBet);
                            System.out.println("You have " + player.getChips() + " chips left.");
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
                System.out.println("Player gets the bet! He wins " + tableBet + " chips for a total of "
                        + player.getChips() + " chips.");
                System.out.println("You have " + player.getChips() + " chips left.");
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
                System.out.println("You have " + player.getChips() + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                System.out.println("GAME END!");
                shuffleHands();
                System.out.println("Dealer shuffles used cards and place behind the deck.");
                tableBet = 0;
            } else {
                System.out.println("It's a tie!");
                System.out.println("No one gets the bet! " + tableBet + " chips are returned to the players.");
                tableBet = 0;
                
                
                System.out.println("You have " + player.getChips() + " chips left.");
                System.out.println("Dealer has " + dealer.getChips() + " chips left.");
                System.out.println("GAME END!");
                shuffleHands();
                System.out.println("Dealer shuffles used cards and place behind the deck.");
            }
            GameLoopAgain();
        }

    }

}
