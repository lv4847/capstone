import java.util.Random;
import java.math.BigInteger;
import java.util.HashSet;
/**
 * This class is an implementation of Importance-Aware Bloom Filter
 *
 * @author Lohit Velagapudi
 */
class ImpBloomFilter extends BloomFilter{

     private int p;

     /**
      * The constructor
      *
      * @param int filterSize size of IBF
      */
     public ImpBloomFilter(int filterSize){
          super(filterSize);
          p=4;
     }

     /**
      * This method checks if a particular data is represented
      * in the IBF. The bits representing the data are also incremented
      * everytime the data is seen.
      *
      * @param String input data to be checked
      */
     public boolean isSeen(String input){
          Hash forInput=new Hash(input);
          int[] bits=forInput.getBits(filterSize);
          
          for(int i=0; i<bits.length; i++){
               int index=bits[i];
               if(filter[index]==0) return false;
          }
          
          for(int i=0; i<bits.length; i++){
               int index=bits[i];
               filter[index]++;
          }
          
          decrementCells();
          return true;
     }

     /**
      * This method randomly decrements p bits
      */
     private void decrementCells(){
          Random randNum=new Random();
          int i=0;
          while(i<p){
               int cell=randNum.nextInt(filterSize);
               filter[cell]--;
               i++;              
          }
     }
         
}
