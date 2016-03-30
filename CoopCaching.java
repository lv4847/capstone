/**
 * This class starts the cooperative caching algorithm
 *
 * @author Lohit Velagapudi
 */
class CoopCaching{

     /**
      * This method starts each client as an individual thread
      *
      * @return Client[] client array of client objects
      */
     public void startAlgorithm(Client[] client){
          try{
               for(int i=0; i<client.length; i++){
                    client[i].start();
               }
               for(int i=0; i<client.length; i++){
                    client[i].join();
               }
          }catch(Exception e){
               e.printStackTrace();
          }
     }

}
