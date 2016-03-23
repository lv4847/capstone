import java.math.BigInteger;

class BloomFilter{
     int[] filter;
     int filterSize;

     public BloomFilter(int filterSize){
          this.filterSize=filterSize;
          filter=new int[filterSize];

          for(int i=0; i<filterSize; i++){
               filter[i]=0;
          }
     }

     public void addSeenMember(String input){
          Hash forInput=new Hash(input);
          int[] bits=forInput.getBits(filterSize);
          
          for(int i=0; i<bits.length; i++){
               int index=bits[i];
               filter[index]=1;
          }
     }

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
