import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.Properties;
import java.io.FileInputStream;

class BloomFilterTester{

     
     private int cacheSize;
     private String inputTrace;
     private int bfType;
     private BloomFilter bf;
     private LRUCache<Integer, Integer> cache;
     private String outfileBFSizeVary;
     private String outfileRequestVary;

     private void recordExperiment(){
          try{
               FileWriter bfVaryWriter=new FileWriter(outfileBFSizeVary);
               bfVaryWriter.write("bfsize/cacheSize, false positive\n");
               for(int i=1; i<=10; i++){
                    cache=new LRUCache<Integer, Integer>(cacheSize);
                    if(bfType==1) bf=new BloomFilter(cacheSize*i);
                    else if(bfType==2) bf=new ImpBloomFilter(cacheSize*i);
                    cacheLoadData();
                    long falsePos=startTest();
                    System.out.println("i: "+i+"\t fp: "+falsePos);
                    bfVaryWriter.write(i+", "+falsePos+"\n");
               }
               bfVaryWriter.close();
           }catch(FileNotFoundException e){
               e.printStackTrace();
           }catch(IOException e){
               e.printStackTrace();
           }    
     }

     private long startTest() throws FileNotFoundException, IOException{
          long fp=0;

          File file=new File(inputTrace);
          FileReader freader=new FileReader(file);
          BufferedReader breader=new BufferedReader(freader);
          String line=null;
          int reqCount=0;
          FileWriter fwriter=new FileWriter(outfileRequestVary);
          fwriter.write("#Request, FalsePositves\n");
          while((line=breader.readLine())!=null){
               int val=Integer.parseInt(line);

               if(bf.isSeen(line)){
                    if(!cache.containsKey(val)) fp++;
               }

               if(!cache.containsKey(val)){
                    cache.put(val, val);
                    bf.addSeenMember(line);
               }
               reqCount++;
               if(reqCount%100==0) fwriter.write(reqCount+", "+fp+"\n");
          }
          fwriter.close();
          return fp;
     }

     private void cacheLoadData() throws FileNotFoundException, IOException{
          File file=new File(inputTrace);
          FileReader freader=new FileReader(file);
          BufferedReader breader=new BufferedReader(freader);
          String line=null;

          for(int i=0; i<cacheSize && (line=breader.readLine())!=null; i++){
               int lineInt=Integer.parseInt(line);
               cache.put(lineInt, lineInt);
               bf.addSeenMember(line);
          }
     } 

     private void readInputFile(String inputFile) throws FileNotFoundException, IOException{
          Properties prop=new Properties();
          FileInputStream fistream=new FileInputStream(inputFile);
          prop.load(fistream);
          cacheSize=Integer.parseInt(prop.getProperty("CacheSize"));
          inputTrace=prop.getProperty("InputFile");
          bfType=Integer.parseInt(prop.getProperty("BFType"));

          if(bfType==1) {
               outfileBFSizeVary="bf-"+"cache-"+cacheSize+".csv";
               outfileRequestVary="bf-request-variation.csv";
          }    
          else if(bfType==2){
                outfileBFSizeVary="ibf-"+"cache-"+cacheSize+".csv";
                outfileRequestVary="ibf-request-variation.csv";
          }      
     }
     
     public static void main(String[] args){
          try{     
               BloomFilterTester test=new BloomFilterTester();
               test.readInputFile(args[0]);
               test.recordExperiment();
          }catch(Exception e){
               e.printStackTrace();
          }     
     }
}
