package evans.ben.archerytracker.sightmarks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Making an SQL table called SightMarks
@Entity(tableName = "SightMarks")
public class SightMark {
    // Defining columns in the table
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "dist")
    public int dist;

    @ColumnInfo(name = "unit")
    public String unit;

    @ColumnInfo(name = "mark")
    public String mark;
}
