package reservit.ticketbooking;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public static void main(String args[]) throws JsonProcessingException {
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
            System.out.println("Option 1: Sign Up        Option 2: Sign In       Option 3: Fetch Bookings");
            System.out.println("Option 4: Search Trains  Option 5: Book a Seat   Option 6: Cancel my Booking");
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
                    int index = 1;
                    for (Train t: trains){
                        System.out.println(index+". Train Id: "+t.getTrainId());
                        index = index+1;
                    }
                    break;
                case 5:
                    System.out.println("book your seat");
                    System.out.println("Enter Train Id");
                    String trainId = scanner.next();
                    System.out.println("Type your source station");
                    source = scanner.next();
                    System.out.println("Type your destination station");
                    destination = scanner.next();
                    System.out.println("Enter your date of travel (DD-MM-YY)");
                    String travelDate = scanner.next();
                    System.out.println("Enter desired seat number)");
                    int seatNum = scanner.nextInt();
                    userBookingService.makeBooking(trainId,source,destination,travelDate, seatNum);
                    break;
                case 6:
                    System.out.println("Enter the ticked id to cancel your booking");
                    String tickedId = scanner.next();
                   if (userBookingService.cancelBooking(tickedId)){
                       System.out.println("Your booking is cancelled");
                   }
                    break;
                case 7:
                    System.out.println("Bye now\nHave a good one...");
                    option =7;
                    break;
            }

        }
    }
}
