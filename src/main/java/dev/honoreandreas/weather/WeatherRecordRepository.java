package dev.honoreandreas.weather;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRecordRepository extends MongoRepository<WeatherRecord, ObjectId> {
    Optional<WeatherRecord> findByDate(String date);

    @Query("{$and: [{'date': {$gte: ?0}}, {'date': {$lte: ?1}}]}")
    Optional<List<WeatherRecord>> findWeatherRecordsByDateBetweenInclusiveSorted(String startDate, String endDate, Sort sort);
}
