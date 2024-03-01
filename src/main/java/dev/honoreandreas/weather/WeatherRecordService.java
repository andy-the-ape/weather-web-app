package dev.honoreandreas.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherRecordService {
    @Autowired
    private WeatherRecordRepository weatherRecordRepository;
    @Autowired
    private final RestTemplate restTemplate = new RestTemplate();
    private final double latitude = WeatherLocation.ODENSE.getLatitude();
    private final double longitude = WeatherLocation.ODENSE.getLongitude();
    private final String EXTERNAL_API_URL
            = "https://api.openweathermap.org/data/2.5/weather?lat=" +
            latitude +
            "&lon=" +
            longitude +
            "&appid=2438ec868e96bc0d041dc6fde565f0b6";
    private WeatherRecord weatherRecordToSave;
    private WeatherRecord currentWeatherRecord;

    public List<WeatherRecord> allWeatherRecords() {
        return weatherRecordRepository.findAll();
    }
    public Optional<WeatherRecord> singleWeatherRecord(String date) {
        return weatherRecordRepository.findWeatherRecordByDate(date);
    }
    public Optional<List<WeatherRecord>> allWeatherRecordsBetweenDates(String startDate, String endDate) {
        return weatherRecordRepository.findWeatherRecordsByDateBetweenInclusiveSorted(
                startDate,
                endDate,
                Sort.by(Sort.Direction.ASC, "date")
        );
    }

    //This method runs every 5 minutes to gather data from the external weather API
    @Scheduled(cron = "0 */5 * * * *")
    public void fetchWeatherRecord() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(EXTERNAL_API_URL, String.class);
        String weatherDetailsJson = responseEntity.getBody();
        try {
            JsonNode weatherNode = new ObjectMapper().readTree(weatherDetailsJson);

            //Handling date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String currentDate = String.valueOf(java.time.LocalDate.now());
            String formattedDate = simpleDateFormat.format(currentDate);

            currentWeatherRecord = createWeatherRecord(
                    "Odense",
                    formattedDate,
                    weatherNode.get("weather").get(0).get("main").textValue(),
                    weatherNode.get("weather").get(0).get("description").textValue(),
                    weatherNode.get("main").get("temp").doubleValue(),
                    weatherNode.get("main").get("humidity").doubleValue(),
                    weatherNode.get("wind").get("speed").doubleValue(),
                    weatherNode.get("wind").get("deg").doubleValue()
                    );

            // Check if current time is after 2pm and before 6pm
            LocalTime timeNow = LocalTime.now();
            LocalTime lowerBoundTime = LocalTime.of(14, 0);
            LocalTime upperBoundTime = LocalTime.of(18, 0);
            if (timeNow.isAfter(lowerBoundTime) && timeNow.isBefore(upperBoundTime)) {
                //checking if today's entry of WeatherRecord already exists in the database
                if (weatherRecordRepository.findWeatherRecordByDate(currentDate).isEmpty()) {
                    persistWeatherRecord();
                }
            }
            System.out.println(weatherRecordToSave);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
        }
    }

    // This method saves data into the database once a day at 2 PM
    @Scheduled(cron = "0 14 * * * *") // Runs at 2 PM every day
    public void persistWeatherRecord() {
        // Check if there is data to save
        if (currentWeatherRecord != null) {
            weatherRecordRepository.save(currentWeatherRecord);
        }
    }
    public WeatherRecord createWeatherRecord(
            String location,
            String date,
            String weatherTitle,
            String description,
            double temperature,
            double humidity,
            double windSpeed,
            double windDirection
            ) {
        WeatherRecord weatherRecord = new WeatherRecord(
                new ObjectId(),
                location,
                date,
                weatherTitle,
                description,
                temperature,
                humidity,
                windSpeed,
                windDirection,
                null,
                null
                );
        return weatherRecordRepository.save(weatherRecord);
    }
}
