package application.model;

public class LeftCustomerTuple {
    private int numberOfLeft = 0;
    private int second;

    public LeftCustomerTuple(int numberOfLeft, int second) {
        this.numberOfLeft = numberOfLeft;
        this.second = second;
    }

    public int getNumberOfLeft() {
        return numberOfLeft;
    }

    public void setNumberOfLeft(int numberOfLeft) {
        this.numberOfLeft = numberOfLeft;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
