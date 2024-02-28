package dev.honoreandreas.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/weather/graph")
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
