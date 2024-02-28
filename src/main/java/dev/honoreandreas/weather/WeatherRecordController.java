package dev.honoreandreas.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherRecordController {
    @Autowired
    private WeatherRecordService weatherRecordService;
    @GetMapping
    public ResponseEntity<List<WeatherRecord>> getAllWeatherRecords() {
        return new ResponseEntity<List<WeatherRecord>>(weatherRecordService.allWeatherRecords(), HttpStatus.OK);
    }
    @GetMapping("/{date}")
    public ResponseEntity<Optional<WeatherRecord>> getSingleWeatherRecord(@PathVariable String date) {
        return new ResponseEntity<Optional<WeatherRecord>>(weatherRecordService.singleWeatherRecord(date), HttpStatus.OK);
    }
}
