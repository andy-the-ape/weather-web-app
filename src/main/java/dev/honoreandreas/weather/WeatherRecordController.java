package dev.honoreandreas.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins="*")
public class WeatherRecordController {
    @Autowired
    private WeatherRecordService weatherRecordService;
    @GetMapping
    public ResponseEntity<List<WeatherRecord>> getAllWeatherRecords() {
        return new ResponseEntity<>(weatherRecordService.allWeatherRecords(), HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<Optional<WeatherRecord>> getCurrentWeatherRecord() {
        return new ResponseEntity<>(Optional.ofNullable(weatherRecordService.getCurrentWeatherRecord()), HttpStatus.OK);
    }

    @GetMapping("/{date}")
    public ResponseEntity<Optional<WeatherRecord>> getSingleWeatherRecordByDate(@PathVariable String date) {
        return new ResponseEntity<>(weatherRecordService.singleWeatherRecord(date), HttpStatus.OK);
    }
}
