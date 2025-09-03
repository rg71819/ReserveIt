package reservit.ticketbooking;

import reservit.ticketbooking.entities.Train;
import reservit.ticketbooking.entities.User;
import reservit.ticketbooking.services.UserBookingService;
import reservit.ticketbooking.util.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class app {
    public static void main(String args[]){
        System.out.println("***********************************");
        System.out.println("*            ReserveIt            *");
        System.out.println("***********************************");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        while(option!=7){
            System.out.println("Choose an Option");
            System.out.println("Option 1: Sign Up");
            System.out.println("Option 2: Sign In");
            System.out.println("Option 3: Fetch Bookings");
            System.out.println("Option 4: Search Trains");
            System.out.println("Option 5: Book a Seat");
            System.out.println("Option 6: Cancel my Booking");
            System.out.println("Option 7: Sign Out");
            option= scanner.nextInt();
            switch (option){
                case 1:
                    System.out.println("Enter username: ");
                    String usernameToSignUp = scanner.next();
                    System.out.println("Enter password: ");
                    String passwordToSignUp = scanner.next();
                    try {
                        User userToSignUp = new User(usernameToSignUp, passwordToSignUp,UserServiceUtil.hashedPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                        userBookingService.signUp(userToSignUp);
                    } catch (Exception e) {
                       System.out.println(e);
                    }
                    break;
                case 2:
                    System.out.println("Enter username: ");
                    String usernameToLogin = scanner.next();
                    System.out.println("Enter password: ");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(usernameToLogin,passwordToLogin,UserServiceUtil.hashedPassword(passwordToLogin),new ArrayList<>(), UUID.randomUUID().toString());
                        userBookingService.loginUser(userToLogin);
                    break;
                case 3:
                    System.out.println("Fetch your bookings.");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Type your source station");
                    String source = scanner.next();
                    System.out.println("Type your destination station");
                    String destination = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source, destination);
                    System.out.println("Train Id");
                    int index = 1;
                    for (Train t: trains){
                        System.out.println(index+"Train Id"+t.getTrainId());

                    }
                    break;
//                case 5:
//                    break;
//                case 6:
//                    break;
                case 7:
                    System.out.println("Bye now\nHave a good one...");
                    System.exit(0);
                    break;
            }

        }
    }
}
