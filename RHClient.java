class RHClient extends Client{

     RHClient(int id, int cacheSize, BloomFilter bf, Manager mgr){
          super(id, cacheSize, bf, mgr);
     }

     public void addBlock(Block data){}
     
     public Block getBlock(int blockId){
          return null;
     }
}
