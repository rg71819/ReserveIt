package reservit.ticketbooking.services;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        userList=loadUsers();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        userList = loadUsers();
    }

    public Boolean loginUser(User user){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),  user1.getHashedPassword());
         }).findFirst();
        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return foundUser.isPresent();
        }
        return false;
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
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }else{
            System.out.println("User doesn't exist");
        }
    }


    public List<Train> getTrains(String source, String destination) {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public Boolean cancelBooking(String ticketId) throws JsonProcessingException {
        //need to check if this works
        if (user == null) {
            System.out.println("No user is logged in.");
            return Boolean.FALSE;
        }

        List<Ticket> ticketsBooked = user.getTicketsBooked();

        boolean removed = ticketsBooked.removeIf(ticket -> ticket.getTicketId()!=null && ticket.getTicketId().equals(ticketId));
        if (removed) {
            try {
                saveUserListToFile();
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

    public Boolean makeBooking(String trainId, String source, String destination, String dateOfTravel, int seatNum)  {
        List<Train> availableTrains = getTrains(source, destination);
        boolean equals = false;
        Train trainToBook = null;
        for (Train t: availableTrains){
            equals = t.getTrainId().equals(trainId);
            if(equals){
                trainToBook = t;
                break;
            }
        }
        if(equals){
            System.out.println(trainToBook.getSeats());
            int row = trainToBook.getSeats().size();
            int col = trainToBook.getSeats().get(0).size();
            int seatNumRow = seatNum/col;
            int seatNumCol = seatNum%col;
            List<Ticket> bookedTicketsList = user.getTicketsBooked();
            Integer a = (int)(Math.random()*1000);
            if(seatNumRow>=0 && seatNumCol>=0 && seatNumRow<row && seatNumCol<col && trainToBook.getSeats().get(seatNumRow).get(seatNumCol) != 1){
                trainToBook.getSeats().get(seatNumRow).set(seatNumCol,1);
            }else{
                System.out.println("Seat not available to book");
                return Boolean.FALSE;
            }

            bookedTicketsList.add(new Ticket(a.toString(), user.getUserId(), source, destination, dateOfTravel, trainToBook));
            user.setTicketsBooked(bookedTicketsList);
            try {
                saveUserListToFile();
                return Boolean.TRUE;
            } catch (IOException e) {
                System.out.println("Something went wrong..");
                System.out.println(e);
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }
}
