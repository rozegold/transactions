package com.transactionandstatistics.application.model;

import org.springframework.stereotype.Component;

@Component
public class Statistics {

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public Statistics() {
    }

    public Statistics(double sum, double max, double min, long count, double avg) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
        this.avg = avg;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistics statistics = (Statistics) o;

        if (Double.compare(statistics.sum, sum) != 0) return false;
        if (Double.compare(statistics.avg, avg) != 0) return false;
        if (Double.compare(statistics.max, max) != 0) return false;
        if (Double.compare(statistics.min, min) != 0) return false;
        return count == statistics.count;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(sum);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(avg);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(min);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }
}
