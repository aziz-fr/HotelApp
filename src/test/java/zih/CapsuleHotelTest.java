package zih;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CapsuleHotelTest {
  public InputStream originalSystemIn;
  public Scanner scanner;
  CapsuleHotel capsuleHotel;
  public final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  public final PrintStream originalSystemOut = System.out;

  @BeforeEach
  public void setUp(){
    // save the original System.in to restore it later
    originalSystemIn = System.in;
    capsuleHotel = new CapsuleHotel(20);
    System.setOut(new PrintStream(outputStream));
  }

  @AfterEach
  public void tearDown(){
    // Restore the original System.in
    System.setIn(originalSystemIn);
    System.setOut(originalSystemOut);
  }

  // should set number of capsules on startup
  @Test
  public void shouldRunTestsTest(){
    String expected = "working";
    assertEquals(expected, "working");
  }

  // check-in test
  @Test
  public void shouldCheckInGuestToNullCapsuleTest(){
    // input to stimulate user typing "1" and pressing enter
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    // set the stimulated input stream
    System.setIn(inputStream);

    // create a scanner to read from the stimulated input
    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);
    assertEquals("John", capsuleHotel.capsules[0]);
    assertTrue(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "SUCCESS!\n" +
            "John is booked in capsule #1.";
    assertTrue(capturedOutput.contains(expectedOutput));
  }

  @Test
  public void shouldNotCheckGuestInOccupiedCapsuleTest(){
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);
    assertEquals("John", capsuleHotel.capsules[0]);
    assertTrue(result);

    input = "1\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    result = capsuleHotel.handleCheckIn(scanner, "Mary");
    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "ERROR\n" +
            "Capsule #1 is occupied.";
    assertTrue(capturedOutput.contains(expectedOutput));

    assertNotNull(capsuleHotel.capsules[0]);
    assertEquals("John", capsuleHotel.capsules[0]);
    assertFalse(result);
  }

  @Test
  public void shouldShowErrorMessageWhenCheckingInToNonExistingRoomTest(){
    String input = "50\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);
    boolean result = capsuleHotel.handleCheckIn(scanner, "John");

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "ERROR\n" +
            "Capsule #50 does not exist.";
    assertTrue(capturedOutput.contains(expectedOutput));
    assertFalse(result);
  }

  // check out
  @Test
  public void shouldCheckGuestOutOfOccupiedRoomTest(){
    // check in a guest
    // Input to stimulate user typing "1" and pressing enter
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);  // set the stimulated input stream
    // create scanner to read from the stimulated input
    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);

    // check a guest out
    input = "1\n";  // Input to stimulate user typing "42" and pressing enter
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream); // set the stimulated input stream

    // create a scanner to read from the stimulated input
    scanner = new Scanner(System.in);
    result = capsuleHotel.handleCheckOut(scanner);
    assertNull(capsuleHotel.capsules[0]);
    assertTrue(result);

    // get the captured output
    String capturedOutput = outputStream.toString().trim();
    String expected = "SUCCESS\n" +
            "John is checked out from capsule #1.";
    assertTrue(capturedOutput.contains(expected));
  }

  @Test
  public void shouldNotCheckGuestOutOfUnoccupiedRoomTest(){
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);

    input = "5\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);
    result = capsuleHotel.handleCheckOut(scanner);
    assertNull(capsuleHotel.capsules[4]);
    assertFalse(result);

    String capturedOutput = outputStream.toString().trim();
    String expected = "ERROR\n" +
            "Capsule #5 is unoccupied.";
    assertFalse(capturedOutput.contains(expected));
  }

  @Test
  public void shouldNotAskForCapsuleIfNoGuestsCheckedInTest(){
    String input = "5\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);
    boolean result = capsuleHotel.handleCheckOut(scanner);
    assertFalse(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "Sorry, check out is only available if there is at least one guest.";
    assertTrue(capturedOutput.contains(expectedOutput));
  }

  @Test
  public void shouldShowErrorMessageWhenCheckingOutNonExistingRoomTest(){
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);

    input = "21\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);
    result = capsuleHotel.handleCheckOut(scanner);
    assertFalse(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "ERROR\n" +
            "Capsule #21 does not exist.";
    assertTrue(capturedOutput.contains(expectedOutput));
  }

  // view guests
  @Test
  public void shouldViewElevenAtATimeTest(){
    // input to stimulate user typing "1" and pressing enter
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    // set the stimulated input stream
    System.setIn(inputStream);

    // create a scanner to read from the stimulated input
    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);
    assertEquals("John", capsuleHotel.capsules[0]);
    assertTrue(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "SUCCESS!\n" +
            "John is booked in capsule #1.";
    assertTrue(capturedOutput.contains(expectedOutput));

    // view capsule 10
    input = "9\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    scanner = new Scanner(System.in);
    capsuleHotel.viewGuests(scanner);
    capturedOutput = outputStream.toString().trim();
    expectedOutput =  "Capsule: Guest\n" +
            "4: [unoccupied]\n" +
            "5: [unoccupied]\n" +
            "6: [unoccupied]\n" +
            "7: [unoccupied]\n" +
            "8: [unoccupied]\n" +
            "9: [unoccupied]\n" +
            "10: [unoccupied]\n" +
            "11: [unoccupied]\n" +
            "12: [unoccupied]\n" +
            "13: [unoccupied]\n" +
            "14: [unoccupied]";

    assertTrue(capturedOutput.contains(expectedOutput));
  }

  @Test
  public void shouldHandleLowerBoundsTest(){
    // input to stimulate user typing "1" and pressing enter
    String input = "1\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    // set the stimulated input stream
    System.setIn(inputStream);

    // create a scanner to read from the stimulated input
    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[0]);
    assertEquals("John", capsuleHotel.capsules[0]);
    assertTrue(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "SUCCESS!\n" +
            "John is booked in capsule #1.";
    assertTrue(capturedOutput.contains(expectedOutput));

    // view capsule 1
    input = "1\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    scanner = new Scanner(System.in);
    capsuleHotel.viewGuests(scanner);
    capturedOutput = outputStream.toString().trim();
    expectedOutput =  "Capsule: Guest\n" +
            "1: John\n" +
            "2: [unoccupied]\n" +
            "3: [unoccupied]\n" +
            "4: [unoccupied]\n" +
            "5: [unoccupied]\n" +
            "6: [unoccupied]\n" +
            "7: [unoccupied]\n" +
            "8: [unoccupied]\n" +
            "9: [unoccupied]\n" +
            "10: [unoccupied]\n" +
            "11: [unoccupied]";

    assertTrue(capturedOutput.contains(expectedOutput));
  }

  @Test
  public void shouldHandleUpperBoundsTest(){
    // input to stimulate user typing "10" and pressing enter
    String input = "10\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    // set the stimulated input stream
    System.setIn(inputStream);

    // create a scanner to read from the stimulated input
    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[9]);
    assertEquals("John", capsuleHotel.capsules[9]);
    assertTrue(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "SUCCESS!\n" +
            "John is booked in capsule #10.";
    assertTrue(capturedOutput.contains(expectedOutput));

    // view capsule 20
    input = "20\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    scanner = new Scanner(System.in);
    capsuleHotel.viewGuests(scanner);
    capturedOutput = outputStream.toString().trim();
    expectedOutput =  "Capsule: Guest\n" +
            "10: John\n" +
            "11: [unoccupied]\n" +
            "12: [unoccupied]\n" +
            "13: [unoccupied]\n" +
            "14: [unoccupied]\n" +
            "15: [unoccupied]\n" +
            "16: [unoccupied]\n" +
            "17: [unoccupied]\n" +
            "18: [unoccupied]\n" +
            "19: [unoccupied]\n" +
            "20: [unoccupied]";

    assertTrue(capturedOutput.contains(expectedOutput));
  }

  @Test
  public void shouldHandleOutOfBoundsTest(){
    // input to stimulate user typing "10" and pressing enter
    String input = "10\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    // set the stimulated input stream
    System.setIn(inputStream);

    // create a scanner to read from the stimulated input
    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.handleCheckIn(scanner, "John");
    assertNotNull(capsuleHotel.capsules[9]);
    assertEquals("John", capsuleHotel.capsules[9]);
    assertTrue(result);

    String capturedOutput = outputStream.toString().trim();
    String expectedOutput = "SUCCESS!\n" +
            "John is booked in capsule #10.";
    assertTrue(capturedOutput.contains(expectedOutput));

    // view capsule 20
    input = "22\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    scanner = new Scanner(System.in);
    capsuleHotel.viewGuests(scanner);
    capturedOutput = outputStream.toString().trim();
    expectedOutput = "ERROR\n" +
            "Capsule #22 does not exist.";
    assertTrue(capturedOutput.contains(expectedOutput));
  }

  // exit program
  @Test
  public void shouldConfirmExitTest(){
    // lowercase y
    String input = "y\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    boolean result = capsuleHotel.confirmExit(scanner);
    assertTrue(result);

    // uppercase Y
    input = "y\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    result = capsuleHotel.confirmExit(scanner);
    assertTrue(result);

    // negative cases
    input = "n\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    result = capsuleHotel.confirmExit(scanner);
    assertFalse(result);

    input = "c\n";
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    scanner = new Scanner(System.in);

    result = capsuleHotel.confirmExit(scanner);
    assertFalse(result);

  }
}