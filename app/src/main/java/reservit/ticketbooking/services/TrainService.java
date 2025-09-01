package reservit.ticketbooking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import reservit.ticketbooking.entities.Train;
import reservit.ticketbooking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {
    private Train train;
    private List<Train> trains;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final static String TRAINS_PATH = "../localDb/trains.json";


    public List<Train> searchTrains(String source, String destination) throws IOException {
        List<Train> trainsList = loadTrains();
        return trainsList.stream().filter(train -> validTrain(train,source,destination)).collect(Collectors.toList());
    }

    public boolean validTrain(Train train, String source, String destination){
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    public List<Train> loadTrains() throws IOException {
        File trains = new File(TRAINS_PATH);
        return objectMapper.readValue(trains,new TypeReference<List<Train>>() {});
    }
}
