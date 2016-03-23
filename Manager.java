import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

abstract class Manager{

     protected LRUCache cache;
     protected BloomFilter bf;
     private Server server;

     public Manager(int cacheSize, BloomFilter bf, Server server){
          cache=new LRUCache(cacheSize);
          this.bf=bf;
          this.server=server;
     }

     public abstract void add(int newVal, Client client);
     

     public abstract Client getClient(int newVal);

     public abstract boolean isSinglet(int blockId);
     

     public Block getFromServer(int newVal){
          return server.getBlock(newVal);
     }

     public abstract void delete(int val);
     
     
     public void display(FileWriter writer) throws IOException, FileNotFoundException{
          synchronized(cache){
               writer.write(cache.toString()+"\n");
          }     
     }    
}
