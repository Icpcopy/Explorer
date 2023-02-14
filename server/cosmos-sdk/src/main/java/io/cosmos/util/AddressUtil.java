package io.cosmos.util;

import com.google.common.primitives.Bytes;
import com.google.crypto.tink.subtle.Bech32;
import io.cosmos.crypto.encode.ConvertBits;
import io.cosmos.crypto.hash.Ripemd;
import io.cosmos.exception.AddressFormatException;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddressUtil {

    public static String createNewAddressSecp256k1(String mainPrefix, byte[] publickKey) throws Exception {
        String addressResult = null;
        try {
            byte[] pubKeyHash = sha256Hash(publickKey, 0, publickKey.length);
            byte[] address = Ripemd.ripemd160(pubKeyHash);
            byte[] bytes = encode(0, address);
            addressResult = io.cosmos.crypto.encode.Bech32.encode(mainPrefix, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addressResult;

    }

    public static byte[] getPubkeyValue(byte[] publickKey) throws Exception {
        try {
            byte[] pubKeyHash = sha256Hash(publickKey, 0, publickKey.length);
            byte[] value = Ripemd.ripemd160(pubKeyHash);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static byte[] decodeAddress(String address){
        byte[] dec = Bech32.decode(address).getData();
        return ConvertBits.convertBits(dec, 0, dec.length, 5, 8, false);
    }

    private static byte[] sha256Hash(byte[] input, int offset, int length) throws NoSuchAlgorithmException {
        byte[] result = new byte[32];
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(input, offset, length);
        return digest.digest();
    }

    private static byte[] encode(int witnessVersion, byte[] witnessProgram) throws AddressFormatException {
        byte[] convertedProgram = ConvertBits.convertBits(witnessProgram, 0, witnessProgram.length, 8, 5, true);
        return convertedProgram;
    }
    public static String convertCosmosToEthAddress(String cosmosAddress) throws Exception {
        Bech32.Bech32Data data = Bech32.decode(cosmosAddress);
        byte[] dec = data.getData();
        dec = ConvertBits.convertBits(dec, 0, dec.length, 5, 8, false);
        return "0x" + Hex.toHexString(dec);
    }
    /**
     * 从以太坊地址生成cosmos地址
     */
    public static String convertEthAddressToCosmos(String ethAddress, String prefix) throws Exception {
        if (ethAddress.startsWith("0x")) {
            ethAddress = ethAddress.substring(2);
        }
        byte[] bytes = Hex.decode(ethAddress);

        byte[] addr = ConvertBits.convertBits(bytes, 0, bytes.length, 8, 5, true);
        return Bech32.encode(prefix, addr);
    }
    public static void main(String[] args) throws Exception {
        String address = convertCosmosToEthAddress("icplaza1mtqhl9vd9mjj8g3qvgrfj3vhcy7cx8k88wpzlx");
        System.out.println(address);
        System.out.println(convertEthAddressToCosmos("0xc64d7e3d4762b958ae852a641adb77d29a25d612","gauss"));
    }
}
