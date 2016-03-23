class RHManager extends Manager{

     public RHManager(int cacheSize, BloomFilter bf, Server server){
          super(cacheSize, bf, server);
     }
     public void add(int newVal, Client client){}
     
     public Client getClient(int newVal){
          return null;
     }

     public boolean isSinglet(int blockId){
          return false;
     }

     public void delete(int val){}
     
}
