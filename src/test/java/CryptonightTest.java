import com.jounaidr.Cryptonight;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptonightTest {

    List<String> inputData = Arrays
            .asList("This is a test",
                    "",
                    "oioi im brit-ish",
                    "abc",
                    "x123!$$£*^(%)$$$$");

    List<String> validHashes = Arrays
            .asList("c1d6521259b6a9d29eb19df3895c601bcb9ae1811ec3dd175a4a9c2949af14fe",
                    "e34985722288be50a2068f973f02248d62e7bc6a0a0dfca2eb84909724857a72",
                    "72bf63a5503919c9d25b9eaaaa50eac6fc1a93e852c4a1fced9b8246da2a22ab",
                    "15b5d19b1a77580c49be0560154d94aace754e6640388e2d738a0ab77f3f2c07",
                    "b29d5abcb136383eefef46d8f1142f8f7787156f15ae6823c5c9d5eef19cce35");

    @Test
    public void TestHashCorrect(){
        for (int i = 0; i < inputData.size(); i++) {
            Cryptonight cryptonight = new Cryptonight(inputData.get(i));
            assertEquals(validHashes.get(i),new String(Hex.encode(cryptonight.returnHash())));
        }
    }

    @Test
    public void TestHashSpeed(){
        long totalTime = 0;

        for (int i = 0; i < inputData.size(); i++) {
            for(int x = 0; x < 1000; x++){ //calculate each hash 1000 times each (5000 in total)
                long startTime = System.currentTimeMillis(); //start timer

                Cryptonight cryptonight = new Cryptonight(inputData.get(i)); //calculate hash

                long endTime = System.currentTimeMillis();

                totalTime = totalTime + (endTime - startTime); //end timer
            }
        }

        float hashRate = (5000 / ((float)totalTime / 1000));

        System.out.println("Total time taken after 5000 hashes for JNI com.jounaidr.CryptoNightJNI.Cryptonight is: " + (totalTime) + " milliseconds");
        System.out.println("Hash rate for JNI com.jounaidr.CryptoNightJNI.Cryptonight is: " + (hashRate) + "H/s");
    }
}