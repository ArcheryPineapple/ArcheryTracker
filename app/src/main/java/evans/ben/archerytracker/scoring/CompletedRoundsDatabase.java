package evans.ben.archerytracker.scoring;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CompletedRound.class}, version = 1)
public abstract class CompletedRoundsDatabase extends RoomDatabase {
    public abstract CompletedRoundsDao completedRoundsDao();
}
