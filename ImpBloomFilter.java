import java.util.Random;
import java.math.BigInteger;
import java.util.HashSet;

class ImpBloomFilter extends BloomFilter{

     private int p;

     public ImpBloomFilter(int filterSize){
          super(filterSize);
          p=4;
     }
     
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

     public void decrementCells(){
          Random randNum=new Random();
          int i=0;
          while(i<p){
               int cell=randNum.nextInt(filterSize);
               filter[cell]--;
               i++;              
          }
     }
         
}
