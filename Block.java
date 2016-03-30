import java.util.List;
/**
 * The block represents smallest unit in memory
 *
 * @author Lohit Velagapudi
 *
 */
class Block{
     private int id;
     private int recirculationCount;
     private List<Integer>  clientList;

     /**
      * The constructor
      *
      * @param id int unique identifier for every string
      */
     public Block(int id){
          this.id=id;
          this.recirculationCount=7;
     }

     /**
      * getter for the block id
      *
      * @return int block id
      */
     public int getId(){
          return id;
     }

     /**
      * getter for recirculation count
      *
      * @return int recirculation count
      */
     public int getRecirculationCount(){
          return recirculationCount;
     }

     /**
      * decrements recirculation count by 1
      */
     public void decrementRecirculationCount(){
          recirculationCount--;
     }

}
