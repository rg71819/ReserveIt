package reservit.ticketbooking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import reservit.ticketbooking.entities.Ticket;
import reservit.ticketbooking.entities.Train;
import reservit.ticketbooking.entities.User;
import reservit.ticketbooking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String USERS_PATH="app/src/main/java/reservit/ticketbooking/localDb/users.json";

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        return OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});
    }

    public UserBookingService() throws IOException{
        loadUsers();
    }

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        userList = loadUsers();
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),  user1.getPassword());
         }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch(IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        OBJECT_MAPPER.writeValue(usersFile, userList);
    }

    public void fetchBooking(){
        user.printTickets();
    }
    private  void saveTicketsToFile(List<Ticket> ticketsBooked) throws IOException{
        File usersFile = new File(USERS_PATH);
        OBJECT_MAPPER.writeValue(usersFile, ticketsBooked);
    }

    public List<Train> getTrains(String source, String destination) {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException e) {
            System.out.println("Something went wrong....");
            return new ArrayList<>();
        }
    }

    public Boolean cancelBooking(String ticketId){
        //need to check if this works
        List<Ticket> ticketsBooked = user.getTicketsBooked();
        boolean removed = ticketsBooked.removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        if (removed) {
            try {
                saveTicketsToFile(ticketsBooked);
                return Boolean.TRUE;
            } catch (IOException e) {
                System.out.println("Something went wrong..");
                System.out.println(e);
                return Boolean.FALSE;
            }
        }else{
            System.out.println("No ticket found with ID " + ticketId);
        }
        return Boolean.FALSE;
    }
}
