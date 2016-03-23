class GFManager extends Manager{

     public GFManager(int cacheSize, BloomFilter bf, Server server){
          super(cacheSize, bf, server);
     }
     
     public void add(int newVal, Client client){
          synchronized(cache){  
               cache.put(newVal, client);
               //System.out.println("Yes mgr");
               bf.addSeenMember(""+newVal);
          }     
     }

     public Client getClient(int newVal){
          synchronized(cache){
               if(bf.isSeen(""+newVal)){
                    return (Client)cache.get(newVal);
               }
               return null;
          }     
     }

     public void delete(int val){
          synchronized(cache){
               cache.remove(val);
          }     
     }

     public boolean isSinglet(int blockId){
          return false;
     }
}
