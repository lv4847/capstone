import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * This class is LRUCache implementation
 *
 * @author Lohit Velagapudi
 */
class LRUCache<K, V> extends LinkedHashMap<K, V>{

     private int size;

     /**
      * The constructor
      * 
      * @param int size size of LRUCache
      */
     public LRUCache(int size){
          super(size, 0.7f, true);
          this.size=size;
     }

     /**
      * This method checks the size of the LRUCahce
      * and decides when to eliminate the least recently used
      */
     protected boolean removeEldestEntry(Map.Entry<K, V> eldest){
          return size()>size;
     }

     /**
      * This method returns least recently used value in cache
      */
     public Object getLRU(){
          Set set=this.entrySet();
          Iterator itr=set.iterator();
          if(itr.hasNext()){
               Map.Entry<K, V> me=(Map.Entry<K, V>) itr.next();
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
