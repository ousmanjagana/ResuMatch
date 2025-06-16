import java.util.*;
public class ResuMatch {

    // Function of this method: Splits a string into lowercase words and removes punctuation
    public static List<String> tokenize(String text) {
        // Replaces all punctuation with empty strings, convert to lowercase, and split by whitespace
        return Arrays.asList(text.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+")); //[^a-zA-Z0-9 ] looks for any symbol, punctuation, or special character that isn't a letter, number, or space
    }

    // Function of this method: Simple filter for common English stop words that add noise (like "the", "in", etc.)
    public static boolean isStopWord(String word) {
        String[] stopwords = {"the", "is", "at", "which", "on", "and", "a", "an", "with", "for", "in", "to", "of"};
        return Arrays.asList(stopwords).contains(word);
    }

    // Function of this method: Builds a frequency map of non-stop words
    public static Map<String, Integer> getTermFrequency(List<String> tokens) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : tokens) {
            // Skip stop words and very short words
            if (!isStopWord(word) && word.length() > 1) {
                freq.put(word, freq.getOrDefault(word, 0) + 1);
            }
        }
        return freq;
    }

    // Function of this method: Converts a frequency map to a numeric vector using a fixed vocabulary
    public static double[] toVector(Map<String, Integer> freq, Set<String> vocab) {
        double[] vector = new double[vocab.size()];
        int i = 0;
        for (String word : vocab) {
            // Assigns the word count to the vector slot based on vocab order
            vector[i++] = freq.getOrDefault(word, 0);
        }
        return vector;
    }

    // Function of this method: Measures similarity between two vectors using cosine similarity formula
    public static double cosineSimilarity(double[] v1, double[] v2) {
        double dot = 0 
        double normA = 0 
        double normB = 0;
        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];             // numerator: dot product
            normA += Math.pow(v1[i], 2);      // ||A||^2
            normB += Math.pow(v2[i], 2);      // ||B||^2
        }
        return (normA == 0 || normB == 0) ? 0 : dot / (Math.sqrt(normA) * Math.sqrt(normB));
        //this calculates the magnitude (or the length of the vector) to compare content instead of just compare the size 
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask user to paste their resume and job description
        System.out.println("Enter your resume text:");
        String resumeText = scanner.nextLine();

        System.out.println("Enter the job description:");
        String jobText = scanner.nextLine();

        // Break both texts into tokens (words) and filter/normalize them
        List<String> resumeTokens = tokenize(resumeText);
        List<String> jobTokens = tokenize(jobText);

        // Get word counts (frequency maps) excluding stop words
        Map<String, Integer> resumeFreq = getTermFrequency(resumeTokens);
        Map<String, Integer> jobFreq = getTermFrequency(jobTokens);

        // Combine unique words from both texts to form a common vocabulary
        Set<String> vocab = new HashSet<>();
        vocab.addAll(resumeFreq.keySet());
        vocab.addAll(jobFreq.keySet());

        // Turn each frequency map into a numeric vector (same word order)
        double[] resumeVec = toVector(resumeFreq, vocab);
        double[] jobVec = toVector(jobFreq, vocab);

        // Compare the two vectors using cosine similarity
        double similarity = cosineSimilarity(resumeVec, jobVec);

        // Output the similarity percentage (match score)
        System.out.printf("Match Score: %.2f%%\n", similarity * 100);

        // Print out words that were present in both texts
        System.out.println("\nTop Matching Keywords:");
        for (String word : vocab) {
            if (resumeFreq.containsKey(word) && jobFreq.containsKey(word)) {
                System.out.println(" - " + word);
            }
        }

        scanner.close();
    }
}
