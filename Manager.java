import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
//import java.util.Scanner;
/**
 * This class is an implementation of manager
 * for simulated caching mechanism
 *
 * @author Lohit Velagapudi
 */
abstract class Manager{

     protected LRUCache<Integer, HashSet<Client>> cache;
     protected BloomFilter bf;
     private Server server;
     protected Client[] clients;
     protected int globalCacheTicks;
     protected int ticks;

     /**
      * The constructor
      *
      * @param int cacheSize size of cache
      * @param BloomFilter bf bloomfilter object
      * @param Server server server object
      */
     public Manager(int cacheSize, BloomFilter bf, Server server, int globalCacheTicks){
          cache=new LRUCache<Integer, HashSet<Client>>(cacheSize);
          this.bf=bf;
          this.server=server;
          this.globalCacheTicks=globalCacheTicks;
          this.ticks=0;
     }


     /**
      * This method returns Tick count
      *
      * @return long tick count
      */
     public long getTotalTicks(){
          return ticks;
     }
     
     /**
      * This methods gets the list of clients to manager
      *
      * @param Client[] clients list of clients
      */
     public void setClients(Client[] clients){
          this.clients=clients;
     }

     /**
      * This method returns the size of the cache
      *
      * @return int size of cache
      */
     public int getCacheSize(){
          return cache.size();
     }
     
     /**
      * This method adds block id and client object holding
      * the block to the cache
      *
      * @param int blockId block id 
      * @param Client client client object
      */
     public void add(int blockId, Client client){
          bf.addSeenMember(""+blockId);
          HashSet<Client> clientSet=null;
          if(cache.containsKey(blockId)) clientSet=(HashSet<Client>)cache.get(blockId); 
          else clientSet=new HashSet<Client>();
          clientSet.add(client);
          cache.put(blockId, clientSet);
     }
     

     /**
      * This method returns the Client object holding the block
      *
      * @param int blockId block id
      */
     public Client getClient(int blockId){
          Client clientWithBlock=null;
          HashSet<Client> clientList=(HashSet<Client>)cache.get(blockId);
          if(clientList!=null) clientWithBlock=(Client)clientList.iterator().next();
          return clientWithBlock;
     }

     /**
      * This method returns the block requested by a Client
      *
      * @param int blockId block id
      */
     public Block getBlock(int blockId){
          Client clientWithBlock=null;
          Block data=null;

          if(bf.isSeen(""+blockId)){
               HashSet<Client> clientList=(HashSet<Client>)cache.get(blockId);
               if(clientList!=null) {
                    Iterator itr=clientList.iterator();
                    while(itr.hasNext()){
                          Client client=(Client) itr.next();
                          LRUCache clientCache=client.getCache();
                          if(clientCache.containsKey(blockId)) {
                               clientWithBlock=client;
                               break;
                          }     
                    }
               }     
          }

          if(clientWithBlock!=null) data=clientWithBlock.getBlock(blockId, true);
          ticks+=globalCacheTicks;
          if(data==null){

               if(clientWithBlock!=null){
                    updateCache();
               }     
                    
               //fetches data from the server since Client does not have it
               return getFromServer(blockId);
          }
          return data;
     }

     /**
      * This method gets client cache data and stores in global cache
      *
      * @param Client client client whose cache data needs to be fetched
      */
     protected abstract void updateCache();


     /**
      * This methods requests data from the server
      *
      * @param int blockId block id being requested
      */
     protected Block getFromServer(int blockId){
          return server.getBlock(blockId);
     }

     /**
      * This method deletes the block id from the cache
      *
      * @param int blockId block id to be deleted
      * @param Client client client to be deleted
      */
     public void delete(int blockId, Client client){               
          if(cache.containsKey(blockId)){
                HashSet<Client> clientSet=(HashSet<Client>)cache.get(blockId);
                clientSet.remove(client);          
                cache.remove(blockId);
                if(!clientSet.isEmpty()) cache.put(blockId, clientSet);
          }
     }
        
}
