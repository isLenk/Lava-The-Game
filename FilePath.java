package Talban.Final;

import processing.core.PApplet;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class FilePath {
    // Fetch the path to said target string
    public static String get (String target) {
        try {
            URL font = PApplet.class.getResource(target);
            return Paths.get(font.toURI()).toFile().getAbsolutePath();
        }
        //Throw err if something fails
        catch (URISyntaxException e) {
            e.printStackTrace();;
            return "";
        }
    }
}
