package dev.honoreandreas.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Getter
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
            "&appid=2438ec868e96bc0d041dc6fde565f0b6&units=metric&lang=da";
    private WeatherRecord currentWeatherRecord;

    public Optional<List<WeatherRecord>> allWeatherRecords() {
        return Optional.of(weatherRecordRepository.findAll());
    }
    public Optional<WeatherRecord> singleWeatherRecord(String date) {
        return weatherRecordRepository.findByDate(date);
    }
    public Optional<List<WeatherRecord>> allWeatherRecordsBetweenDates(String startDate, String endDate) {
        return weatherRecordRepository.findWeatherRecordsByDateBetweenInclusiveSorted(
                startDate,
                endDate,
                Sort.by(Sort.Direction.ASC, "date")
        );
    }

    //This method runs every 5 minutes to gather data from the external weather API
//    @Scheduled(cron = "0 */5 * * * *")
    @Scheduled(fixedDelay = 10000)
    public void fetchWeatherRecord() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(EXTERNAL_API_URL, String.class);
        String weatherDetailsJson = responseEntity.getBody();
        try {
            JsonNode weatherNode = new ObjectMapper().readTree(weatherDetailsJson);

            //Handling date format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = LocalDate.now().format(dateFormatter);

            currentWeatherRecord = createWeatherRecord(
                    "Odense",
                    formattedDate,
                    weatherNode.get("weather").get(0).get("id").intValue(),
                    weatherNode.get("weather").get(0).get("main").textValue(),
                    weatherNode.get("weather").get(0).get("description").textValue(),
                    weatherNode.get("weather").get(0).get("icon").textValue(),
                    weatherNode.get("main").get("temp").doubleValue(),
                    weatherNode.get("main").get("humidity").doubleValue(),
                    weatherNode.get("wind").get("speed").doubleValue(),
                    weatherNode.get("wind").get("deg").doubleValue()
                    );

            // Check if current time is after 2pm and before 6pm
            LocalTime timeNow = LocalTime.now();
            LocalTime lowerBoundTime = LocalTime.of(9, 0);
            LocalTime upperBoundTime = LocalTime.of(21, 0);
            if (timeNow.isAfter(lowerBoundTime) && timeNow.isBefore(upperBoundTime)) {
                addWeatherRecordToDB(currentWeatherRecord);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
        }
    }

    // This method saves data into the database once a day at 2 PM
    @Scheduled(cron = "0 14 * * * *") // Runs at 2 PM every day
    public void persistWeatherRecord() {
        addWeatherRecordToDB(currentWeatherRecord);

    }
    public void addWeatherRecordToDB(WeatherRecord weatherRecord) {
        // Check if there is data to save
        if (weatherRecord != null) {
            //checking if today's entry of WeatherRecord is already persisted
            Optional<WeatherRecord> existingRecord = weatherRecordRepository.findByDate(weatherRecord.getDate());
            if (existingRecord.isEmpty()) {
                weatherRecordRepository.save(weatherRecord);
            }
        }
    }
    public WeatherRecord createWeatherRecord(
            String location,
            String date,
            int weatherId,
            String weatherTitle,
            String weatherDescription,
            String weatherIconId,
            double temperature,
            double humidity,
            double windSpeed,
            double windDirection
            ) {
        return new WeatherRecord(
                new ObjectId(),
                location,
                date,
                weatherId,
                weatherTitle,
                weatherDescription,
                weatherIconId,
                temperature,
                humidity,
                windSpeed,
                windDirection
                );
    }

}
