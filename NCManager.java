import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
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
     public NCManager(int cacheSize, BloomFilter bf, Server server, int globalCacheTicks){
          super(cacheSize, bf, server, globalCacheTicks);
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
          super.add(blockId, client);

          if(blockCount.containsKey(blockId)) blockCount.put(blockId, blockCount.get(blockId)+1);
          else blockCount.put(blockId, 1);
     }

   
     /**
      * This method deletes the block id from the cache
      * and decrements the number of occurence in HashMap
      *
      * @param int blockId block id to be deleted
      * @param Client client client to be deleted
      */
     public void delete(int blockId, Client client){
          super.delete(blockId, client);
                         
          if(blockCount.containsKey(blockId)){
                if(blockCount.get(blockId)==1){
                       blockCount.remove(blockId);
                }
                else{
                     blockCount.put(blockId, blockCount.get(blockId)-1); 
                }
          }
     }

     /**
      * This method gets client cache data and stores in global cache
      */
     protected void updateCache(){
          for(int i=0; i<clients.length; i++){
               LRUCache clientCache=clients[i].getCache();
               Set set=clientCache.entrySet();
               Iterator itr=set.iterator();

               while(itr.hasNext()){
                   Map.Entry me=(Map.Entry) itr.next();
                   int blockId=(int)me.getKey();
                   HashSet<Client> clientSet=null;
                   if(cache.containsKey(blockId)) clientSet=(HashSet<Client>)cache.get(blockId);
                   else clientSet=new HashSet<Client>();
                   clientSet.add(clients[i]);
                   cache.put(blockId, clientSet);
                   
                   synchronized(blockCount){
                        if(i==0) blockCount.clear();
                        if(blockCount.containsKey(blockId)) blockCount.put(blockId, blockCount.get(blockId)+1);
                        else blockCount.put(blockId, 1);
                   }     
               }
          }
     }

     /**
      * this method returns if a particular block is singlet
      *
      * @param int blockId block being checked
      * @return boolean true if singlet else false
      */
     public boolean isSinglet(int blockId){
          Integer count=blockCount.get(blockId);
          if(count!=null) return (count==1);
          return false;
     }
    
}
