package evans.ben.archerytracker.coachingnotes;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
     @Query("INSERT INTO Notes (date, title, content) VALUES (:today, '', '')")
     long createNote(String today);

     @Query("SELECT * FROM Notes ORDER BY id DESC")
     List<Note> getAllNotes();

     @Query("UPDATE Notes SET title = :title, content = :content WHERE id = :id")
     void saveNote(long id, String title, String content);

    @Query("DELETE FROM Notes WHERE id = :id")
    void deleteNote(long id);
}
