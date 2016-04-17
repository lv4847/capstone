import java.util.Random;

class RHClient extends Client{

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
     RHClient(int id, int cacheSize, BloomFilter bf, Manager mgr, Client[] client, int localCacheTicks){
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
          int victimClient=-1;
          int victimBlock=-1;
          int next=-1;
          
          synchronized(mgr){
               synchronized(cache){
                     lru=(Block)cache.getLRU();
                     if(lru!=null && ((RHManager)mgr).isSinglet(lru.getId())){
                         if(lru.getRecirculationCount()>0){
                                lru.decrementRecirculationCount();
                                Victim victim=((RHManager)mgr).getVictim(this);
                                if(victim!=null){
                                   victimClient=victim.getClient();
                                   victimBlock=victim.getBlock();
                                }
                                else{
                                   Random randNum=new Random();
                                   next=id;
                                   while(next==id){
                                        next=randNum.nextInt(client.length);
                                   }
                                }   
                         }      
                     }
                     
                    if(cache.size()==cacheSize) mgr.delete(lru.getId(), this);             
                    cache.put(data.getId(), data);
                    bf.addSeenMember(""+data.getId());
                    mgr.add(data.getId(), this);           
               }
               if(victimClient!=-1 && lru!=null){
                    ((RHClient)client[victimClient]).replace(victimBlock, lru);
               }
          }
          if(next!=-1 && lru!=null){
               client[next].addBlock(lru);
          }     
     }
     
     /**
      * This method replaces the the block with victimId
      * with a Singlet
      *
      * @param int vicitmId block to be replaced
      * @param Block singlet new block to be added 
      */
     public void replace(int victimId, Block singlet){
          synchronized(cache){
               int singletId=singlet.getId();
               cache.remove(victimId);
               
               mgr.delete(victimId, this);
               cache.put(singletId, singlet);
               
               mgr.add(singletId, this);         
          }
     }
}
