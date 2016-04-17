import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is an implementation of client
 * for simulated caching mechanism
 *
 * @author Lohit Velagapudi
 */
abstract class Client extends Thread{
     protected int id;
     protected LRUCache<Integer, Block> cache;
     protected int cacheSize;
     protected BloomFilter bf;
     protected Manager mgr;
     protected int hitCount;
     protected int falsePositive;
     protected int localCacheTicks;
     protected int ticks;
     private int requestCount;

     /**
      * The constructor
      *
      * @param int id client identifier
      * @param int cacheSize size of the client cache
      * @param BloomFilter bf the bloomfilter or IBF object
      * @param Manager mgr the manager object
      */
     public Client(int id, int cacheSize, BloomFilter bf, Manager mgr, int localCacheTicks){
          this.id=id;
          this.cache=new LRUCache<Integer, Block>(cacheSize);
          this.cacheSize=cacheSize;
          this.bf=bf;
          this.mgr=mgr;
          this.hitCount=0;
          this.falsePositive=0;
          this.localCacheTicks=localCacheTicks;
          this.ticks=0;
          this.requestCount=0;
     }

     /**
      * This method returns the Local Cache Ticks
      */
     public long getTotalTicks(){
          return ticks; 
     }

     /**
      * This method returns the number of requests received
      */
     public int getRequestCount(){
          return requestCount;
     }

     /**
      * This method returns the client id
      */
     public int getClientId(){
          return id;
     }

     /**
      * This method returns the client cache size
      */
     public int getCacheSize(){
          return cache.size();
     }
     
     /**
      * getter for hit count
      * 
      * @return int hit count
      */
     public int getHitCount(){
          return hitCount;
     }

     /**
      * getter for false positive count of BF
      *
      * @return int false positve count
      */
     public int getFalsePositives(){
          return falsePositive;
     }

     
     /**
      * This method is used to add contents to the 
      * client cache before the experiment starts
      * and the data is also represented in the bloomfilter
      * Every data added to the cache is added to
      * the global cache aswell
      *
      * @param int blockId block id
      */
     public void initAddCache(int blockId){
          synchronized(mgr){
               synchronized(cache){
                    cache.put(blockId, new Block(blockId));
                    bf.addSeenMember(""+blockId);
                    mgr.add(blockId, this);
               }
          }     
     }

     /**
      * This method adds a block to the cache
      * during the caching algorithm
      *
      * @param Block data block of data to be added to the cache
      */
     public abstract void addBlock(Block data);
     

     /**
      * This method finds the block of data in the cache
      *
      * @param int blockId block id to be found
      * @return Block  returns the block with the blockId or null 
      */
     public Block getBlock(int blockId, boolean fromServer){
          synchronized(cache){
               if(!fromServer) ticks+=localCacheTicks;
               if(bf.isSeen(""+blockId)){
                    Block block=null;
                    if(fromServer && cache.containsKey(blockId)){
                        block=new Block(blockId); 
                    }    
                    else if (!fromServer) block=cache.get(blockId);
                    if(block!=null){
                         hitCount++;
                         return block;
                    }
                    else falsePositive++;
               }
               return null;
          }
     }
     

     /**
      * This method reads a trace file with the same name as the client Id
      */
     public void readTrace() throws FileNotFoundException, IOException{
          File file=new File("trace\\"+id+".txt");
   
          FileReader freader=new FileReader(file);
          BufferedReader breader=new BufferedReader(freader);
          String line=null;
          
          while((line=breader.readLine())!=null){
               int traceVal=Integer.parseInt(line);
               findBlock(traceVal);
               requestCount++;               
          }
     }

     /**
      * This method receives the value from the trace file to be looked for
      *
      * @param int request the block id to be looked for
      */
     private void findBlock(int request) throws IOException, FileNotFoundException{
          Block data=getBlock(request, false);
          if(data==null){
               synchronized(mgr){
                    data=mgr.getBlock(request);
               }        
          }
          if(data!=null){
              addBlock(data);
          }
     }

     /**
      * This method returns the LRUCache of Client
      */
     public LRUCache getCache(){
          synchronized(mgr){
               return cache;
          }     
     }

     /**
      * The run method to start each client as a thread
      */    
     public void run(){
          try{
               readTrace();
          }catch(Exception e){
               e.printStackTrace();
          }     
     }
     
     
}
