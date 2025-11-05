/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloomfilter;

import java.io.*;
import java.util.*;
import java.math.*;
import java.security.*;


/**
 *
 * @author Akshay
 */
public class BloomFilterImpl<T> {
    private final BitArray bitArray;
    private final int size;
    private final int hashAlgorithmsNumber;
    private List<MessageDigest> messageDigests;
    
    private Map<T, Integer> seenCount = new HashMap<>();
    
    public static <E> BloomFilterImpl<E> create(int size,int hashAlgorithmsNumber){
        return new BloomFilterImpl<>(size,hashAlgorithmsNumber,new BitArray(size));
    }
    
    public static <E> BloomFilterImpl<E> create(int size, int hashAlgorithmsNumber, BitArray bitArray){
        return new BloomFilterImpl<>(size,hashAlgorithmsNumber,bitArray);
    }
    
    public static <E> BloomFilterImpl<E> create(int numberOfInsertions, double errorThreshold){
        int b = getOptimalBitArraySize(numberOfInsertions, errorThreshold);
        int k = getOptimalNumberOfHashFunctions(numberOfInsertions, b);
        return new BloomFilterImpl<>(b,k,new BitArray(b));
    }
    
    private BloomFilterImpl(int size,int hashAlgorithmsNumber,BitArray bitArray){
        this.size = size;
        this.hashAlgorithmsNumber = hashAlgorithmsNumber;
        this.bitArray = bitArray;
        this.messageDigests = new ArrayList<>();
        
        int added = 0;
        for(HashAlgorithm hashAlgorithm : HashAlgorithm.values()){
            try{
                messageDigests.add(MessageDigest.getInstance(hashAlgorithm.toString()));
            }catch(NoSuchAlgorithmException e){
                System.out.println("Hash function " + hashAlgorithm + " appears to be missing from jdk. Trying out the next available algorithm");
                continue;
            }
            added++;
            
            if(added == hashAlgorithmsNumber){
                break;
            }
        }
    }
    
    public void add(T element){
        for(MessageDigest md : messageDigests){
            int bitToSet = getBitPositionAfterHash(element, md);
            bitArray.setBit(bitToSet);
        }
    }
    
    private int getBitPositionAfterHash(T element,MessageDigest messageDigest){
        byte[] hashValue = messageDigest.digest(String.valueOf(element).getBytes());
        int bitToSet = getIntegerValueFromHash(hashValue);
        messageDigest.reset();
        seenCount.put(element, seenCount.getOrDefault(bitToSet, 0)+1);
        return bitToSet;
        
    }
    
    private int getIntegerValueFromHash(byte[] hashValue){
        int bitToSet = 0;
        bitToSet = Math.abs(new BigInteger(1,hashValue).intValue())%size;
        return bitToSet;
    }
    
    public boolean contains(T element){
        boolean result = true;
        for(MessageDigest md : messageDigests){
            int bitPosition = getBitPositionAfterHash(element,md);
            if(!bitArray.isBitSet(bitPosition)){
                result = false;
                break;
            }
        }
        
        return result;
    }
    
    public Map<T, Integer> getSeen() {
        return seenCount;
    }
    
    public void serialise(String filepath) throws IOException{
        String bitArrayPath = filepath + "_bit_array";
        bitArray.serialise(bitArrayPath);
        
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filepath))){
            bos.write(Conversion.toBytes(size));
            bos.write(Conversion.toBytes(hashAlgorithmsNumber));
        }
    }
    
    public static <E> BloomFilterImpl<E> load(String filepath) throws IOException {
        String bitArrayPath = filepath + "_bit_array";
        
        BitArray loadedBitArray = BitArray.load(bitArrayPath);
        
        int size;
        int numberOfHashes;
        
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filepath))){
            byte[] bytes = bis.readNBytes(Integer.BYTES);
            size = Conversion.toInteger(bytes);
            
            bytes = bis.readNBytes(Integer.BYTES);
            numberOfHashes = Conversion.toInteger(bytes);
        }
        
        return create(size,numberOfHashes,loadedBitArray);
    } 
    
    public BitArray getBitArray(){
        return bitArray.copy();
    }
    
    private static int getOptimalBitArraySize(long n, double E){
        return (int) (-n * Math.log(E) / (Math.log(2)*Math.log(2)));
    }
    
    static int getOptimalNumberOfHashFunctions(long n, long b){
        return Math.max(1, (int)Math.round((double) b/ n * Math.log(2)));
    }
    
    public int getK(){
        return hashAlgorithmsNumber;
    }
    
    public int getB(){
        return size;
    }
}
