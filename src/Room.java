public class Room {
    private final int floor;
    private final int number;
    private final Enemy enemy;
    private boolean visited = false;

    public Room(int floor, int number, Enemy enemy) {
        this.floor = floor;
        this.number = number;
        this.enemy = enemy;
    }

    public int getNumber() { return number; }
    public Enemy getEnemy() { return enemy; }
    public boolean isVisited() { return visited; }
    public void setVisited(boolean v) { visited = v; }
}
