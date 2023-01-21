//package pers.yujie.dashboard.service.impl;
//
//import io.ipfs.multihash.Multihash;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//
//class NodeServiceImplTest {
//
//  @Test
//  void testPartition() {
//    List<Integer> list = new ArrayList<>();
//    list.add(1);
//    list.add(2);
//    list.add(3);
//    list.add(4);
//
////    List<List<Integer>> partitions = ListUtils.partition(list, 3);
////    System.out.println(partitions);
//  }
//
//  @Test
//  void testString() {
//    List<Multihash> list = new ArrayList<>();
//    list.add(Multihash.fromBase58("QmevHYpfvatpQ7rYAGb5mCxHZDsCELk1seBUr3Hfu97H14"));
//    list.add(Multihash.fromBase58("QmevHYpfvatpQ7rYAGb5mCxHZDsCELk1seBUr3Hfu97H14"));
//
//    StringBuilder builder = new StringBuilder();
//    for (Multihash ref : list) {
//      builder.append(ref.toString());
//      builder.append(" ");
//    }
//    String refHashList = builder.toString();
//    System.out.println(list.toString());
//  }
//
//  @Test
//  void testBigInteger() {
//    BigInteger index = BigInteger.ONE;
//    System.out.println("this is " + index);
//  }
//
//}