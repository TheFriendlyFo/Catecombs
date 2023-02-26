public class DirectionUtils {

    private DirectionUtils(){}
    public static int getX(int a) {
        return switch (a) {
            case 1 -> 1;
            case 3 -> -1;
            default -> 0;
        };
    }

    public static int getY(int a) {
        return switch (a) {
            case 0 -> 1;
            case 2 -> -1;
            default -> 0;
        };
    }

    public static int getX(String a) {
        return switch (a.toLowerCase()) {
            case "d" -> 1;
            case "a" -> -1;
            default -> 0;
        };
    }

    public static int getY(String a) {
        return switch (a.toLowerCase()) {
            case "s" -> 1;
            case "w" -> -1;
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
