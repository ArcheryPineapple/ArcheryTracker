package evans.ben.archerytracker.scoring;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompletedRoundsDao {
    // For saving a new round from the scoring activity
    @Query("INSERT INTO CompletedRounds (date, roundName, round, arrowValues, totalScore, " +
            "archerString, scorerString) VALUES (:today, :roundName, :round, :arrowValues, " +
            ":totalScore, :archerString, :scorerString)")
    long saveCompletedRound(String today, String roundName, String round, String arrowValues,
                            int totalScore, String archerString, String scorerString);

    // Getting the CompletedRound object from the database
    @Query("SELECT * FROM CompletedRounds WHERE id = :id")
    CompletedRound getRound(Long id);

    // Updating the signatures
    @Query("UPDATE CompletedRounds SET archerString = :archerString, scorerString = :scorerString WHERE id = :id")
    void updateSignatures(Long id, String archerString, String scorerString);

    // Get all CompletedRounds
    @Query("SELECT * FROM CompletedRounds ORDER BY id DESC")
    List<CompletedRound> getAllRounds();

    // Get the PB for a given round
    @Query("SELECT totalScore FROM CompletedRounds WHERE roundName = :roundName ORDER BY totalScore DESC LIMIT 1")
    int getPB(String roundName);

    // Get list of unique rounds in the database
    @Query("SELECT DISTINCT roundName FROM CompletedRounds")
    List<String> getRounds();

    // Get the serialised round object searching by roundName
    @Query("SELECT round FROM CompletedRounds WHERE roundName = :roundName")
    String getRoundObject(String roundName);
}
