package gameservice;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import gameservice.ServiceProto.MapProgressRequest;
import gameservice.ServiceProto.MapProgressResponse;
import gameservice.ServiceProto.UserInfoRequest;
import gameservice.ServiceProto.UserInfoResponse;
import gameservice.ServiceProto.ItemRelationRequest;
import gameservice.ServiceProto.ItemRelationResponse;

public class StubClient {

    private final ManagedChannel channel;
    private final GameServiceGrpc.GameServiceBlockingStub blockingStub;

    public StubClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = GameServiceGrpc.newBlockingStub(channel);
    }

    public void getUserInfo(int userid) {
        UserInfoRequest request = UserInfoRequest.newBuilder()
                .setUserid(userid)
                .build();

        UserInfoResponse response = blockingStub.getUserInfo(request);
        System.out.println(response.getCheckmessage());
        System.out.println("User ID : " + response.getUserid());
        System.out.println("Nickname : " + response.getNkname());
        System.out.println("Experience : " + response.getExp());
        System.out.println("Save Time : " + response.getSavetime());
    }

    public void saveUserInfo(int userid, String nkname, int exp, String savetime) {
        UserInfoRequest request = UserInfoRequest.newBuilder()
                .setUserid(userid)
                .setNkname(nkname)
                .setExp(exp)
                .setSavetime(savetime)
                .build();

        UserInfoResponse response = blockingStub.saveUserInfo(request);
        System.out.println(response.getCheckmessage());
    }

    public void getMapProgress(int userid){
        MapProgressRequest request = MapProgressRequest.newBuilder()
                .setUserid(userid)
                .build();

        MapProgressResponse response = blockingStub.getMapProgress(request);
        double x = response.getXloc();
        double y = response.getYloc();
        double z = response.getZloc();

        System.out.println(response.getCheckmessage());
        System.out.println("User ID : " + response.getUserid());
        System.out.println("User Location : " + x + ", " + y + ", " + z);
        System.out.println("Map Progress : " + response.getMapprogress());
    }

    public void saveMapProgress(int userid, int mapprogress, double xloc, double yloc, double zloc) {
        MapProgressRequest request = MapProgressRequest.newBuilder()
                .setUserid(userid)
                .setMapprogress(mapprogress)
                .setXloc(xloc)
                .setYloc(yloc)
                .setZloc(zloc)
                .build();
        MapProgressResponse response = blockingStub.saveMapProgress(request);
    }

    public void getItemRelation(int userid) {
        ItemRelationRequest request = ItemRelationRequest.newBuilder()
                .setUserid(userid)
                .build();

        ItemRelationResponse response = blockingStub.getItemRelation(request);
        System.out.println(response.getCheckmessage());
        System.out.println("User ID : " + response.getUserid());
        System.out.println("Item ID : " + response.getItemid());
        System.out.println("Item Name : " + response.getItemname());
        System.out.println("Quantity : " + response.getQuantity());
    }

    public void saveItemRelation(int userid, String itemid, int quantity) {
        ItemRelationRequest request = ItemRelationRequest.newBuilder()
                .setUserid(userid)
                .setItemid(itemid)
                .setQuantity(quantity)
                .build();

        ItemRelationResponse response = blockingStub.saveItemRelation(request);
    }

    public void shutdown() {
        channel.shutdown();
    }

    public static void main(String[] args) {
        StubClient client = new StubClient("localhost", 9090);

        // Save User Info
//        client.saveUserInfo(1, "gameservice", 550, "1999-07-16 21:57:15");
//        client.saveUserInfo(2, "daewook", 220, "1945-07-16 19:45:35");
//        client.saveUserInfo(3, "seungtae", 600, "2000-12-07 15:19:41");
//        client.saveUserInfo(4, "junyeong", 210, "1873-07-16 11:23:14");
//        client.saveUserInfo(5, "minguk", 520, "2000-03-07 15:35:24");
//
//        // Save Game Progress
//        client.saveMapProgress(1, 1, -40, -8, 0);
//        client.saveMapProgress(3, 0, -50, -9, 1);
//        // Save User Item
//        client.saveItemRelation(1, "c100", 1);
//        client.saveItemRelation(1, "c101", 2);
//        client.saveItemRelation(3, "c100", 1);
//        client.saveItemRelation(3, "c101", 1);

        client.getUserInfo(1);
        client.getMapProgress(1);
        client.getItemRelation(1);
        client.getUserInfo(3);
        client.getMapProgress(3);
        client.getItemRelation(3);

        client.shutdown();
    }
}