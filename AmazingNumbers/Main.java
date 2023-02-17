import java.util.*;
import java.util.function.Function;

public class Main {

    static String instructions = """
                Welcome to Amazing Numbers!
                                  
                Supported requests:
                - enter a natural number to know its properties;
                - enter two natural numbers to obtain the properties of the list:
                  * the first parameter represents a starting number;
                  * the second parameter shows how many consecutive numbers are to be printed;
                - two natural numbers and properties to search for;
                - a property preceded by minus must not be present in numbers;
                - separate the parameters with one space;
                - enter 0 to exit.""";

    public enum PROPERTIES{
        EVEN(Main::isEven),
        ODD(Main::isOdd),
        BUZZ(Main::isBuzz),
        DUCK(Main::isDuck),
        PALINDROMIC(Main::isPalindromic),
        GAPFUL(Main::isGapful),
        SPY(Main::isSpy),
        SQUARE(Main::isSquare),
        SUNNY(Main::isSunny),
        JUMPING(Main::isJumping),
        HAPPY(Main::isHappy),
        SAD(Main::isSad);

        final private Function<Long, Boolean> func;

        PROPERTIES(Function<Long, Boolean> func){
            this.func = func;
        }

        public Function<Long, Boolean> getFunc(){
            return this.func;
        }

        public static PROPERTIES findByName(String name){
            for (PROPERTIES property : values()) {
                if (property.name().equalsIgnoreCase(name)) {
                    return property;
                }
            }
            return null;
        }
    }

    public static boolean isEven(long i){
        return i % 2 == 0;
    }

    public static boolean isDivisibleBy7(long i){
        return i % 7 == 0;
    }

    public static boolean endsWith7(long i){
        String iString = String.valueOf(i);
        return iString.charAt(iString.length() - 1) == '7';
    }

    public static boolean isDuck(long i){
        return String.valueOf(i).indexOf('0') != -1;
    }

    public static boolean isPalindromic(long i){
        String iString = String.valueOf(i);
        int left = 0, right = iString.length() - 1;
        while(left < right) {
            if(iString.charAt(left) != iString.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isGapful(long i) {
        String iString = String.valueOf(i);
        if(iString.length() < 3){
            return false;
        }
        String concatenatedString = "" + iString.charAt(0) + iString.charAt(iString.length() - 1);
        return i % Long.parseLong(concatenatedString) == 0;
    }

    public static boolean isSpy(long i) {
        long sum = 0, product = 1;
        while(i > 0){
            long digit = i % 10;
            sum += digit;
            product *= digit;
            i = i/10;
        }
        return sum == product;
    }

    public static boolean isSunny(long i) {
        return isSquare(i + 1);
    }

    public static boolean isSquare(long i) {
        double root = Math.sqrt(i);
        return ((root - Math.floor(root)) == 0);
    }

    public static boolean isBuzz(long i){
        return isDivisibleBy7(i) || endsWith7(i);
    }

    public static boolean isOdd(long i){
        return !isEven(i);
    }

    public static void printForSingleValue(long i) {
        System.out.println("Properties of " + i);
        System.out.println("\tbuzz: " + isBuzz(i));
        System.out.println("\tduck: " + isDuck(i));
        System.out.println("\tpalindromic: " + isPalindromic(i));
        System.out.println("\tgapful: " + isGapful(i));
        System.out.println("\tspy: " + isSpy(i));
        System.out.println("\tsquare: " + isSquare(i));
        System.out.println("\tsunny: " + isSunny(i));
        System.out.println("\tjumping: " + isJumping(i));
        System.out.println("\thappy: " + isHappy(i));
        System.out.println("\tsad: " + isSad(i));
        System.out.println("\teven: " + isEven(i));
        System.out.println("\todd: " + isOdd(i));
    }
    public static void printForMultipleValues(long i){
        System.out.println(i + " is "
                + (isEven(i) ? "even, " : "")
                + (isOdd(i) ? "odd, " : "")
                + (isBuzz(i) ? "buzz, " : "")
                + (isDuck(i) ? "duck, " : "")
                + (isPalindromic(i) ? "palindromic, " : "")
                + (isGapful(i) ? "gapful, " : "")
                + (isSpy(i) ? "spy, " : "")
                + (isSunny(i) ? "sunny, " : "")
                + (isSquare(i) ? "square, " : "")
                + (isJumping(i) ? "jumping, " : "")
                + (isHappy(i) ? "happy, " : "")
                + (isSad(i) ? "sad " : ""));
    }

    public static boolean isJumping(long num) {
        String str = String.valueOf(num);
        for(int i = 0; i < str.length() - 1; i++){
            int current = Character.getNumericValue(str.charAt(i));
            if(i > 0){
                int previous = Character.getNumericValue(str.charAt(i - 1));
                if(!(previous == current + 1 || previous == current - 1)){
                    return false;
                }
            }
            int next = Character.getNumericValue(str.charAt(i + 1));
            if(!(next == current + 1 || next == current - 1)){
                return false;
            }
        }
        return true;
    }

    public static boolean isHappy(long num) {
        Set<Long> unique_num = new HashSet<>();
        while (unique_num.add(num))
        {
            int value = 0;
            while (num > 0)
            {
                value += Math.pow(num % 10, 2);
                num /= 10;
            }
            num = value;
        }
        return num == 1;
    }

    public static boolean isSad(long i) {
        return !isHappy(i);
    }

    public static void printAvailableProperties(){
        System.out.print("Available properties: [");
        for (PROPERTIES value : PROPERTIES.values()) {
            System.out.print(value.name());
            if(!(value.ordinal() == PROPERTIES.values().length - 1)){
                System.out.print(", ");
            }
        }
        System.out.print("]\n");
    }

    public static void main(String[] args) {
        System.out.println(instructions);
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter a request: ");
            String str = scanner.nextLine();
            if (Objects.equals(str, "")) {
                System.out.println(instructions);
                continue;
            }
            long number;
            long consecutive = 0;
            ArrayList<String> properties = new ArrayList<>();
            ArrayList<String> arguments = new ArrayList<>(List.of(str.split(" ")));
            try {
                number = Long.parseLong(arguments.get(0));
            } catch (Exception ex) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }
            if (arguments.size() > 1) {
                try {
                    consecutive = Long.parseLong(arguments.get(1));
                } catch (Exception ex) {
                    System.out.println("The second parameter should be a natural number.");
                    continue;
                }
            }
            ArrayList<String> wrong = new ArrayList<>();
            if (arguments.size() > 2) {
                for (int i = 2; i < arguments.size(); i++) {
                    String value = arguments.get(i).toUpperCase();
                    PROPERTIES property = PROPERTIES.findByName(value);
                    if (property == null) {
                        if(value.charAt(0) == '-') {
                            property = PROPERTIES.findByName(value.substring(1));
                            if (property == null) {
                                wrong.add(value);
                            }
                            properties.add(value);
                        } else {
                            wrong.add(value);
                        }
                    } else {
                        properties.add(value);
                    }
                }
                if(wrong.size() > 1) {
                    System.out.print("The properties [");
                    for(String w : wrong) {
                        System.out.print(w);
                        if(!(wrong.indexOf(w) == wrong.size() - 1)) {
                            System.out.print(", ");
                        }
                    }
                    System.out.print("] are wrong.\n");
                    printAvailableProperties();
                    continue;
                } else if(wrong.size() == 1) {
                    System.out.println("The property [" + wrong.get(0) + "] is wrong.");
                    printAvailableProperties();
                    continue;
                }
                if (properties.contains("EVEN") && properties.contains("ODD")){
                    System.out.println("The request contains mutually exclusive properties: [EVEN, ODD]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if(properties.contains("-EVEN") && properties.contains("-ODD")){
                    System.out.println("The request contains mutually exclusive properties: [-EVEN, -ODD]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if(properties.contains("DUCK") && properties.contains("SPY")){
                    System.out.println("The request contains mutually exclusive properties: [DUCK, SPY]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if(properties.contains("-DUCK") && properties.contains("-SPY")){
                    System.out.println("The request contains mutually exclusive properties: [-DUCK, -SPY]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if (properties.contains("SUNNY") && properties.contains("SQUARE")) {
                    System.out.println("The request contains mutually exclusive properties: [SUNNY, SQUARE]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if (properties.contains("-SUNNY") && properties.contains("-SQUARE")) {
                    System.out.println("The request contains mutually exclusive properties: [-SUNNY, -SQUARE]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if (properties.contains("HAPPY") && properties.contains("SAD")) {
                    System.out.println("The request contains mutually exclusive properties: [HAPPY, SAD]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else if (properties.contains("-HAPPY") && properties.contains("-SAD")) {
                    System.out.println("The request contains mutually exclusive properties: [-HAPPY, -SAD]");
                    System.out.println("There are no numbers with these properties.");
                    continue;
                } else {
                    boolean exit = false;
                    for(PROPERTIES p : PROPERTIES.values()){
                        if(properties.contains(p.name()) && properties.contains("-" + p.name())){
                            System.out.println("The request contains mutually exclusive properties: [" + p.name() + ", -" + p.name() + "]");
                            System.out.println("There are no numbers with these properties.");
                            exit = true;
                            break;
                        }
                    }
                    if(exit){
                        continue;
                    }
                }
            }
            if (number < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
            } else if (consecutive < 0) {
                System.out.println("The second parameter should be a natural number.");
            } else if (number == 0) {
                System.out.println("Goodbye!");
                break;
            } else if (consecutive == 0) {
                printForSingleValue(number);
            } else if (properties.size() == 0) {
                for (long i = 0; i < consecutive; i++) {
                    long current = number + i;
                    printForMultipleValues(current);
                }
            } else {
                int i = 0;
                while (consecutive > 0) {
                    long current = number + i;
                    boolean fits = true;
                    for(String s : properties) {
                        PROPERTIES property = PROPERTIES.findByName(s);
                        if(property == null){
                            property = PROPERTIES.findByName(s.substring(1));
                            assert property != null;
                            if (property.getFunc().apply(current)) {
                                fits = false;
                                break;
                            }
                        } else {
                            if (!property.getFunc().apply(current)) {
                                fits = false;
                                break;
                            }
                        }
                    }
                    if(fits){
                        printForMultipleValues(current);
                        consecutive--;
                    }
                    i++;
                }
            }
        }
    }
}