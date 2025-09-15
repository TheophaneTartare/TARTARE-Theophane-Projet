import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassesData {

    public static void main(String[] args) throws Exception {
        Directory index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);

        String projectPath = "C:\\Users\\chris\\Documents\\GL\\eclipse-collections"; // Remplace par le chemin de ton projet
        indexJavaFiles(w, projectPath);
        w.close();

        analyzePackages(index, analyzer);
    }

    private static void indexJavaFiles(IndexWriter w, String projectPath) throws IOException {
        Files.walk(Paths.get(projectPath))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String content = new String(Files.readAllBytes(path));
                        Document doc = new Document();
                        doc.add(new TextField("content", content, Field.Store.YES));
                        doc.add(new TextField("path", path.toString(), Field.Store.YES));
                        w.addDocument(doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void analyzePackages(Directory index, StandardAnalyzer analyzer) throws Exception {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        Map<String, Integer> packageClassCount = extractPackageClassCounts(searcher, analyzer);
        printStatistics(packageClassCount);
        analyzeInheritance(searcher, analyzer);

        reader.close();
    }

    private static Map<String, Integer> extractPackageClassCounts(IndexSearcher searcher, StandardAnalyzer analyzer) throws Exception {
        Map<String, Integer> packageClassCount = new HashMap<>();
        Query query = new QueryParser("content", analyzer).parse("class");
        TopDocs docs = searcher.search(query, Integer.MAX_VALUE);

        for (org.apache.lucene.search.ScoreDoc hit : docs.scoreDocs) {
            Document doc = searcher.doc(hit.doc);
            String content = doc.get("content");
            String packageName = extractPackageName(content);
            if (packageName != null) {
                packageClassCount.put(packageName, packageClassCount.getOrDefault(packageName, 0) + countClassOccurrences(content));
            }
        }
        return packageClassCount;
    }

    private static int countClassOccurrences(String content) {
        Pattern pattern = Pattern.compile("class\\s+[A-Za-z0-9_]+");
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static String extractPackageName(String content) {
        Pattern pattern = Pattern.compile("package\\s+([a-zA-Z0-9\\.]+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "(default package)";
    }

    private static void printStatistics(Map<String, Integer> packageClassCount) {
        if (packageClassCount.isEmpty()) {
            System.out.println("Aucun paquetage trouvé.");
            return;
        }

        int totalClasses = packageClassCount.values().stream().mapToInt(Integer::intValue).sum();
        int minClasses = packageClassCount.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxClasses = packageClassCount.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        double avgClasses = packageClassCount.values().stream().mapToInt(Integer::intValue).average().orElse(0);

        System.out.println("Nombre total de classes : " + totalClasses);
        System.out.println("Nombre minimum de classes par paquetage : " + minClasses);
        System.out.println("Nombre maximum de classes par paquetage : " + maxClasses);
        System.out.println("Nombre moyen de classes par paquetage : " + avgClasses);
    }

    private static void analyzeInheritance(IndexSearcher searcher, StandardAnalyzer analyzer) throws Exception {
        Query query = new QueryParser("content", analyzer).parse("class");
        TopDocs docs = searcher.search(query, Integer.MAX_VALUE);

        Map<String, Set<String>> inheritanceMap = new HashMap<>();

        for (org.apache.lucene.search.ScoreDoc hit : docs.scoreDocs) {
            Document doc = searcher.doc(hit.doc);
            String content = doc.get("content");
            String className = extractClassName(content);
            Set<String> parents = extractParents(content);
            inheritanceMap.put(className, parents);
        }

        calculateDIT(inheritanceMap);
        calculateNOC(inheritanceMap);
    }

    private static String extractClassName(String content) {
        Pattern pattern = Pattern.compile("class\\s+([A-Za-z0-9_]+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static Set<String> extractParents(String content) {
        Set<String> parents = new HashSet<>();
        Pattern extendsPattern = Pattern.compile("extends\\s+([A-Za-z0-9_]+)");
        Matcher extendsMatcher = extendsPattern.matcher(content);
        if (extendsMatcher.find()) {
            parents.add(extendsMatcher.group(1));
        }
        Pattern implementsPattern = Pattern.compile("implements\\s+([A-Za-z0-9_,\\s]+)");
        Matcher implementsMatcher = implementsPattern.matcher(content);
        if (implementsMatcher.find()) {
            String[] interfaces = implementsMatcher.group(1).split(",");
            for (String iface : interfaces) {
                parents.add(iface.trim());
            }
        }
        return parents;
    }

    private static void calculateDIT(Map<String, Set<String>> inheritanceMap) {
        Map<String, Integer> ditMap = new HashMap<>();
        for (String className : inheritanceMap.keySet()) {
            ditMap.put(className, calculateDITRecursive(className, inheritanceMap, new HashSet<>()));
        }

        int minDIT = ditMap.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxDIT = ditMap.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        double avgDIT = ditMap.values().stream().mapToInt(Integer::intValue).average().orElse(0);

        System.out.println("\nProfondeur de l'arbre d'héritage (DIT) :");
        System.out.println("Min : " + minDIT);
        System.out.println("Max : " + maxDIT);
        System.out.println("Moyenne : " + avgDIT);
    }

    private static int calculateDITRecursive(String className, Map<String, Set<String>> inheritanceMap, Set<String> visited) {
    if (visited.contains(className)) {
        return 0;
    }
    visited.add(className);

    Set<String> parents = inheritanceMap.get(className);
    if (parents == null || parents.isEmpty()) {
        return 0;
    }

    int maxParentDIT = 0;
    for (String parent : parents) {
        maxParentDIT = Math.max(maxParentDIT, calculateDITRecursive(parent, inheritanceMap, visited));
    }
    return maxParentDIT + 1;
}


    private static void calculateNOC(Map<String, Set<String>> inheritanceMap) {
    Map<String, Integer> nocMap = new HashMap<>();
    for (String className : inheritanceMap.keySet()) {
        nocMap.put(className, 0);
    }

    for (Set<String> parents : inheritanceMap.values()) {
        for (String parent : parents) {
            nocMap.put(parent, nocMap.getOrDefault(parent, 0) + 1);
        }
    }

    int minNOC = nocMap.values().stream().mapToInt(Integer::intValue).min().orElse(0);
    int maxNOC = nocMap.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    double avgNOC = nocMap.values().stream().mapToInt(Integer::intValue).average().orElse(0);

    System.out.println("\nNombre d'enfants (NOC) :");
    System.out.println("Min : " + minNOC);
    System.out.println("Max : " + maxNOC);
    System.out.println("Moyenne : " + avgNOC);
}

}