import java.util.Random;

class NCClient extends Client{

     Client[] client;
     
     public NCClient(int id, int cacheSize, BloomFilter bf, Manager mgr, Client[] client){
          super(id, cacheSize, bf, mgr);
          this.client=client;
     }
     
     public void addBlock(Block data){
          Block lru=null;
          int next=-1;
          synchronized(cache){
               //handleSinglet();
                lru=(Block)cache.getLRU();
                if(lru!=null && mgr.isSinglet(lru.getId())){
                    if(lru.getRecirculationCount()>0){
                      lru.decrementRecirculationCount();
                      Random randNum=new Random();
                      next=id;
                      while(next==id){
                         next=randNum.nextInt(client.length);
                      }    
                      //System.out.println("Client "+id+" -> Client "+next+" Sending "+lru.getId()+" Count "+lru.getRecirculationCount());
                      //client[next].addBlock(lru);
                    }     
                    Client c=mgr.getClient(lru.getId());
                    if(c!=null && c.id==this.id) mgr.delete(lru.getId()); 
                }               
               cache.put(data.getId(), data);
               bf.addSeenMember(""+data.getId());
               mgr.add(data.getId(), this);              
          }
          if(next!=-1){
               //System.out.println("Client "+id+" -> Client "+next+" Sending "+lru.getId()+" Count "+lru.getRecirculationCount());
               client[next].addBlock(lru);
          }     
     }

     public Block getBlock(int blockId){
          synchronized(cache){
               if(bf.isSeen(""+blockId)){
                    Block block=(Block)cache.get(blockId);
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
