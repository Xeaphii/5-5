package com.bamafolks.android.games.tictic;

/**
 * Created by Sunny on 3/23/2015.
 */
public class Line {
    int StartIndex;

    Line(int startIndex,int endIndex) {
        this.setStartIndex(startIndex);
        this.setEndIndex(endIndex);
    }

    public int getEndIndex() {
        return EndIndex;
    }

    public void setEndIndex(int endIndex) {
        EndIndex = endIndex;
    }

    public int getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(int startIndex) {
        StartIndex = startIndex;
    }

    int EndIndex;
}
