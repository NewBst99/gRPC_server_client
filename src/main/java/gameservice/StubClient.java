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
        System.out.println(response.getCheckmessage());
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
        System.out.println(response.getCheckmessage());
    }

    public void shutdown() {
        channel.shutdown();
    }

    public static void main(String[] args) {
        StubClient client = new StubClient("localhost", 9090);

        // Save User Info
        client.saveUserInfo(1, "user1", 55, "2024-10-16 21:57:15");
        client.saveUserInfo(2, "user2", 22, "2024-09-10 19:45:35");
        client.saveUserInfo(3, "user3", 60, "2024-09-07 15:19:41");
        client.saveUserInfo(4, "user4", 21, "2024-08-23 11:23:14");
        client.saveUserInfo(5, "user5", 52, "2024-03-07 15:35:24");

        // Save Game Progress
        client.saveMapProgress(1, 1, -40, -8, 0);
        client.saveMapProgress(2, 0, -50, -9, 1);
        // Save User Item
        client.saveItemRelation(1, "c100", 1);
        client.saveItemRelation(1, "c101", 2);
        client.saveItemRelation(2, "c100", 1);
        client.saveItemRelation(2, "c101", 1);

        // Get Info
        client.getUserInfo(1);
        client.getMapProgress(1);
        client.getItemRelation(1);
        client.getUserInfo(2);
        client.getMapProgress(2);
        client.getItemRelation(2);

        client.shutdown();
    }
}