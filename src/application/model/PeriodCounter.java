package application.model;

import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

import static application.Supermarket.*;

public class PeriodCounter extends Thread {
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SUPERMARKET_WORKING_TIME++;
            for (CheckoutTill checkoutTill : CHECKOUT_TILL_LIST) {
                if (checkoutTill.isWorkingStatus() == false) {
                    if (checkoutTill.getStopWatch().isRunning() == false) {
                        checkoutTill.getStopWatch().start();
                    }
                } else {
                    try {
                        checkoutTill.getStopWatch().stop();
                    } catch (Exception e) {

                    }
                }

                Platform.runLater(new MyRunnable(checkoutTill) {
                    @Override
                    public void run() {
                        if (checkoutTill.getCheckoutId() == 1) {
                            double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                            double runningTime = SUPERMARKET_WORKING_TIME - idleTime;
                            double utilzation = runningTime/SUPERMARKET_WORKING_TIME;

                            String runningTimeString = String.valueOf(runningTime);
                            String utilizationString = String.valueOf(utilzation);
                            LABEL_UTIL_1.setText("Till 1 running for " + runningTimeString + " \tUtilization: "+ utilizationString);
                        }
                        if (checkoutTill.getCheckoutId() == 2) {
                            double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                            double runningTime = SUPERMARKET_WORKING_TIME - idleTime;
                            double utilzation = runningTime/SUPERMARKET_WORKING_TIME;

                            String runningTimeString = String.valueOf(runningTime);
                            String utilizationString = String.valueOf(utilzation);
                            LABEL_UTIL_2.setText("Till 2 running for " + runningTimeString + " \tUtilization: "+ utilizationString);
                        }
                        if (checkoutTill.getCheckoutId() == 3) {
                            double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                            double runningTime = SUPERMARKET_WORKING_TIME - idleTime;
                            double utilzation = runningTime/SUPERMARKET_WORKING_TIME;

                            String runningTimeString = String.valueOf(runningTime);
                            String utilizationString = String.valueOf(utilzation);
                            LABEL_UTIL_3.setText("Till 3 running for " + runningTimeString + " \tUtilization: "+ utilizationString);
                        }
                        if (checkoutTill.getCheckoutId() == 4) {
                            double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                            double runningTime = SUPERMARKET_WORKING_TIME - idleTime;
                            double utilzation = runningTime/SUPERMARKET_WORKING_TIME;

                            String runningTimeString = String.valueOf(runningTime);
                            String utilizationString = String.valueOf(utilzation);
                            LABEL_UTIL_4.setText("Till 4 running for " + runningTimeString + " \tUtilization: "+ utilizationString);
                        }
                        if (checkoutTill.getCheckoutId() == 5) {
                            double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                            double runningTime = SUPERMARKET_WORKING_TIME - idleTime;
                            double utilzation = runningTime/SUPERMARKET_WORKING_TIME;

                            String runningTimeString = String.valueOf(runningTime);
                            String utilizationString = String.valueOf(utilzation);
                            LABEL_UTIL_5.setText("Till 5 running for " + runningTimeString + " \tUtilization: "+ utilizationString);
                        }
                        if (checkoutTill.getCheckoutId() == 6) {
                            double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                            double runningTime = SUPERMARKET_WORKING_TIME - idleTime;
                            double utilzation = runningTime/SUPERMARKET_WORKING_TIME;

                            String runningTimeString = String.valueOf(runningTime);
                            String utilizationString = String.valueOf(utilzation);
                            LABEL_UTIL_6.setText("Till 6 running for " + runningTimeString + " \tUtilization: "+ utilizationString);
                        }
                        LABEL_WORKING_TIME.setText("Supermarket opened: "+String.valueOf(SUPERMARKET_WORKING_TIME));
                    }
                });


            }
        }
    }
}
