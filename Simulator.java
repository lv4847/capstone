import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
/**
 * This class initiates the experiment and has the main method
 *
 * @author Lohit Velagapudi
 */
class Simulator{
     private Server server;
     private Client[] client;
     private Manager mgr;

     // 1: varying client cache size 2: varying # clients
     private int exptType;    
     
     //1: Greedy Forwarding 2: NChance 3:Robinhood
     private int cachingAlgo;

     //1. BloomFilter 2: IBF
     private int bfType;
     
     private int numOfClients;
     private int minNumOfClients;
     private int maxNumOfClients;

     private int clientCacheSize;
     private int minClientCacheSize;
     private int maxClientCacheSize;
     
     private int increments;
     private int diskSize;
     
     private String traceFile;

     private int clientCacheTicks;
     private int globalCacheTicks;
     private int diskAccessTicks;

     /**
      * This method processes the input file and sets the parameters
      * for the experiment
      *
      * @param String filename input file name
      */
     void readInputFile(String fileName) throws FileNotFoundException, IOException{
          Properties prop=new Properties();
          FileInputStream inputFile=new FileInputStream(fileName);
          prop.load(inputFile);
          exptType=Integer.parseInt(prop.getProperty("Expt"));
          cachingAlgo=Integer.parseInt(prop.getProperty("CachingAlgo"));
          bfType=Integer.parseInt(prop.getProperty("BFType"));
          diskSize=Integer.parseInt(prop.getProperty("DiskSize"));
          increments=Integer.parseInt(prop.getProperty("Increment"));
          traceFile=prop.getProperty("TraceFile");

          clientCacheTicks=Integer.parseInt(prop.getProperty("ClientCacheTicks"));
          globalCacheTicks=Integer.parseInt(prop.getProperty("GlobalCacheTicks"));
          diskAccessTicks=Integer.parseInt(prop.getProperty("DiskAccessTicks"));
          
          if(exptType==1){
               minClientCacheSize=Integer.parseInt(prop.getProperty("MinClientCacheSize"));
               maxClientCacheSize=Integer.parseInt(prop.getProperty("MaxClientCacheSize"));
               numOfClients=Integer.parseInt(prop.getProperty("NumOfClients"));
          }
          else if(exptType==2){
               minNumOfClients=Integer.parseInt(prop.getProperty("MinNumOfClients"));
               maxNumOfClients=Integer.parseInt(prop.getProperty("MaxNumOfClients"));
               clientCacheSize=Integer.parseInt(prop.getProperty("ClientCacheSize"));
          }
     }

     /**
      * This method records the experiment results by varying the
      * client cache size or number of clients
      */
     public void generateResults() throws FileNotFoundException, IOException{
          
          System.out.println("Starting....");
          if(exptType==1){
               
               FileWriter fwriter=new FileWriter("Clients-"+numOfClients+".csv");
               fwriter.write("ClientCacheSize, CacheHits, DiskAccess, FalsePositive, Ticks/Request, GlobalCacheSize\n");
               for(int i=minClientCacheSize; i<=maxClientCacheSize; i=i+increments){                    
                    System.out.println("Cache Size: "+i+"\t# Clients: "+numOfClients);
                    if(loadData(numOfClients, i)) runCachingAlgorithm(i, fwriter);
               }
               fwriter.close();
          }
          else if(exptType==2){
               FileWriter fwriter=new FileWriter("CacheSize-"+clientCacheSize+".csv");
               fwriter.write("#Clients, CacheHits/Client, DiskAccess/Client, FalsePositive/Client, Ticks/Request, GlobalCacheSize\n");
               for(int i=minNumOfClients; i<=maxNumOfClients; i=i+increments){
                    System.out.println("Cache Size: "+clientCacheSize+"\t# Clients: "+i);
                    if(loadData(i, clientCacheSize)) runCachingAlgorithm(i, fwriter);
               }
               fwriter.close();
          }
          
          System.out.println("End");
     }

     /**
      * This method adds data to the cache of clients and manager
      *
      * @param int clients number of clients
      * @param int cacheSizeClient size of the client cache
      * @return boolean true if loading data successfull or else false
      */
     private boolean loadData(int clients, int cacheSizeClient) throws FileNotFoundException, IOException{
          server=new Server(diskSize, diskAccessTicks);
                    
          int bfSize=cacheSizeClient*clients*10;
          
          if (bfType==1) mgr=getManager(clients*cacheSizeClient, new BloomFilter(bfSize), server, globalCacheTicks);
          else if (bfType==2) mgr=getManager(clients*cacheSizeClient, new ImpBloomFilter(bfSize), server, globalCacheTicks);
          
          client=new Client[clients];
          bfSize=cacheSizeClient*10;
          for(int i=0; i<clients; i++){
               if(bfType==1) client[i]=getClient(i, cacheSizeClient, new BloomFilter(bfSize), mgr, client, clientCacheTicks);
               else if(bfType==2) client[i]=getClient(i, cacheSizeClient, new ImpBloomFilter(bfSize), mgr, client, clientCacheTicks);
          }

          mgr.setClients(client);          
          File file=new File(traceFile);
          FileReader freader=new FileReader(file);
          BufferedReader breader=new BufferedReader(freader);
          String line=null;

          for(int i=0; i<clients; i++){
               for(int j=0; j<cacheSizeClient && (line=breader.readLine())!=null; j++){
                    int lineInt=Integer.parseInt(line);
                    client[i].initAddCache(lineInt);
               }
               if(line==null && i<clients-1) return false;
          }

          return true;
     }

     /**
      * This method runs the algorithm for each case i.e. number of clients
      * and client cache size
      *
      * @param int var value changing for each run
      * @param Filewriter writer for writing to the ouput file
      */
     private void runCachingAlgorithm(int var, FileWriter writer) throws IOException{
          System.out.println("Running..");
          CoopCaching cacheAlgo=new CoopCaching();

          cacheAlgo.startAlgorithm(client);

          int hitCount=0;
          int falsePositive=0;
          long ticks=0;
          int reqCount=0;
          
          for(int i=0; i<client.length; i++){
               hitCount=hitCount+client[i].getHitCount();
               falsePositive=falsePositive+client[i].getFalsePositives();
               ticks=ticks+client[i].getTotalTicks();
               reqCount=reqCount+client[i].getRequestCount();
          }
          ticks=ticks+mgr.getTotalTicks();
          ticks=ticks+server.getTotalTicks();
          System.out.println("Manager: "+(((float)mgr.getCacheSize()/diskSize)*100));
          System.out.println("Hit Count: "+ (((float)hitCount/reqCount)*100));
          System.out.println("Disk Access: "+(((float)server.diskAccessCount()/reqCount)*100));
          System.out.println("Ticks: "+((float)ticks/reqCount));
          System.out.println();
          
          writer.write(var+", "+(((float)hitCount/reqCount)*100)+", "+(((float)server.diskAccessCount()/reqCount)*100)+", "
          +(((float)falsePositive/reqCount)*100)+", "+(((float)ticks/reqCount))+","+(((float)mgr.getCacheSize()/diskSize)*100)+"\n");
     }

     

     /**
      * This method returns specific Client object for a caching algorithm
      * @param int id client identifier
      * @param int cacheSize size of the client cache
      * @param BloomFilter bf the bloomfilter or IBF object
      * @param Manager mgr the manager object
      * @param Client[] client list of client objects
      */
     private Client getClient(int id, int cacheSize, BloomFilter bf, Manager mgr, Client[] client, int clientCacheTicks){
          if(cachingAlgo==1) return new GFClient(id, cacheSize, bf, mgr, clientCacheTicks);
          else if(cachingAlgo==2) return new NCClient(id, cacheSize, bf, mgr, client, clientCacheTicks);
          else if(cachingAlgo==3) return new RHClient(id, cacheSize, bf, mgr, client, clientCacheTicks);
          return null;

     }

     /**
      * This method returns specific Manager object for a caching algorithm
      * @param int cacheSize size of cache
      * @param BloomFilter bf bloomfilter object
      * @param Server server server object
      */
     private Manager getManager(int cacheSize, BloomFilter bf, Server server, int globalCacheTicks){
          if(cachingAlgo==1) return new GFManager(cacheSize, bf, server, globalCacheTicks);
          else if(cachingAlgo==2) return new NCManager(cacheSize, bf, server, globalCacheTicks);
          else if(cachingAlgo==3) return new RHManager(cacheSize, bf, server, globalCacheTicks);
          return null;
     }

     /**
      * The main method
      *
      * @param String[] args input file name
      */
     public static void main(String[] args){
          try{
               Simulator sim=new Simulator();
               sim.readInputFile(args[0]);
               sim.generateResults();
          }catch(FileNotFoundException e){
               e.printStackTrace();
          }catch(IOException e){
               e.printStackTrace();
          }
     }
}
