import java.util.*;

class ATMUser {
    private String name;
    private double balance;
    private int pin;

    // Constructor to create a new user with their name, starting balance and 4-digit PIN
    public ATMUser(String name, double balance, int pin) {
        this.name = name;
        this.balance = balance;
        this.pin = pin;
    }

    // Method to get the user's name so we can greet them later
    public String getName() {
        return name;
    }

    // Check if the entered PIN matches the user's actual PIN
    // Returns true if correct, false if wrong
    public boolean authenticate(int enteredPin) {
        return pin == enteredPin;
    }

    // Display the current balance formatted with 2 decimals
    // Shows how much money user currently has in their account
    public void checkBalance() {
        System.out.printf("Balance: Rs. %.2f\n", balance);
    }

    // Deposit money into the account if the amount is positive
    // Adds the deposit amount to the user's balance and confirms
    // If the amount is invalid (negative or zero), prints error message
    public void deposit(double amt) {
        if (amt > 0) {
            balance += amt;
            System.out.printf("Deposited Rs. %.2f :)\n", amt);
        } else {
            System.out.println("Nah, invalid deposit");
        }
    }

    // Withdraw money from the account if there's enough balance and amount is positive
    // Deducts the amount from balance and confirms withdrawal
    // If amount is greater than balance, warns user about insufficient funds
    // If amount invalid (negative or zero), prints error message
    public void withdraw(double amt) {
        if (amt > 0 && amt <= balance) {
            balance -= amt;
            System.out.printf("Withdrawn Rs. %.2f, done!\n", amt);
        } else if (amt > balance) {
            System.out.println("Bro, not enough balance");
        } else {
            System.out.println("Invalid withdrawal");
        }
    }
 

    // Change the PIN number for the user
    // Checks if the old PIN entered is correct
    // Ensures new PIN is exactly 4 digits also between 1000 and 9999
    // Returns true if successful otherwise false with proper messages
    public boolean changePIN(int oldPin, int newPin) {
        if (this.pin == oldPin) {
            if (newPin >= 1000 && newPin <= 9999) {
                this.pin = newPin;
                return true;
            } else {
                System.out.println("Pin gotta be 4 digits, dude");
                return false;
            }
        } else {
            System.out.println("Old pin wrong, try again");
            return false;
        }
    }
}

public class ATMInterface {
    // Store all users in a map: username (String) -> ATMUser object
    private static Map<String, ATMUser> users = new HashMap<>();
    // Scanner for reading user input from console
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Adding some dummy users with usernames, initial balances and PINs
        users.put("jones", new ATMUser("Jones", 5000, 1234));
        users.put("tom", new ATMUser("Tom", 8000, 5678));
        users.put("alex", new ATMUser("Alex", 3000, 4321));

        System.out.println("===== ATM =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose: ");
        int opt = scanner.nextInt();
        scanner.nextLine(); // Clear input buffer after reading number

        ATMUser currentUser = null; // To keep track of logged in user

        // Based on user input, either login or register a new user
        if (opt == 1) {
            currentUser = login();
        } else if (opt == 2) {
            currentUser = register();
        } else {
            System.out.println("Wrong option, bye!");
            return; // Exit program for invalid choice
        }

        // If login or register failed (null), exit
        if (currentUser == null) return;

        // Greet the logged in user by their name
        System.out.println("\nHey " + currentUser.getName() + "!");

        boolean run = true;
        while (run) {
            showMenu(); // Show ATM options
            int ch = scanner.nextInt();

            switch (ch) {
                case 1:
                    // Option 1: Check current balance
                    currentUser.checkBalance();
                    break;
                case 2:
                    // Option 2: Deposit money into account
                    System.out.print("Deposit Rs.: ");
                    double d = scanner.nextDouble();
                    currentUser.deposit(d);
                    break;
                case 3:
                    // Option 3: Withdraw money from account
                    System.out.print("Withdraw Rs.: ");
                    double w = scanner.nextDouble();
                    currentUser.withdraw(w);
                    break;
                case 4:
                    // Option 4: Change account PIN securely
                    System.out.print("Old PIN: ");
                    int oldPin = scanner.nextInt();
                    System.out.print("New 4-digit PIN: ");
                    int newPin = scanner.nextInt();
                    if (currentUser.changePIN(oldPin, newPin)) {
                        System.out.println("Pin changed successfully!");
                    }
                    break;
                case 5:
                    // Option 5: Exit the ATM program
                    System.out.println("Thank you for using! Visit again!");
                    run = false;
                    break;
                default:
                    // Invalid option chosen
                    System.out.println("Nope, try again");
            }
            System.out.println(); // Add blank line for spacing
        }

        // Close scanner resource before program ends
        scanner.close();
    }

    // Handles user login process
    // Asks for username, checks if user exists
    // Then asks for PIN and authenticates
    // Returns logged-in ATMUser object or null if fail
    static ATMUser login() {
        System.out.print("Username: ");
        String user = scanner.nextLine().toLowerCase(); // Ensure case-insensitive match

        if (!users.containsKey(user)) {
            System.out.println("No user detected with that name");
            return null;
        }

        ATMUser u = users.get(user);

        System.out.print("PIN: ");
        int pin = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        if (!u.authenticate(pin)) {
            System.out.println("Pin incorrect");
            return null;
        }
        return u;
    }

    // Handles new user registration process
    // Asks for username, makes sure it’s not taken
    // Gets full name, 4-digit PIN and initial deposit amount
    // Returns new ATMUser object if success or null if fail
    static ATMUser register() {
        System.out.print("Pick username: ");
        String user = scanner.nextLine().toLowerCase(); // Store all usernames lowercase

        if (users.containsKey(user)) {
            System.out.println("Username taken, sorry");
            return null;
        }

        System.out.print("Full name: ");
        String name = scanner.nextLine();

        System.out.print("Set 4-digit PIN: ");
        int pin = scanner.nextInt();

        System.out.print("Initial deposit Rs.: ");
        double dep = scanner.nextDouble();
        scanner.nextLine(); // clearing buffer

        if (dep < 0) {
            System.out.println("No negative deposits allowed");
            return null;
        }

        ATMUser nu = new ATMUser(name, dep, pin);
        users.put(user, nu);
        System.out.println("You are in! Login now.");
        return nu;
    }


    
    // Displays ATM options menu to the user
    static void showMenu() {
        System.out.println("=== MENU ===");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Change PIN");
        System.out.println("5. Exit");
        System.out.print("Choice: ");
    }
}
