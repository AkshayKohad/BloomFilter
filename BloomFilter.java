/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bloomfilter;
import java.io.IOException;
/**
 *
 * @author Akshay
 */
public class BloomFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
//        HashAlgorithm h = HashAlgorithm.SHA_256;
//        System.out.println(h);
//        
//        BitArray b = new BitArray(50);


        System.out.println("=== Bloom Filter Demonstration ===\n");

       
        int expectedInsertions = 15;
        double falsePositiveRate = 0.02; // 2% expected false positives

        
        BloomFilterImpl<String> bloomFilter = BloomFilterImpl.create(expectedInsertions, falsePositiveRate);
        System.out.println("Created Bloom Filter with parameters:");
        System.out.println(" - Expected Insertions: " + expectedInsertions);
        System.out.println(" - False Positive Rate: " + falsePositiveRate);
        System.out.println(" - Bit Array Size (B): " + bloomFilter.getB());
        System.out.println(" - Number of Hash Functions (K): " + bloomFilter.getK());
        System.out.println();

        // Step 3: Add elements
        String[] itemsToAdd = {"linkedin", "github", "stackoverflow", "google", "amazon", "microsoft"};
        System.out.println("Adding elements to Bloom Filter:");
        for (String item : itemsToAdd) {
            bloomFilter.add(item);
            System.out.println(" + Added: " + item);
        }

        System.out.println("\nStep 4: Checking element membership\n");

        String[] testItems = {"linkedin", "github", "netflix", "amazon", "chatgpt", "openai"};
        for (String test : testItems) {
            boolean result = bloomFilter.contains(test);
            System.out.printf(" - %-12s : %s%n", test, result ? "Possibly Present" : "Definitely Not Present");
        }

        String filePath = "bloom_data";
        try {
            bloomFilter.serialise(filePath);
            System.out.println("\nBloom Filter serialized successfully!");
            System.out.println(" - Saved to: '" + filePath + "' and '" + filePath + "_bit_array'");
        } catch (IOException e) {
            System.err.println("Error during serialization: " + e.getMessage());
        }

       
        try {
            BloomFilterImpl<String> loadedFilter = BloomFilterImpl.load(filePath);
            System.out.println("\nBloom Filter successfully loaded from disk.");

            System.out.println("Rechecking membership using loaded Bloom Filter:");
            for (String test : testItems) {
                boolean result = loadedFilter.contains(test);
                System.out.printf(" [Loaded] %-12s : %s%n", test, result ? "Possibly Present" : "Definitely Not Present");
            }

        } catch (IOException e) {
            System.err.println("Error during loading: " + e.getMessage());
        }

        System.out.println("\n=== End of Bloom Filter Demonstration ===");
    }
    
}
