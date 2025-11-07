import java.util.Random;

public class Player {
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackMin = 5;
    private final int attackMax = 10;
    private int potions = 2;
    private final Random rand = new Random();

    public Player(String name) {
        this.name = name;
        this.maxHp = 50;
        this.hp = maxHp;
    }

    public int attack() {
        return attackMin + rand.nextInt(attackMax - attackMin + 1);
    }

    public void takeDamage(int d) {
        hp -= d;
        if (hp < 0) hp = 0;
    }

    public boolean usePotion() {
        if (potions <= 0) return false;
        potions--;
        hp += 20;
        if (hp > maxHp) hp = maxHp;
        return true;
    }

    public void gainPotion() {
        potions++;
    }

    public boolean isAlive() { return hp > 0; }
    public int getHp() { return hp; }

    public String status() {
        return "HP: " + hp + "/" + maxHp + " | Pociones: " + potions;
    }
}
