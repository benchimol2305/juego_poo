
import java.util.*;

public class Game {
    private Player player;
    private Map<Integer, List<Room>> floors;
    private int currentFloor = 1;
    private final int FLOORS = 3;
    private final int ROOMS = 5;
    private final Scanner scanner = new Scanner(System.in);
    private final Random rand = new Random();

    public Game() {
        player = new Player("Protagonista");
        floors = new HashMap<>();
        buildFloors();
    }

    private void buildFloors() {
        for (int f = 1; f <= FLOORS; f++) {
            List<Room> list = new ArrayList<>();
            for (int r = 1; r <= ROOMS; r++) {
                boolean isBoss = (f == FLOORS && r == ROOMS);
                Room room = new Room(f, r, isBoss ? bossForFloor(f) : randomEnemyForFloor(f));
                list.add(room);
            }
            floors.put(f, list);
        }
    }

    private Enemy randomEnemyForFloor(int floor) {
        int baseHp = 10 + floor * 5;
        int baseAtk = 3 + floor;
        String[] names = {"Sombra", "Susurro", "Figura", "Grito"};
        String name = names[rand.nextInt(names.length)] + " (Piso " + floor + ")";
        return new Enemy(name, baseHp + rand.nextInt(6), baseAtk + rand.nextInt(3));
    }

    private Enemy bossForFloor(int floor) {
        // Jefe final
        return new Enemy("El Payaso", 60, 10);
    }

    public void start() {
        println("Juego: Dungeon RPG, inspirado en It");
        println("Objetivo: Llegar al piso 3 sala 5 y derrotar a Pennuwhyse.");
        mainLoop();
    }

    private void mainLoop() {
        boolean running = true;
        while (running) {
            println("\n--- Piso " + currentFloor + " ---");
            displayRooms();
            println("Comandos: entrar [1-5], subir, bajar, estado, salir");
            System.out.print("> ");
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.startsWith("entrar")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    try {
                        int r = Integer.parseInt(parts[1]);
                        if (r >= 1 && r <= ROOMS) {
                            enterRoom(r);
                            if (!player.isAlive()) {
                                println("Has muerto. Fin del juego.");
                                running = false;
                            }
                            if (bossDefeated()) {
                                println("\nHas derrotado a Pennuwhyse. ¡Victoria!");
                                running = false;
                            }
                        } else println("Sala invslida.");
                    } catch (NumberFormatException e) {
                        println("Comando invalido.");
                    }
                } else println("Usa: entrar 1");
            } else if (line.equals("subir")) {
                if (currentFloor > 1) { currentFloor--; println("Subes a piso " + currentFloor); }
                else println("Ya estás en el piso superior.");
            } else if (line.equals("bajar")) {
                if (currentFloor < FLOORS) { currentFloor++; println("Bajas a piso " + currentFloor); }
                else println("Ya andas en el piso mas bajo.");
            } else if (line.equals("estado")) {
                println(player.status());
            } else if (line.equals("salir")) {
                println("Saliendo...");
                running = false;
            } else {
                println("Comando no reconocido.");
            }
        }
    }

    private void displayRooms() {
        List<Room> rooms = floors.get(currentFloor);
        for (Room room : rooms) {
            String mark = room.isVisited() ? "(v)" : "";
            println("Sala " + room.getNumber() + " " + mark);
        }
    }

    private void enterRoom(int number) {
        Room room = floors.get(currentFloor).get(number - 1);
        println("\nEntras en la sala " + number + " del piso " + currentFloor + ".");
        if (room.isVisited()) {
            println("La sala esta vacia.");
            return;
        }
        if (room.getEnemy() == null) {
            println("Nada en esta sala.");
            room.setVisited(true);
            return;
        }
        battle(room.getEnemy(), room);
    }

    private void battle(Enemy enemy, Room room) {
        println("Encuentras a: " + enemy.getName() + " (HP " + enemy.getHp() + ")");
        boolean running = true;
        while (running && player.isAlive() && enemy.isAlive()) {
            println("\nTu HP: " + player.getHp() + " | Enemigo HP: " + enemy.getHp());
            println("Opciones: atacar, curar, huir");
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();
            if (cmd.equals("atacar")) {
                int dmg = player.attack();
                enemy.takeDamage(dmg);
                println("Le haces " + dmg + " de dano.");
            } else if (cmd.equals("curar")) {
                if (player.usePotion()) {
                    println("Usaste una pocion. HP: " + player.getHp());
                } else {
                    println("No tienes pociones.");
                    continue;
                }
            } else if (cmd.equals("huir")) {
                if (rand.nextBoolean()) {
                    println("Logras huir.");
                    return;
                } else {
                    println("No pudiste huir.");
                }
            } else {
                println("Comando invalido.");
                continue;
            }

            if (enemy.isAlive()) {
                int ed = enemy.attack();
                player.takeDamage(ed);
                println(enemy.getName() + " te hace " + ed + " de dano.");
            }
        }

        if (!player.isAlive()) return;

        if (!enemy.isAlive()) {
            println("Has vencido a " + enemy.getName() + "!");
            room.setVisited(true);
            // drop chance for potion
            if (rand.nextInt(100) < 40) {
                player.gainPotion();
                println("Encontraste una pocion.");
            }
        }
    }

    private boolean bossDefeated() {
        Room bossRoom = floors.get(FLOORS).get(ROOMS - 1);
        return bossRoom.isVisited();
    }

    private void println(String s) { System.out.println(s); }
}
