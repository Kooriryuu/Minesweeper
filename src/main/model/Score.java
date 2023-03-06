package model;

public class Score {

    private long time;
    private int height;
    private int width;

    public Score(long time, int h, int w) {
        height = h;
        width = w;
        this.time = time;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Score) {
            Score o = (Score) other;
            return (o.time == this.time && o.height == this.height && o.width == this.width);
        }
        return false;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long getTime() {
        return time;
    }


}
