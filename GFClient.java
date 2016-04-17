/**
 * This Client class is specific for Greedy Forwarding
 *
 * @author Lohit Velagapudi
 */
class GFClient extends Client{

     /**
      * The constructor
      *
      * @param int id client identifier
      * @param int cacheSize size of the client cache
      * @param BloomFilter bf the bloomfilter or IBF object
      * @param Manager mgr the manager object
      */
     public GFClient(int id, int cacheSize, BloomFilter bf, Manager mgr, int localCacheTicks){
          super(id, cacheSize, bf, mgr, localCacheTicks);
     }

     /**
      * This method adds data to the cache and bloomfilter
      * Every data added to the client cache is also added to
      * the global cache
      *
      * @param Block data the data to be added to client cache
      */
     public void addBlock(Block data){
          synchronized(mgr){
               synchronized(cache){
                    Block lru=(Block)cache.getLRU();
                    cache.put(data.getId(), data);

                    if(lru!=null && cache.get(lru.getId())==null && cache.size()==cacheSize){
                         int lruId=lru.getId();
                         mgr.delete(lruId, this);
                    }
                    bf.addSeenMember(""+data.getId());
                    mgr.add(data.getId(), this);
               }
          }      
     }
}
