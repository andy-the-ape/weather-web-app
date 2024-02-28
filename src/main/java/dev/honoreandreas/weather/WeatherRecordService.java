package dev.honoreandreas.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherRecordService {
    @Autowired
    private WeatherRecordRepository weatherRecordRepository;

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
}
