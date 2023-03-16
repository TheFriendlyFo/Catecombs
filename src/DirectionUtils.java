public class DirectionUtils {

    private DirectionUtils(){}
    public static int getX(int x, int a) {
        return x + switch (a) {
            case 1 -> 1;
            case 3 -> -1;
            default -> 0;
        };
    }

    public static int getY(int y, int a) {
        return y + switch (a) {
            case 0 -> 1;
            case 2 -> -1;
            default -> 0;
        };
    }

    public static char getIcon(int x, int y) {
        switch (y) {
            default -> {
                if (x == 1) {
                    return '>';
                } else {
                    return '<';
                }
            }
            case 1 -> {
                 return 'v';
            }
            case -1 -> {
                 return '^';
            }
        }
    }
}
