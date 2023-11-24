package maxOnline;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Stream;

public class MaxOnline {
    static final int startYear = 2020;
    static final int endYear = 2022;
    static final LocalDate startDate = LocalDate.of(startYear, 01, 01);
    static final LocalDate endDate = LocalDate.of(endYear, 12, 31);

    private TreeMap<LocalDate, Integer> periodOfActivity = new TreeMap<>();

    public static void main(String[] args) {
        MaxOnline main = new MaxOnline();
        main.findMaxOnlineDate();
        System.out.println();
        main.findMaxOnline();
    }

    public int findMaxOnline() {
        long m = System.currentTimeMillis();
        // Найти самое большое количество онлайна одновременно
        Random random = new Random();
        int i = random.nextInt(4, 1000);
        List onlineList = Stream.generate(MaxOnline::generateUser).limit(i).toList();

        fillMapWithPeriodOfActivities(onlineList);

        Map.Entry<LocalDate, Integer> maxEntry = periodOfActivity.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElse(null);
        System.out.println("Date of max online is: \n" + maxEntry.getKey() + " - " + maxEntry.getValue() + " users");

        long s = System.currentTimeMillis();
        System.out.println("Time spent for algoritm " + (s - m) + "ms");

        periodOfActivity = new TreeMap<>();
        return maxEntry.getValue();
    }

    public LocalDate findMaxOnlineDate() {
        long m = System.currentTimeMillis();

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)
        Random random = new Random();

        int i = random.nextInt(4, 1000);
        List<UserOnline> onlineList = Stream.generate(MaxOnline::generateUser).limit(i).toList();

        fillMapWithPeriodOfActivities(onlineList);

        int max = periodOfActivity.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElse(null)
                .getValue();

        findMaxPeriodOfActivity(max);

        long s = System.currentTimeMillis();
        System.out.println("Time spent for algoritm: " + (s - m) + "ms");
        periodOfActivity = new TreeMap<>();
        return null;
    }

    private void fillMapWithPeriodOfActivities(List<UserOnline> onlineList) {
        for (UserOnline user : onlineList) {
            long daysOfUserSession = java.time.temporal.ChronoUnit.DAYS.between(user.getStartSession(), user.getEndSession());

            for (long j = 0; j < daysOfUserSession; j++) {
                if (!periodOfActivity.containsKey(user.getStartSession().plusDays(j))) {
                    periodOfActivity.put(user.getStartSession().plusDays(j), 1);
                }
                int value = periodOfActivity.get(user.getStartSession().plusDays(j)) + 1;
                periodOfActivity.put(user.getStartSession().plusDays(j), value);
            }
        }
    }

    private void findMaxPeriodOfActivity(int max) {
        List<List<LocalDate>> periodsOfMaxActivities = new ArrayList<>();
        boolean maxPeriodIsGoingOn = false;
        LocalDate firstDateOfMaxPeriod = null;
        LocalDate lastDateOfMaxPeriod = null;
        List<LocalDate> period = new ArrayList<>();

        for (Map.Entry<LocalDate, Integer> entry: periodOfActivity.entrySet()) {
            int temp = entry.getValue();
            if (temp == max) {
                if (!maxPeriodIsGoingOn) {
                    maxPeriodIsGoingOn = true;
                    firstDateOfMaxPeriod = entry.getKey();
                }
                lastDateOfMaxPeriod = entry.getKey();

            } else if (maxPeriodIsGoingOn) {
                maxPeriodIsGoingOn = false;          //change marker
                period.add(firstDateOfMaxPeriod);    // create period
                period.add(lastDateOfMaxPeriod);
                periodsOfMaxActivities.add(period);  //add this period to the list
                period = new ArrayList<>();          //update list for new period
            }
        }
        System.out.println("Dates of maximum user's activity:");
        if (periodsOfMaxActivities.isEmpty()) {
            System.out.println("activities not found");
        }
        for (List<LocalDate> date : periodsOfMaxActivities) {
            for (int j = 0; j < date.size(); j++) {
                if (j == 0) {
                    System.out.printf(date.get(j) + " - ");
                } else {
                    System.out.printf(date.get(j).toString());
                }

            }
            System.out.printf(" " + max + " users");
            System.out.println();
        }
    }

    public static UserOnline generateUser() {
        Random random = new Random();
        int randomYearStart = random.nextInt(startYear, endYear);
        int randomMonthStart = random.nextInt(1, 12);
        int randomDayStart = random.nextInt(1, Month.of(randomMonthStart).maxLength());
        int randomYearEnd = random.nextInt(startYear, endYear);
        int randomMonthEnd = random.nextInt(1, 12);
        int randomDayEnd = random.nextInt(1, Month.of(randomMonthEnd).maxLength());
        LocalDate startUserOnline = LocalDate.of(randomYearStart, randomMonthStart, randomDayStart);
        LocalDate endUserOnline = LocalDate.of(randomYearEnd, randomMonthEnd, randomDayEnd);
        if (startUserOnline.isAfter(endUserOnline)) {
            LocalDate temp = startUserOnline;
            startUserOnline = endUserOnline;
            endUserOnline = temp;
        }
        return new UserOnline(startUserOnline, endUserOnline);
    }
}