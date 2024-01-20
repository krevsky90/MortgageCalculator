package mortgage;

import java.util.List;
import java.util.Map;

import static mortgage.MortgageLoader.DEBT_THRESHOLD;
import static mortgage.MortgageUtils.formatMoney;

public class MortgageController {
    public void processBulk(List<Mortgage> mortgageList, boolean logging) {
        for (Mortgage mortgage : mortgageList) {
            process(mortgage, logging);
        }
    }

    /**
     * fill the mortgage main output characteristics:
     */
    public void process(Mortgage mortgage, boolean logging) {
        double monthRate = mortgage.getMonthRate();
        double initialDebt = mortgage.getInitialDebt();
        int initialDuration = mortgage.getInitialDuration();
        Map<Integer, Double> earlyRepayments = mortgage.getEarlyRepayments();

        double monthPayment = getMonthPayment(monthRate, initialDebt, initialDuration, false);
        mortgage.setInitialMonthPayment(monthPayment);

        if (logging) {
            System.out.println("===================== START PROCESSING =====================\n" + mortgage.toString());
            System.out.println("Initial monthPayment = " + formatMoney(monthPayment) + " " + mortgage.getCurrency());
        }

        int currentMonth = 1;
        while ((int)mortgage.getCurrentDebt() > DEBT_THRESHOLD*mortgage.getInitialDebt()) {
            mortgage.setCurrentDuration(currentMonth);

            if (earlyRepayments.containsKey(currentMonth)) {
                if (mortgage.addEarlyPayment(earlyRepayments.get(currentMonth), logging) == 1) {
                    break;
                }
                //recalculate monthPayment
                double newMonthPayment = getMonthPayment(monthRate, mortgage.getCurrentDebt(), initialDuration - currentMonth, logging);
                if (logging) {
                    System.out.println("Now month payment is decreased on " + formatMoney(monthPayment - newMonthPayment) + " " + mortgage.getCurrency());
                }
                monthPayment = newMonthPayment;
            }

            mortgage.addRegularPayment(monthPayment, logging);
            currentMonth++;
        }

        //it is supposed that we can do one extra pay = DEBT_THRESHOLD*initialDebt to finish the mortgage
        if (mortgage.getCurrentDebt() > 0) {
            mortgage.addEarlyPayment(mortgage.getCurrentDebt(), logging);
        }

        if (logging) {
            System.out.println("--------------- Final mortgage ---------------");
            System.out.println(mortgage.toString());
            System.out.println("===================== FINISH PROCESSING =====================\n");
        }
    }

    /**
     * looks like this is correct formula according to the example
     * from https://www.mtsbank.ru/articles/mozhno-li-dosrochno-pogasit-ipoteku/
     */
    public double getMonthPayment(double monthRate, double debt, int duration, boolean logging) {
        double totalRate = Math.pow(1 + monthRate, duration);
        double monthPayment = debt * monthRate * totalRate / (totalRate - 1);
        if (logging) {
            System.out.println(">>>>>  current monthPayment = " + (int) monthPayment);
        }
        return monthPayment;
    }
}