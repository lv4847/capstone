import java.io.FileWriter;
import java.util.Random;

class TraceGenerator{
     //number of trace files
     static int numOfTraces=100;

     //number of traces per file
     static int numOfTracesPerFile=1000;

     //max value in the trace file (for range)
     static int maxValueInFile=1000;
     
     public static void main(String[] args){
          for(int i=0; i<numOfTraces; i++){
               try{
                    FileWriter fwriter=new FileWriter(i+".txt");
                    Random randNum=new Random();
                    for(int j=0; j<numOfTracesPerFile; j++){
                         fwriter.write(randNum.nextInt(maxValueInFile)+"\n");
                    }
                    fwriter.close();
               }catch(Exception e){
                    System.out.println(e);
               }
          }
     }
}
