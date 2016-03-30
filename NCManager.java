import java.util.HashMap;
/**
 * This manager class is specific for N-Chance
 *
 * @author Lohit Velagapudi
 */
class NCManager extends Manager{
     private HashMap<Integer, Integer> blockCount;

     /**
      * The constructor
      *
      * @param int cacheSize size of cache
      * @param BloomFilter bf bloomfilter object
      * @param Server server server object
      */
     public NCManager(int cacheSize, BloomFilter bf, Server server){
          super(cacheSize, bf, server);
          blockCount=new HashMap<Integer, Integer>();
     }

     /**
      * This method adds block id and client object holding
      * the block to the cache and adds the occurence of 
      * block to HashMap
      *
      * @param int blockId block id 
      * @param Client client client object
      */
     public void add(int blockId, Client client){
          synchronized(cache){
               cache.put(blockId, client);
               bf.addSeenMember(""+blockId);
               if(blockCount.containsKey(blockId)){
                    blockCount.put(blockId, blockCount.get(blockId)+1);
               }
               else{
                    blockCount.put(blockId, 1);
               }
          }
     }

     /**
      * This method returns the Client object holding the block
      *
      * @param int blockId block id
      */
     public Client getClient(int blockId){
          synchronized(cache){
               if(bf.isSeen(""+blockId)){
                    return cache.get(blockId);
               }
               return null;
          }
     }

     /**
      * This method deletes the block id from the cache
      * and decrements the number of occurence in HashMap
      *
      * @param int blockId block id
      */
     public void delete(int blockId){
          synchronized(cache){
               cache.remove(blockId);
               if(blockCount.containsKey(blockId)){
                    if(blockCount.get(blockId)==1){
                         blockCount.remove(blockId);
                    }
                    else{
                        blockCount.put(blockId, blockCount.get(blockId)-1); 
                    }
               }
          }
     }

     /**
      * this method returns if a particular block is singlet
      *
      * @param boolean true if singlet else false
      */
     public boolean isSinglet(int blockId){
          synchronized(cache){
               Integer count=blockCount.get(blockId);
               if(count!=null) return (count==1);
               return false;
          }     
     }     
}
