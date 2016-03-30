import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * This class is an implementation of manager
 * for simulated caching mechanism
 *
 * @author Lohit Velagapudi
 */
abstract class Manager{

     protected LRUCache<Integer, Client> cache;
     protected BloomFilter bf;
     private Server server;

     /**
      * The constructor
      *
      * @param int cacheSize size of cache
      * @param BloomFilter bf bloomfilter object
      * @param Server server server object
      */
     public Manager(int cacheSize, BloomFilter bf, Server server){
          cache=new LRUCache<Integer, Client>(cacheSize);
          this.bf=bf;
          this.server=server;
     }

     /**
      * This method adds block id and client object holding
      * the block to the cache
      *
      * @param int blockId block id 
      * @param Client client client object
      */
     public abstract void add(int newVal, Client client);
     

     /**
      * This method returns the Client object holding the block
      *
      * @param int blockId block id
      */
     public abstract Client getClient(int newVal);


     /**
      * this method returns if a particular block is singlet
      *
      * @param boolean true if singlet else false
      */
     public abstract boolean isSinglet(int blockId);
     

     /**
      * This methods requests data from the server
      *
      * @param int blockId block id being requested
      */
     public Block getFromServer(int blockId){
          return server.getBlock(blockId);
     }

     /**
      * This method deletes the block id from the cache
      *
      * @param int blockId block id
      */
     public abstract void delete(int val);
     
     
     /**public void display(FileWriter writer) throws IOException, FileNotFoundException{
          synchronized(cache){
               writer.write(cache.toString()+"\n");
          }     
     } */   
}
