class CoopCaching{

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

     //public int diskAccess();

     //public int cacheHits();
}
