/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloomfilter;

/**
 *
 * @author Akshay
 */

public class Conversion {
   private Conversion(){}
   
   public static byte[] toBytes(Long val){
       int bytesInALong = Long.BYTES;
       byte[] result = new byte[bytesInALong];
       
       int mask = (1<<(Byte.SIZE+1))-1;
       
       for(int i=bytesInALong-1;i>=0;i--){
           byte end8bits = (byte)(mask & val);
           result[i] = end8bits;
           val = val >> Byte.SIZE;
       }
       return result;
   }
   
   
   public static Long toLong(byte[] arr){
       int bytesInALong = Byte.SIZE;
       
       if(bytesInALong < arr.length){
           throw new ArithmeticException("Cannot convert bytes to long as byte array has more elements than 1 long can handle");
       }
       
       Long result = 0L;
       
       for(int i=0;i<bytesInALong;i++){
           result = result << Byte.SIZE;
           int b = arr[i]& 0xFF;
           result = result | b;
       }
       return result;
   }
   
   public static byte[] toBytes(Integer val){
       int bytesInAInt = Integer.BYTES;
       
       byte []result = new byte[bytesInAInt];
       
       int mask = (1<<(Byte.SIZE+1))-1;
       
       for(int i=bytesInAInt-1;i>=0;i--){
           byte end8bits = (byte)(mask & val);
           result[i] = end8bits;
           val = val >> Byte.SIZE;
       }
       return result;
   }
   
   public static Integer toInteger(byte arr[]){
       int bytesInAInt = Integer.BYTES;
       
       if(bytesInAInt < arr.length){
           throw new ArithmeticException("Cannot convert bytes to int as byte array has more elements than 1 int can handle");
       }
       
       int result = 0;
       for(int i=0;i<bytesInAInt;i++){
           result = result << Byte.SIZE;
           
           int b = arr[i] & 0xFF;
           
           result = result | b;
       }
       
       return result;
   }
}
