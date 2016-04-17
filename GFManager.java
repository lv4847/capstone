import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
/**
 * This manager class is specific for Greedy Forwarding
 *
 * @author Lohit Velagapudi
 */
class GFManager extends Manager{

     /**
      * The constructor
      *
      * @param int cacheSize size of cache
      * @param BloomFilter bf bloomfilter object
      * @param Server server server object
      */
     public GFManager(int cacheSize, BloomFilter bf, Server server, int globalCacheTicks){
          super(cacheSize, bf, server, globalCacheTicks);
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
               }
          }
     }

}
