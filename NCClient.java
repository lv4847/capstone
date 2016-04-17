import java.util.Random;
/**
 * This client implementation specific to N-Chance
 *
 * @author Lohit Velagapudi
 */
class NCClient extends Client{

     private Client[] client;

     /**
      * The constructor
      *
      * @param int id client identifier
      * @param int cacheSize size of the client cache
      * @param BloomFilter bf the bloomfilter or IBF object
      * @param Manager mgr the manager object
      * @param Client[] client list of client objects
      */
     public NCClient(int id, int cacheSize, BloomFilter bf, Manager mgr, Client[] client, int localCacheTicks){
          super(id, cacheSize, bf, mgr, localCacheTicks);
          this.client=client;
     }

     /**
      * This method adds content to the cache, checks the least recently used
      * if it is a singlet and forwards it to a random client accordingly
      *
      * @param Block data data to be added to the cache
      */
     public void addBlock(Block data){
          Block lru=null;
          int next=-1;
          synchronized(mgr){
               synchronized(cache){
                     lru=(Block)cache.getLRU();
                     if(lru!=null && ((NCManager)mgr).isSinglet(lru.getId())){
                         if(lru.getRecirculationCount()>0){
                                lru.decrementRecirculationCount();
                                Random randNum=new Random();
                                next=id;
                                while(next==id){
                                   next=randNum.nextInt(client.length);
                                }
                         }      
                     }               

                    if(cache.size()==cacheSize) mgr.delete(lru.getId(), this); 
                    cache.put(data.getId(), data);
                    bf.addSeenMember(""+data.getId());
                    mgr.add(data.getId(), this);              
               }
          }
          if(next!=-1 && lru!=null){
               client[next].addBlock(lru);
          }     
     }


}
