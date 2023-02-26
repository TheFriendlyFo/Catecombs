public class Main {
    public static void main(String[] args) {
        Catacombs catacombs = new Catacombs(6, 10);
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            catacombs.update();
        }
    }
}