import java.math.BigInteger;
/**
 * This class is an implementation of Bloom Filter
 *
 * @author Lohit Velagapudi
 *
 */
class BloomFilter{
     int[] filter;
     int filterSize;

     /**
      * The constructor sets the bloomfilter size and
      * initializes all the bits of the bloomfilter to 0
      *
      * @param filterSize int the size of the bloomfilter
      * 
      */
     public BloomFilter(int filterSize){
          this.filterSize=filterSize;
          filter=new int[filterSize];

          for(int i=0; i<filterSize; i++){
               filter[i]=0;
          }
     }

     /**
      * This method represents a new element in bloomfilter
      *
      * @param input String data to be represented
      */
     public void addSeenMember(String input){
          Hash forInput=new Hash(input);
          int[] bits=forInput.getBits(filterSize);
          
          for(int i=0; i<bits.length; i++){
               int index=bits[i];
               filter[index]=1;
          }
     }

     /**
      * This method returns false if a data has not been 
      * represented in the bloomfilter or else true
      * The data may have been represented if it returns true
      *
      * @param input String the data being looked up for
      * @return boolean false if data not present, true if data may be present
      */
     public boolean isSeen(String input){
          Hash forInput=new Hash(input);
          int[] bits=forInput.getBits(filterSize);
          
          for(int i=0; i<bits.length; i++){
               int index=bits[i];
               if(filter[index]==0) return false;
          }
          return true;
     }

     /*public static void main(String[] args){
          BloomFilter bf=new BloomFilter(10);
          bf.addSeenMember("Lohit");
          System.out.println(bf.isSeen("Lohit"));
          System.out.println(bf.isSeen("Ramu"));
     }*/
}
