import java.util.Scanner;

import static java.lang.System.exit;

enum Coffee{
    espresso(1, 250, 0, 16, 4),
    latte(2, 350, 75, 20, 7),
    cappuccino(3, 200, 100, 12, 6);
    final int number;
    final int water;
    final int milk;
    final int coffeeBeans;
    final int money;
    Coffee(int number, int water, int milk, int coffeeBeans, int money){
        this.number = number;
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.money = money;
    }
    public static Coffee getByNumber(int i){
        for(Coffee c : values()){
            if(c.number == i){
                return c;
            }
        }
        return null;
    }
}
enum CoffeeMachineState{
    prompt,
    choosingAction,
    choosingCoffee,
    fillingWater,
    fillingMilk,
    fillingBeans,
    fillingCups;
}
class CoffeeMachine{
    int water;
    int milk;
    int coffeeBeans;
    int cups;
    int money;
    CoffeeMachineState state = CoffeeMachineState.choosingAction;
    CoffeeMachine(int water, int milk, int coffeeBeans, int cups, int money){
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.cups = cups;
        this.money = money;
    }
    public boolean canMakeCoffee(Coffee c){
        if(this.water < c.water){
            System.out.println("Sorry, not enough water!");
            return false;
        } else if (this.milk < c.milk){
            System.out.println("Sorry, not enough milk!");
            return false;
        } else if (this.coffeeBeans < c.coffeeBeans) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        } else if (this.cups < 0) {
            System.out.println("Sorry, not enough cups!");
            return false;
        }
        return true;
    }
    public void displayStock(){
        System.out.println("The coffee machine has:");
        System.out.println(this.water + " ml of water");
        System.out.println(this.milk + " ml of milk");
        System.out.println(this.coffeeBeans + " g of coffee beans");
        System.out.println(this.cups + " disposable cups");
        System.out.println("$" + this.money + " of money");
    }
    public void makeCoffee(Coffee coffee){
        this.water -= coffee.water;
        this.milk -= coffee.milk;
        this.coffeeBeans -= coffee.coffeeBeans;
        this.cups -= 1;
        this.money += coffee.money;
    }
    public void operate(String input){
        if(state == CoffeeMachineState.choosingAction){
            if (input.equals("BUY")) {
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
                this.state = CoffeeMachineState.choosingCoffee;
            } else if (input.equals("FILL")) {
                System.out.println("Write how many ml of water you want to add:");
                this.state = CoffeeMachineState.fillingWater;
            } else if (input.equals("TAKE")) {
                System.out.println("I gave you $" + this.money);
                this.money = 0;
            } else if (input.equals("EXIT")){
                exit(0);
            } else if (input.equals("REMAINING")){
                this.displayStock();
            }
        } else if (state == CoffeeMachineState.choosingCoffee) {
            if(input.equals("BACK")){
                this.state = CoffeeMachineState.choosingAction;
            } else {
                Coffee c = Coffee.getByNumber(Integer.parseInt(input));
                if (this.canMakeCoffee(c)) {
                    System.out.println("I have enough resources, making you a coffee!");
                    makeCoffee(c);
                }
                this.state = CoffeeMachineState.choosingAction;
            }
        } else if (state == CoffeeMachineState.fillingWater) {
            this.water += Integer.parseInt(input);
            System.out.println("Write how many ml of milk you want to add:");
            this.state = CoffeeMachineState.fillingMilk;
        } else if (state == CoffeeMachineState.fillingMilk) {
            this.milk += Integer.parseInt(input);
            System.out.println("Write how many grams of coffee beans you want to add:");
            this.state = CoffeeMachineState.fillingBeans;
        } else if (state == CoffeeMachineState.fillingBeans){
            this.coffeeBeans += Integer.parseInt(input);
            System.out.println("Write how many disposable cups you want to add:");
            this.state = CoffeeMachineState.fillingCups;
        } else if (state == CoffeeMachineState.fillingCups) {
            this.cups += Integer.parseInt(input);
            this.state = CoffeeMachineState.choosingAction;
        }
    }
}

public class main {
    public static void main(String[] args) {
        CoffeeMachine cm1 = new CoffeeMachine(400, 540, 120, 9, 550);
        while (true) {
            if(cm1.state == CoffeeMachineState.choosingAction) {
                System.out.println("Write action (buy, fill, take, remaining, exit):");
            }
            cm1.operate(new Scanner(System.in).next().toUpperCase());
        }
    }
}
