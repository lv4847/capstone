/**
 * This class is an implementation of Server
 */
class Server{

     private Block[] block;
     private int diskSize;
     private int diskAccess;
     private int diskAccessTicks;
     private int ticks;

     /**
      * The constructor
      * 
      * @param int diskSize size of the disk
      */
     public Server(int diskSize, int diskAccessTicks){
          this.diskSize=diskSize;
          this.diskAccess=0;
          this.block=new Block[diskSize];
          this.diskAccessTicks=diskAccessTicks;
          this.ticks=0;
          
          for(int i=0; i<diskSize; i++){
               block[i]=new Block(i);
          }
     }

     public long getTotalTicks(){
          return ticks;
     }

     /**
      * This method returns the requested block
      * increments disk access whenever method is called
      *
      * @param int id block id
      * @return Block requested block
      */
     public Block getBlock(int id){
          diskAccess++;
          ticks+=diskAccessTicks;
          if(id<diskSize) {               
               return block[id];
          }
          else return null;
     }

     /**
      * getter for disk access count
      *
      * @return int  disk access count
      */
     public int diskAccessCount(){
          return diskAccess;
     }
}
