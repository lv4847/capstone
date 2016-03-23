import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Hash{

     private BigInteger sha1Value;
     private BigInteger md5Value;
     
     //BigInteger hashValue;
     
     public Hash(String input){
          sha1Value=sha1(input);
          md5Value=md5(input);
          //System.out.println(sha1Value+" "+md5Value);
     }

     public int[] getBits(int max){
          int[] bits=new int[4];
          bits[0]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(1))).mod(BigInteger.valueOf(max)).intValue();
          bits[1]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(2))).mod(BigInteger.valueOf(max)).intValue();
          bits[2]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(3))).mod(BigInteger.valueOf(max)).intValue();
          bits[3]=sha1Value.add(md5Value.multiply(BigInteger.valueOf(4))).mod(BigInteger.valueOf(max)).intValue();
          return bits;
     }
     
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
