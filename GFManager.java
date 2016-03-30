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
     public GFManager(int cacheSize, BloomFilter bf, Server server){
          super(cacheSize, bf, server);
     }

     /**
      * This method adds block id and client object holding
      * the block to the cache
      *
      * @param int blockId block id 
      * @param Client client client object
      */
     public void add(int blockId, Client client){
          synchronized(cache){  
               cache.put(blockId, client);
               //System.out.println("Yes mgr");
               bf.addSeenMember(""+blockId);
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
      *
      * @param int blockId block id
      */
     public void delete(int blockId){
          synchronized(cache){
               cache.remove(blockId);
          }     
     }

     /**
      * This method is not used in Greedy Forwarding
      */
     public boolean isSinglet(int blockId){
          return false;
     }
}
