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
     protected BloomFilter bf;
     protected Manager mgr;
     protected int hitCount;
     protected int falsePositive;

     /**
      * The constructor
      *
      * @param int id client identifier
      * @param int cacheSize size of the client cache
      * @param BloomFilter bf the bloomfilter or IBF object
      * @param Manager mgr the manager object
      */
     public Client(int id, int cacheSize, BloomFilter bf, Manager mgr){
          this.id=id;
          this.cache=new LRUCache<Integer, Block>(cacheSize);
          this.bf=bf;
          this.mgr=mgr;
          this.hitCount=0;
          this.falsePositive=0;
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
      * @param int newVal block id
      */
     public void initAddCache(int newVal){
          synchronized(cache){
               cache.put(newVal, new Block(newVal));
               bf.addSeenMember(""+newVal);
               mgr.add(newVal, this);
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
     public abstract Block getBlock(int blockId);
     

     /**
      * This method reads a trace file with the same name as the client Id
      */
     public void readTrace() throws FileNotFoundException, IOException{
          File file=new File("E:\\capstone\\trace\\"+id+".txt");
          //File file=new File(id+".txt");
          FileReader freader=new FileReader(file);
          BufferedReader breader=new BufferedReader(freader);
          String line=null;
          //writer=new FileWriter("outputlog"+id+".txt");
          while((line=breader.readLine())!=null){
               int traceVal=Integer.parseInt(line);
               findBlock(traceVal);               
          }
          //writer.close();
     }

     /**
      * This method receives the value from the trace file to be looked for
      *
      * @param int val the block id to be looked for
      */
     private void findBlock(int val) throws IOException, FileNotFoundException{
          //System.out.println("Client "+id+" looking for "+val);
          
          Block data=getBlock(val);
          if(data==null){
               //writer.write("Lookup: "+val+" Client Id "+id+"\nClient");
               //display(writer);
               Client otherClient=mgr.getClient(val);
               if(otherClient!=null) {
                    data=otherClient.getBlock(val);
                    //System.out.println("Found at Client "+otherClient.id);
               }
               if(data==null) {
                    //writer.write("Manager: ");
                    //mgr.display(writer);
                    data=mgr.getFromServer(val);
                    //System.out.println("Requested from Server");
               }

               if(data!=null) addBlock(data);        
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
     
     /*public void display(FileWriter writer) throws IOException, FileNotFoundException{
          synchronized(cache){
               writer.write(cache.toString()+"\n");
          }     
     }*/
     
     
}
