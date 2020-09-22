package evans.ben.archerytracker.scoring.input;

import android.os.Parcel;
import android.os.Parcelable;

// This class holds the data which is sent from the RoundAdapter to the ScoringActivity
public class Distance implements Parcelable {
    // Distance
    private String distance;
    // Number of arrows shot at this distance
    private String arrowsAtDistance;
    // Scoring style (same numbering format as Round class
    private int scoringStyle;
    // Arrows per end
    private int arrowsEnd;
    // Distance value for ongoing rounds
    private int distanceValue;

    // Constructor
    public Distance(String distance, String arrowsAtDistance, int scoringStyle, int arrowsEnd, int distanceValue) {
        this.distance = distance;
        this.arrowsAtDistance = arrowsAtDistance;
        this.scoringStyle = scoringStyle;
        this.arrowsEnd = arrowsEnd;
        this.distanceValue = distanceValue;
    }

    protected Distance(Parcel in) {
        distance = in.readString();
        arrowsAtDistance = in.readString();
        scoringStyle = in.readInt();
        arrowsEnd = in.readInt();
        distanceValue = in.readInt();
    }

    public static final Creator<Distance> CREATOR = new Creator<Distance>() {
        @Override
        public Distance createFromParcel(Parcel in) {
            return new Distance(in);
        }

        @Override
        public Distance[] newArray(int size) {
            return new Distance[size];
        }
    };

    // Getters
    public String getDistance() {
        return distance;
    }
    public String getArrowsAtDistance() {
        return arrowsAtDistance;
    }
    public int getScoringStyle() {
        return scoringStyle;
    }
    public int getArrowsEnd(){
        return arrowsEnd;
    }

    public int getDistanceValue() {
        return distanceValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(distance);
        parcel.writeString(arrowsAtDistance);
        parcel.writeInt(scoringStyle);
        parcel.writeInt(arrowsEnd);
        parcel.writeInt(distanceValue);
    }
}
