package reservit.ticketbooking;

import reservit.ticketbooking.services.UserBookingService;

import java.io.IOException;
import java.util.Scanner;

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
            System.out.println("Something Went wrong");
            System.out.println(e);
        }
        while(option!=7){
            System.out.println("Choose an Option");
            System.out.println("Option 1: Sign Up");
            System.out.println("Option 1: Sign In");

        }
    }
}
