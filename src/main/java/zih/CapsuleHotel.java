package zih;

import java.util.Scanner;

public class CapsuleHotel {
  // store the capsule in an array
  String[] capsules;

  // constructor
  // initializing the size of the capsules in the hotel
  public CapsuleHotel(int numberOfCapsules){
    // new String(10)
    // the capsule will store null values unless string is stored
    capsules = new String[numberOfCapsules];
  }

  /**
   * Method Name: getMenuOption
   * This method is designed to display a simple menu for a guest system
   * allows user to choose an option from the menu
   * it takes a Scanner object as parameter to read use input
   * returns the user input as a String
   * @param Scanner scanner
   * @return String
   */
  public String getMenuOption(Scanner scanner){
    System.out.println("\nGuest Menu");
    System.out.println("==========");
    System.out.println("1. Check In");
    System.out.println("2. Check Out");
    System.out.println("3. View Guests");
    System.out.println("4. Exit");
    System.out.print("Choose an option [1-4]: ");
    String option = scanner.nextLine();
    return option;
  }

  /**
   * Method Name: handleCheckIn
   * This method is designed to facilitate the check-in process for guests into capsules.
   * It takes a Scanner object for user input and a String representing the guest name.
   * Returns a boolean value indicating success or failure of check in process.
   * @param Scanner scanner
   * @param String guestName
   * @return
   */
  public boolean handleCheckIn(Scanner scanner, String guestName){
    System.out.printf("Capsule #[1-%s]: ", capsules.length);
    int capsuleNumber;

    try {
      capsuleNumber = Integer.parseInt(scanner.nextLine());
    } catch (Exception e){
      System.out.println("ERROR");
      System.out.println("Invalid input. Please enter a valid capsule number.");
      return false;
    }

    // check for the range of the capsule number
    // should not be 0 or greater than the length
    if (capsuleNumber < 1 || capsuleNumber > capsules.length){
      System.out.println("ERROR");
      System.out.printf("Capsule #%s does not exist.", capsuleNumber);
      return false;
    }

    // convert capsule number from (1-based) into valid array index (0-based)
    int index = capsuleNumber - 1;
    if (capsules[index] != null){
      System.out.println("ERROR");
      System.out.printf("Capsule #%s is occupied.", capsuleNumber);
      return false;
    }

    // store the guest name inside the current index of the capsule array
    capsules[index] = guestName;
    System.out.println("SUCCESS!");
    System.out.printf("%s is booked in capsule #%s%n", guestName, capsuleNumber);
    return true;
  }

  /**
   * Method Name: handleCheckOut
   * Manages check out process for guests from capsules.
   * Takes in a Scanner object for user input and returns a boolean value
   * indicating the success or failure of the check-out process.
   * @param Scanner scanner
   * @return boolean
   */
  public boolean handleCheckOut(Scanner scanner){
    boolean checkForGuest = false;

    // check if the capsule has guests
    for (String guest : capsules){
      if (guest != null){
        checkForGuest = true;
        break;
      }
    }

    // display error message if there are no guests to check out
    if (!checkForGuest){
      System.out.println("Sorry, check out is only available if there is at least one guest.");
      return false;
    }

    // display the size of the capsule
    System.out.println("Guest Check Out");
    System.out.println("===============");
    System.out.printf("Capsule #[1-%s]: ", capsules.length);


    // check for valid user input, if user input is valid
    // store user input into capsule number
    int capsuleNumber;
    try {
      capsuleNumber = Integer.parseInt(scanner.nextLine());
    } catch (Exception e){
      System.out.println("ERROR");
      System.out.println("Invalid input. Please enter a valid capsule number.");
      return false;
    }

    // check if the capsule number greater than 0 and less than capsules' length
    if (capsuleNumber < 1 || capsuleNumber > capsules.length){
      System.out.println("ERROR");
      System.out.printf("Capsule #%s does not exist.", capsuleNumber);
      return false;
    }

    // convert capsule number (1-based) into array index (0-based)
    // display error message if capsule is unoccupied
    int index = capsuleNumber - 1;
    if (capsules[index] == null){
      System.out.println("ERROR");
      System.out.printf("Capsule #%s is unoccupied.", capsuleNumber);
      return false;
    }

    // store guest name before removing to print to the user
    // set capsule index to null to remove user
    String guestName = capsules[index];
    capsules[index] = null;
    System.out.println("SUCCESS!");
    System.out.printf("%s has been checked out from capsule #%s", guestName, capsuleNumber);
    return true;
  }

  /**
   * Method Name: viewGuests
   * designed to display information about the guests occupying capsules.
   * Takes in a Scanner object for user input and prints list of guests
   * in the capsules along with their capsule number.
   * if capsule is null [unoccupied] should be printed, otherwise the guest name.
   * @param Scanner scanner
   */
  public void viewGuests(Scanner scanner){
    System.out.println("View Guests");
    System.out.println("===========");
    System.out.printf("Capsule #[1-%s]: ", capsules.length);

    int capsuleNumber = Integer.parseInt(scanner.nextLine());

    // handle non-existent capsule number error
    if (capsuleNumber < 1 || capsuleNumber > capsules.length) {
      System.out.println("ERROR");
      System.out.printf("Capsule #%s does not exist.", capsuleNumber);
      return;
    }

    final int VIEW_RANGE = 11;
    final int HALF_RANGE = VIEW_RANGE / 2;

    if (capsuleNumber < HALF_RANGE || capsules.length <= VIEW_RANGE){      // first 11
      printGuestInRange(0, Math.min(VIEW_RANGE, capsules.length));
    } else if ((capsuleNumber + HALF_RANGE) >= capsules.length) {          // last 11
      printGuestInRange((capsules.length - VIEW_RANGE), capsules.length);
    } else {     // middle 11
      printGuestInRange((capsuleNumber - HALF_RANGE), (capsuleNumber + HALF_RANGE - 1));
    }

  }

  // displays list of guests from start to end, 11 at a time.
  public void printGuestInRange(int start, int end){
    String message = "Capsule: Guest\n==============\n";

    for (int i = start; i < end; i++){
      int displayIndex = i + 1;
      if (capsules[i] == null) {
        message += displayIndex + ": [unoccupied]\n";
      } else {
        message += displayIndex + ": " + capsules[i] + "\n";
      }
    }
    System.out.println(message);
  }

  public static boolean confirmExit(Scanner scanner){
    System.out.println("Exit");
    System.out.println("====");
    System.out.println("Are you sure you want to exit?");
    System.out.println("All data will be lost");
    System.out.print("Exit [y/n]: ");

    // exits the program if the user input is "y"
    return scanner.nextLine().equalsIgnoreCase("y");
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    System.out.println("Welcome to the Capsule-Hotel");
    System.out.println("----------------------------");

    // store the length of the capsule array
    int numberOfCapsules;

    // prompt user to input a valid number and store it into numberOfCapsules
    // which will be used to create a capsule array
    while (true){
      System.out.print("Enter the number of capsules: ");
      int input = Integer.parseInt(scanner.nextLine());

      try {
        // assign the input into number of capsules
        numberOfCapsules = input;

        if (numberOfCapsules <= 0){
          System.out.println("ERROR");
          System.out.println("Number of capsules must be greater than 0.");
        } else {
          break;
        }
      } catch (NumberFormatException e){
        System.out.println("ERROR");
        System.out.println("Please enter a valid number.");
      }
    }

    System.out.printf("There are %s unoccupied capsules ready to be booked.%n%n", numberOfCapsules);

    // create a new CapsuleHotel object with a number of capsule length (Array)
    CapsuleHotel hotelApp = new CapsuleHotel(numberOfCapsules);

    // Switch Statement will invoke methods depending on user input
    // from the Menu Option
    do {
      // prompt user with menu option, store user input into to int option
      int option = Integer.parseInt(hotelApp.getMenuOption(scanner));

      // invoke CapsuleHotel methods depending on the option
      switch (option){
        case 1:
          System.out.println("Guest Check In");
          System.out.println("==============");
          // prompt user for name, store it in guestName
          System.out.print("Guest Name: ");
          String guestName = scanner.nextLine();
          // check in a guest using their name
          hotelApp.handleCheckIn(scanner, guestName);
          break;
        case 2:
          hotelApp.handleCheckOut(scanner);
          break;
        case 3:
          hotelApp.viewGuests(scanner);
          break;
        case 4:
          // exit program if user input is "y" in confirmExit method
          exit = confirmExit(scanner);
          break;
        default:
          System.out.println("Invalid option. Please try again.");
      }
      // run loop until user inputs exit
    } while (!exit);

  }

}