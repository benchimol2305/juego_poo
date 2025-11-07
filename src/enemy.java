import java.util.Random;

public class Enemy {
    private final String name;
    private int hp;
    private final int attack;
    private final Random rand = new Random();

    public Enemy(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    public int attack() {
        return Math.max(1, attack - 2 + rand.nextInt(5));
    }

    public void takeDamage(int d) {
        hp -= d;
        if (hp < 0) hp = 0;
    }

    public boolean isAlive() { return hp > 0; }
    public int getHp() { return hp; }
    public String getName() { return name; }
}