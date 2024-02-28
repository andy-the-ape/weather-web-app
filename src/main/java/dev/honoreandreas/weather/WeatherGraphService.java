package dev.honoreandreas.weather;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherGraphService {
    @Autowired
    private WeatherRecordService weatherRecordService;
    public WeatherGraph createWeatherGraph(String startDate, String endDate) {
        Optional<List<WeatherRecord>> weatherRecords = weatherRecordService.allWeatherRecordsBetweenDates(startDate, endDate);
        if (weatherRecords.isPresent()) {
            List<WeatherRecord> records = weatherRecords.get();
            return new WeatherGraph(
                    new ObjectId(),
                    startDate,
                    endDate,
                    records
            );
        } else {
            return null;
        }
    }
}
