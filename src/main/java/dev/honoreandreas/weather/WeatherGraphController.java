package dev.honoreandreas.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/weather/graph")
@CrossOrigin(origins="*")
public class WeatherGraphController {
    @Autowired
    private WeatherGraphService weatherGraphService;

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<WeatherGraph> getWeatherGraph(
            @PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return new ResponseEntity<WeatherGraph>(weatherGraphService.createWeatherGraph(startDate, endDate), HttpStatus.OK);
    }
}
