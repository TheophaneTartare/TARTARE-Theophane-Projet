import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CommentaryAnalyse {

    public static void main(String[] args) throws Exception {
        String projectPath = "C:\\Users\\chris\\Documents\\GL\\eclipse-collections"; // Remplace par le chemin de ton projet
        countAndIdentifyComments(projectPath);
    }

    private static void countAndIdentifyComments(String projectPath) throws IOException {
        final int[] totalLines = {0};
        final int[] javadocCount = {0};
        final int[] singleLineCount = {0};
        final int[] multiLineCount = {0};

        try (Stream<java.nio.file.Path> paths = Files.walk(Paths.get(projectPath))) {
            paths.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = new String(Files.readAllBytes(path));
                            totalLines[0] += countLines(content);
                            javadocCount[0] += countOccurrences(content, "/\\*\\*.*?\\*/");
                            singleLineCount[0] += countOccurrences(content, "//.*");
                            multiLineCount[0] += countOccurrences(content, "/\\*.*?\\*/");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        System.out.println("Nombre total de lignes de commentaire : " + totalLines[0]);
        System.out.println("\nTypes de commentaires :");
        System.out.println("Javadoc : " + javadocCount[0]);
        System.out.println("Ligne simple : " + singleLineCount[0]);
        System.out.println("Multi-lignes : " + multiLineCount[0]);
    }

    private static int countLines(String content) {
        Pattern pattern = Pattern.compile("//.*|/\\*.*?\\*/", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            String comment = matcher.group();
            count += comment.split("\n").length;
        }
        return count;
    }

    private static int countOccurrences(String content, String patternString) {
        int count = 0;
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}