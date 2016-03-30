import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides hash values for a particular data
 *
 * @author Lohit Velagapudi
 */
class Hash{

     private BigInteger sha1Value;
     private BigInteger md5Value;

     /**
      * The constructor
      *
      * @param String input data whose hash values reqd
      */
     public Hash(String input){
          sha1Value=sha1(input);
          md5Value=md5(input);
     }

     /**
      * This method returns an array of hash values for a data
      * The array size is 4
      *
      * @param int max max range 
      */
     public int[] getBits(int max){
          int[] bits=new int[4];
          bits[0]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(1))).mod(BigInteger.valueOf(max)).intValue();
          bits[1]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(2))).mod(BigInteger.valueOf(max)).intValue();
          bits[2]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(3))).mod(BigInteger.valueOf(max)).intValue();
          bits[3]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(4))).mod(BigInteger.valueOf(max)).intValue();
          return bits;
     }

     /**
      * SHA1 hash function
      * 
      * @param String input data to be hashed
      */
     private BigInteger sha1(String input) {
		try {
			MessageDigest output = MessageDigest.getInstance("SHA1"); 
			output.update(input.getBytes());
			return new BigInteger(1, output.digest());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return null;
	}

     /**
      * MD5 hash function
      *
      * @param String input data to be hashed
      */
	private BigInteger md5(String input) {
		try {
			MessageDigest output = MessageDigest.getInstance("MD5"); 
			output.update(input.getBytes());
			return new BigInteger(1, output.digest());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return null;
	}

	/*public static void main(String[] args){
	     
	     System.out.println("Lohit ");
	     new Hash("Lohit");
	     System.out.println("Velagapudi ");
	     new Hash("Velagapudi");
	     
	     
	}*/
}
