package application.model;

import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

import static application.Supermarket.*;

public class PeriodCounter extends Thread {
    double totalUtilization = 0;
    public void run() {

        while (true) {
            synchronized (WORKING_OBJECT){
                while(WORKING_FLAG == false){
                    try {
                        WORKING_OBJECT.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    WORKING_OBJECT.notifyAll();

                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SUPERMARKET_WORKING_TIME++;
            LEFT_CUSTOMER_LIST.add(new LeftCustomerTuple(LEFT_CUSTOMER_NUMBER, SUPERMARKET_WORKING_TIME));
            for (CheckoutTill checkoutTill : CHECKOUT_TILL_LIST) {
                if (checkoutTill.isWorkingStatus() == false) {
                    if (checkoutTill.getStopWatch().isRunning() == false) {
                        checkoutTill.getStopWatch().start();
                    }
                }
                else {
                    try {
                        checkoutTill.getStopWatch().stop();
                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
                    }
                }
                AVERAGE_WAIT_TIME = TOTAL_WAIT_TIME/ENQUEUE_REQUESTED;
                AVERAGE_WAIT_TIME = Math.floor(AVERAGE_WAIT_TIME) / 1000;
                double idleTime = checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS);
                double supermarketWorkingTime = SUPERMARKET_WORKING_TIME;
                double runningTime = supermarketWorkingTime - idleTime;
                double utilzation = runningTime/SUPERMARKET_WORKING_TIME;
                utilzation = Math.floor(utilzation * 10000) / 100;
                checkoutTill.setUtilization(utilzation);
                totalUtilization += utilzation;

                String runningTimeString = String.valueOf(runningTime);
                String utilizationString = String.valueOf(utilzation);
                Platform.runLater(new MyRunnable(checkoutTill) {
                    @Override
                    public void run() {

                        if (checkoutTill.getCheckoutId() == 1) {

                            LABEL_UTIL_1.setText("Till 1 runs for " + runningTimeString + " \tUtilization: "+ utilizationString +"%");
                        }
                        if (checkoutTill.getCheckoutId() == 2) {
                            LABEL_UTIL_2.setText("Till 2 runs for " + runningTimeString + " \tUtilization: "+ utilizationString+"%");
                        }
                        if (checkoutTill.getCheckoutId() == 3) {
                            LABEL_UTIL_3.setText("Till 3 runs for " + runningTimeString + " \tUtilization: "+ utilizationString+"%");
                        }
                        if (checkoutTill.getCheckoutId() == 4) {
                            LABEL_UTIL_4.setText("Till 4 runs for " + runningTimeString + " \tUtilization: "+ utilizationString+"%");
                        }
                        if (checkoutTill.getCheckoutId() == 5) {
                            LABEL_UTIL_5.setText("Till 5 runs for " + runningTimeString + " \tUtilization: "+ utilizationString+"%");
                        }
                        if (checkoutTill.getCheckoutId() == 6) {
                            LABEL_UTIL_6.setText("Till 6 runs for " + runningTimeString + " \tUtilization: "+ utilizationString+"%");
                        }
                        LABEL_WORKING_TIME.setText("Supermarket opened: "+String.valueOf(SUPERMARKET_WORKING_TIME));
                        LABEL_AVERAGE_CUSTOMER_WAIT_TIME.setText("Average wait time " + String.valueOf(AVERAGE_WAIT_TIME));
                        AVERAGE_UTIL = Math.floor(totalUtilization/600/SUPERMARKET_WORKING_TIME * 10000)/100;

                        LABEL_AVERAGE_UTIL.setText("Average utilization " + String.valueOf(AVERAGE_UTIL) +"%");
                    }
                });


            }

        }
    }
}
