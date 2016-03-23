import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

class LRUCache extends LinkedHashMap{

     private int size;
     
     public LRUCache(int size){
          super(size, 0.7f, true);
          this.size=size;
     }

     protected boolean removeEldestEntry(Map.Entry eldest){
          return size()>size;
     }

     public Object getLRU(){
          Set set=this.entrySet();
          Iterator itr=set.iterator();
          if(itr.hasNext()){
               Map.Entry me=(Map.Entry) itr.next();
               return me.getValue();
          }
          return null;
     }
     
     /*public static void main(String[] args){
          LRUCache cache=new LRUCache(3);

          cache.put(1, "Lohit");
          cache.put(2, "Velagapudi");
          cache.put(3, "asd");
          System.out.println(cache);
          System.out.println(cache.getLRU());
          //cache.get(1);
          System.out.println(cache);
          System.out.println(cache.getLRU());
          //cache.put(4, "kolli");
          System.out.println(cache);
          System.out.println(cache.getLRU());
          //cache.put(5, "nishi");
          System.out.println(cache);
          System.out.println(cache.getLRU());
          //cache.get(6);
          System.out.println(cache);
          System.out.println(cache.getLRU());
           
     }*/
}
