class Server{
     private Block[] block;
     private int diskSize;
     private int diskAccess;

     public Server(int diskSize){
          this.diskSize=diskSize;
          this.diskAccess=0;
          this.block=new Block[diskSize];
          
          for(int i=0; i<diskSize; i++){
               block[i]=new Block(i);
          }
     }

     public Block getBlock(int id){
          diskAccess++;
          if(id<diskSize) {               
               return block[id];
          }
          else return null;
     }

     public int diskAccessCount(){
          return diskAccess;
     }
}
