public class Main {
    public static void main(String[] args) {
        Catacombs catacombs = new Catacombs(6, 10);
        while (true) {
            catacombs.update();
        }
    }
}