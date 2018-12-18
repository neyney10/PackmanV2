package Utli;

import java.util.ArrayList;
import java.util.Random;

/**
 * Color generator class is a Singleton Anti-pattern design pattern for randomly generate colors.
 */
public class ColorGenerator {
    private static ColorGenerator ourInstance = new ColorGenerator();
    private ArrayList<String> blacklist = new ArrayList<>();
    public static ColorGenerator getInstance() {
        return ourInstance;
    }

    private ColorGenerator() {
    }

    private boolean DuplicateColor(String color){
        if (blacklist.contains(color))
            return true;
        blacklist.add(color);
        return false;
    }

    /**
     * get a new Color in HEXA
     * @return new string of HEXA color ARGB 
     */
    public String colorGenerate(){
        Random r = new Random();
        final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char [] s = new char[7];
        int n;
        String colorHex;
        do {
            n = r.nextInt(0x1000000);

            s[0] = '#';
            for (int i = 1; i < 7; i++) {
                s[i] = hex[n & 0xf];
                n >>= 4;
            }
            colorHex = String.valueOf(s);
        }while (DuplicateColor(colorHex));
        return colorHex;
    }
}
