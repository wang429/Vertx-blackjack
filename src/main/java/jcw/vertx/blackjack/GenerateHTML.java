package jcw.vertx.blackjack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generates a new HTML file to pass to clients
 * 
 * TODO Instead of using this to generate the HTML file, get a good grasp of how
 * to use Javascript
 * 
 * @author James Wang
 */
public class GenerateHTML {
    public static String generateGameHTMLFile(String filename, String dealerHand, String playerHand, String playerId)
            throws IOException {
        String str1 = new String(Files.readAllBytes(Paths.get("str1.txt")));

        StringBuilder str2SB = new StringBuilder();
        str2SB.append("<p>");
        str2SB.append(dealerHand);
        str2SB.append("</p>\n");

        String str3 = new String(Files.readAllBytes(Paths.get("str3.txt")));

        StringBuilder str4SB = new StringBuilder();
        str4SB.append("<p>");
        str4SB.append(playerHand);
        str4SB.append("</p>\n");

        String str5 = new String(Files.readAllBytes(Paths.get("str5.txt")));

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filename));
            out.print(str1);
            out.print(str2SB.toString());
            out.print(str3);
            out.print(str4SB.toString());
            out.print(str5);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return filename;
    }
}
