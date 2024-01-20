package mortgage;

import java.util.*;

public class MortgageLoader {
    public static final double DEBT_THRESHOLD = 0.01d;

    public List<Mortgage> loadAllMortgages() {
        List<Mortgage> result = new ArrayList<>();

        String currency = "GBP";
        double yearRate = 3.0f;


        double initialDebt = 600000;
        int initialDuration = 30*12; //months

        //mortgage #1
        Mortgage mortgage1 = new Mortgage(currency, yearRate, initialDebt, initialDuration, Collections.emptyMap());

        //mortgage #2:
        //add early payments every 12 months. The amount of stocks is the same
        //map <number of month, when early repayment is done> -> <sum of early repayment>, where 1 <= key < duration
        Map<Integer, Double> earlyRepayments2 = new LinkedHashMap<>();
        double earlyPaymentPerYear2 = 30000.0;
        for (int i = 12; i < initialDuration; i += 12) {
            earlyRepayments2.put(i, earlyPaymentPerYear2);
        }
        Mortgage mortgage2 = new Mortgage(currency, yearRate, initialDebt, initialDuration, earlyRepayments2);

        //mortgage #3:
        //add early payments every 12 months. The amount of stocks grows every year on k3
        //map <number of month, when early repayment is done> -> <sum of early repayment>, where 1 <= key < duration
        Map<Integer, Double> earlyRepayments3 = new LinkedHashMap<>();
        double k3 = 0.1; //this is coefficient of increasing of the stocks that the company gives to employees (every year)
        double earlyPaymentPerYear3 = 30000.0;
        for (int i = 12; i < initialDuration; i += 12) {
            earlyPaymentPerYear3 *= 1 + k3;
            earlyRepayments3.put(i, earlyPaymentPerYear3);
        }
        Mortgage mortgage3 = new Mortgage(currency, yearRate, initialDebt, initialDuration, earlyRepayments3);

        //mortgage #4:
        //add early payments every 3 months. The amount of stocks grows every year on k4
        //map <number of month, when early repayment is done> -> <sum of early repayment>, where 1 <= key < duration
        Map<Integer, Double> earlyRepayments4 = new LinkedHashMap<>();
        double k4 = 0.1; //this is coefficient of increasing of the stocks that the company gives to employees (every year)
        double earlyPaymentPerYear4 = 30000.0;
        for (int i = 3; i < initialDuration; i += 3) {
            if (i % 12 == 0) {
                earlyPaymentPerYear4 *= 1 + k4;
            }
            earlyRepayments4.put(i, earlyPaymentPerYear4/4);
        }
        Mortgage mortgage4 = new Mortgage(currency, yearRate, initialDebt, initialDuration, earlyRepayments4);

        //
        result.add(mortgage1);
        result.add(mortgage2);
        result.add(mortgage3);
        result.add(mortgage4);
        return result;
    }
}