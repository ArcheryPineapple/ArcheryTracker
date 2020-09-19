package evans.ben.archerytracker.sightmarks;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;


// This is where we define the SQL queries we want to implement
@Dao
public interface SightMarksDao {

    @Query("INSERT INTO SightMarks (dist, unit, mark) VALUES ('', '', '')")
    long createSightMark();

    @Query("SELECT * FROM SightMarks ORDER BY unit, dist")
    List<SightMark> getAllSightMarks();

    @Query("UPDATE SightMarks SET dist = :dist, unit = :unit, mark = :mark WHERE id = :id")
    void saveSightMark(long id, int dist, String unit, String mark);

    @Query("DELETE FROM SightMarks WHERE id = :id")
    void deleteSightMark(long id);
}
