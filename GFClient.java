class GFClient extends Client{

     public GFClient(int id, int cacheSize, BloomFilter bf, Manager mgr){
          super(id, cacheSize, bf, mgr);
     }
     
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
