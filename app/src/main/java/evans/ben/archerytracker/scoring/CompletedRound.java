package evans.ben.archerytracker.scoring;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "CompletedRounds")
public class CompletedRound {
    // Fields for the object
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "roundName")
    public String roundName;

    // This is a serialised version of the round object created using gson
    @ColumnInfo(name = "round")
    public String round;

    // This is a serialised version of the List<Integer> object created using gson
    @ColumnInfo(name = "arrowValues")
    public String arrowValues;

    @ColumnInfo(name = "totalScore")
    public int totalScore;

    @ColumnInfo(name = "archerString")
    public String archerString;

    @ColumnInfo(name = "scorerString")
    public String scorerString;

}
