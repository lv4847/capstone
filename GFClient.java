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
     public GFClient(int id, int cacheSize, BloomFilter bf, Manager mgr){
          super(id, cacheSize, bf, mgr);
     }

     /**
      * This method adds data to the cache and bloomfilter
      * Every data added to the client cache is also added to
      * the global cache
      *
      * @param Block data the data to be added to client cache
      */
     public void addBlock(Block data){
          synchronized(cache){
               Block lru=(Block)cache.getLRU();
               cache.put(data.getId(), data);
               if(lru!=null && cache.get(lru.getId())==null){
                    int lruId=lru.getId();
                    Client c=mgr.getClient(lruId);
                    if(c!=null && c.id==this.id)
                         mgr.delete(lruId);
               }
               bf.addSeenMember(""+data.getId());
               mgr.add(data.getId(), this);
         }      
     }

     /**
      * This method gets the data from the cache and adds
      * hit count and false positive count
      *
      * @param int blockId the data with the block id or null if not present
      */
     public Block getBlock(int blockId){
          synchronized(cache){
               if(bf.isSeen(""+blockId)){
                    Block block=cache.get(blockId);
                    if(block!=null){
                         hitCount++;
                         return block;
                    }
                    else falsePositive++;
               }
               return null;
          }      
     }
}
