package evans.ben.archerytracker.sightmarks;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import evans.ben.archerytracker.sightmarks.SightMark;
import evans.ben.archerytracker.sightmarks.SightMarksDao;

// Need to define the database for SQL to use
@Database(entities = {SightMark.class}, version = 1)
public abstract class SightMarksDatabase extends RoomDatabase {
    public abstract SightMarksDao sightMarksDao();
}

