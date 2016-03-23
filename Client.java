import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

abstract class Client extends Thread{
     protected int id;
     protected LRUCache cache;
     protected BloomFilter bf;
     protected Manager mgr;
     protected int hitCount;
     protected int falsePositive;

     public Client(int id, int cacheSize, BloomFilter bf, Manager mgr){
          this.id=id;
          this.cache=new LRUCache(cacheSize);
          this.bf=bf;
          this.mgr=mgr;
          this.hitCount=0;
          this.falsePositive=0;
     }

     public int getHitCount(){
          return hitCount;
     }

     public int getFalsePositives(){
          return falsePositive;
     }
     
     public void initAddCache(int newVal){
          synchronized(cache){
               cache.put(newVal, new Block(newVal));
               bf.addSeenMember(""+newVal);
               mgr.add(newVal, this);
          }     
     }

     public abstract void addBlock(Block data);
     

     public abstract Block getBlock(int blockId);
     

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
