package com.jazzyjohn.tram.petersburgtram;

/**
 * Created by vania on 28-Oct-17.
 */

public enum TramColor
{
    Invalid,
    White,
    Blue,
    Yellow,
    Green,
    Red;

    public static int toInt(TramColor color) {
        switch(color)
        {
            case Invalid:
                return 0;
            case White:
                return 1;
            case Blue:
                return 2;
            case Yellow:
                return 3;
            case Green:
                return 4;
            case Red:
                return 5;
        }
        return 0;
    }

    public static int toColor(TramColor color) {
        switch(color)
        {
            case Invalid:
                return R.color.black;
            case White:
                return R.color.white;
            case Blue:
                return R.color.blue;
            case Yellow:
                return R.color.yellow;
            case Green:
                return R.color.green;
            case Red:
                return R.color.red;
        }
        return 0;
    }
}
