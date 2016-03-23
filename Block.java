import java.util.List;

class Block{
     private int id;
     private int recirculationCount;
     private List<Integer>  clientList;

     public Block(int id){
          this.id=id;
          this.recirculationCount=7;
     }

     public int getId(){
          return id;
     }

     public int getRecirculationCount(){
          return recirculationCount;
     }

     public void decrementRecirculationCount(){
          recirculationCount--;
     }

     public void setRecirculationCount(int count){
          this.recirculationCount=count;
     }
}
