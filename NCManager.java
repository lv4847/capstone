import java.util.HashMap;

class NCManager extends Manager{
     private HashMap<Integer, Integer> blockCount;

     public NCManager(int cacheSize, BloomFilter bf, Server server){
          super(cacheSize, bf, server);
          blockCount=new HashMap<Integer, Integer>();
     }

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

     public Client getClient(int blockId){
          synchronized(cache){
               if(bf.isSeen(""+blockId)){
                    return (Client)cache.get(blockId);
               }
               return null;
          }
     }

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

     public boolean isSinglet(int blockId){
          synchronized(cache){
               Integer count=(Integer)blockCount.get(blockId);
               if(count!=null) return (count==1);
               return false;
          }     
     }     
}
