package World.Towers;

/**
 * Created by volverine on 5/12/16.
 */
public class Bank {
    private int money;

    public Bank() {
        money = 0;
    }

    public Bank(int start_bank) {
        money = start_bank;
    }

    public boolean isEnough(int price) {
        return money >= price;
    }

    public void buy(int price) {
        money -= price;
    }

    public void invest(int money) {
        this.money += money;
    }
}
