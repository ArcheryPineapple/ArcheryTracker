package evans.ben.archerytracker.scoring;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.util.List;

public class Round implements Parcelable {
    // Round name
    private String roundName;
    /* Scoring type: 0 - metric outdoors, 1 - imperial outdoors, 2 - indoors full,
       3 - indoors 3 spot, 4 - worcester */
    private int scoringType;
    // List of distances for the round include unit e.g. 90m or 100yd
    private List<String> distances;
    // List of number of arrows shot at each distance
    private List<String> arrowsDistance;
    // Arrows per an end
    private int arrowsPerEnd;

    // Constructor
    public Round(String roundName, int scoringType, List<String> distances, List<String> arrowsDistance, int arrowsPerEnd) {
        this.roundName = roundName;
        this.scoringType = scoringType;
        this.distances = distances;
        this.arrowsDistance = arrowsDistance;
        this.arrowsPerEnd = arrowsPerEnd;
    }

    protected Round(Parcel in) {
        roundName = in.readString();
        scoringType = in.readInt();
        distances = in.createStringArrayList();
        arrowsDistance = in.createStringArrayList();
        arrowsPerEnd = in.readInt();
    }

    public static final Creator<Round> CREATOR = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };

    // Getters
    public String getRoundName() {
        return roundName;
    }
    public int getScoringType() {
        return scoringType;
    }
    public List<String> getDistances() {
        return distances;
    }
    public List<String> getArrowsDistance() {
        return arrowsDistance;
    }
    public int getArrowsPerEnd() {
        return arrowsPerEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(roundName);
        parcel.writeInt(scoringType);
        parcel.writeStringList(distances);
        parcel.writeStringList(arrowsDistance);
        parcel.writeInt(arrowsPerEnd);
    }
}
