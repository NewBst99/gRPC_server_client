package gameservice;

import io.grpc.stub.StreamObserver;
import gameservice.ServiceProto.UserInfoRequest;
import gameservice.ServiceProto.UserInfoResponse;
import gameservice.ServiceProto.UserLocationRequest;
import gameservice.ServiceProto.UserLocationResponse;
import gameservice.ServiceProto.SkillRelationRequest;
import gameservice.ServiceProto.SkillRelationResponse;
import gameservice.ServiceProto.ItemRelationRequest;
import gameservice.ServiceProto.ItemRelationResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StubServer extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public void getUserInfo(UserInfoRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        int userid = request.getUserid();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String query = "SELECT nkname, curexp, maxexp, userlevel, " +
                    "curhp, maxhp, curmp, maxmp, attpower, statpoint, skillpoint" +
                    " FROM userinfo_table WHERE userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nkname = resultSet.getString("nkname");
                float curexp = resultSet.getFloat("curexp");
                float maxexp = resultSet.getFloat("maxexp");
                float userlevel = resultSet.getFloat("userlevel");
                float curhp = resultSet.getFloat("curhp");
                float maxhp = resultSet.getFloat("maxhp");
                float curmp = resultSet.getFloat("curmp");
                float maxmp = resultSet.getFloat("maxmp");
                float attpower = resultSet.getFloat("attpower");
                float statpoint = resultSet.getFloat("statpoint");
                float skillpoint = resultSet.getFloat("skillpoint");

                UserInfoResponse response = UserInfoResponse.newBuilder()
                        .setUserid(userid)
                        .setNkname(nkname)
                        .setCurexp(curexp)
                        .setMaxexp(maxexp)
                        .setUserlevel(userlevel)
                        .setCurhp(curhp)
                        .setMaxhp(maxhp)
                        .setCurmp(curmp)
                        .setMaxmp(maxmp)
                        .setAttpower(attpower)
                        .setStatpoint(statpoint)
                        .setSkillpoint(skillpoint)
                        .build();
                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(UserInfoResponse.newBuilder()
                        .build());
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void saveUserInfo(UserInfoRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String checkQuery = "SELECT COUNT(*) FROM userinfo_table WHERE userid = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, request.getUserid());

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    resultSet.next();
                    boolean recordExists = resultSet.getInt(1) > 0;
                    String query;

                    if (recordExists) {
                        query = "UPDATE userinfo_table SET nkname = ? , curexp = ?, maxexp = ?, userlevel = ?, " +
                                "curhp = ?, maxhp = ?, curmp = ?, maxmp = ?, attpower = ?, statpoint = ?, skillpoint = ?" +
                                " WHERE userid = ?";
                    } else {
                        query = "INSERT INTO userinfo_table (userid, nkname, curexp, maxexp, userlevel, " +
                                "curhp, maxhp, curmp, maxmp, attpower, statpoint, skillpoint) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    }

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        if (recordExists) {
                            // Set parameters for the UPDATE statement
                            statement.setString(1, request.getNkname());
                            statement.setFloat(2, request.getCurexp());
                            statement.setFloat(3, request.getMaxexp());
                            statement.setFloat(4, request.getUserlevel());
                            statement.setFloat(5, request.getCurhp());
                            statement.setFloat(6, request.getMaxhp());
                            statement.setFloat(7, request.getCurmp());
                            statement.setFloat(8, request.getMaxmp());
                            statement.setFloat(9, request.getAttpower());
                            statement.setFloat(10, request.getStatpoint());
                            statement.setFloat(11, request.getSkillpoint());
                            statement.setFloat(12, request.getUserid());
                        } else {
                            // Set parameters for the INSERT statement
                            statement.setFloat(1, request.getUserid());
                            statement.setString(2, request.getNkname());
                            statement.setFloat(3, request.getCurexp());
                            statement.setFloat(4, request.getMaxexp());
                            statement.setFloat(5, request.getUserlevel());
                            statement.setFloat(6, request.getCurhp());
                            statement.setFloat(7, request.getMaxhp());
                            statement.setFloat(8, request.getCurmp());
                            statement.setFloat(9, request.getMaxmp());
                            statement.setFloat(10, request.getAttpower());
                            statement.setFloat(11, request.getStatpoint());
                            statement.setFloat(12, request.getSkillpoint());
                        }

                        // Execute the appropriate statement
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            UserInfoResponse response = UserInfoResponse.newBuilder()
                                    .build();
                            responseObserver.onNext(response);
                        } else {
                            responseObserver.onNext(UserInfoResponse.newBuilder()
                                    .build());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseObserver.onError(e);
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void getUserLocation(UserLocationRequest request, StreamObserver<UserLocationResponse> responseObserver) {
        int userid = request.getUserid();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String query = "SELECT xloc, yloc, zloc FROM userlocation_table WHERE userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                float xloc = resultSet.getFloat("xloc");
                float yloc = resultSet.getFloat("yloc");
                float zloc = resultSet.getFloat("zloc");

                UserLocationResponse response = UserLocationResponse.newBuilder()
                        .setUserid(userid)
                        .setXloc(xloc)
                        .setYloc(yloc)
                        .setZloc(zloc)
                        .build();

                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(UserLocationResponse.newBuilder()
                        .build());
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void saveUserLocation(UserLocationRequest request, StreamObserver<UserLocationResponse> responseObserver) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String checkQuery = "SELECT COUNT(*) FROM userlocation_table WHERE userid = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, request.getUserid());

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    resultSet.next();
                    boolean recordExists = resultSet.getInt(1) > 0;
                    String query;

                    if (recordExists) {
                        query = "UPDATE userlocation_table SET xloc = ?, yloc = ?, zloc = ? WHERE userid = ?";
                    } else {
                        query = "INSERT INTO userlocation_table (userid, xloc, yloc, zloc) VALUES (?, ?, ?, ?)";
                    }

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        if (recordExists) {
                            // Set parameters for the UPDATE statement
                            statement.setFloat(1, request.getXloc());
                            statement.setFloat(2, request.getYloc());
                            statement.setFloat(3, request.getZloc());
                            statement.setInt(4, request.getUserid());
                        } else {
                            statement.setInt(1, request.getUserid());
                            statement.setFloat(2, request.getXloc());
                            statement.setFloat(3, request.getYloc());
                            statement.setFloat(4, request.getZloc());
                        }

                        // Execute the appropriate statement
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            UserLocationResponse response = UserLocationResponse.newBuilder()
                                    .build();
                            responseObserver.onNext(response);
                        } else {
                            responseObserver.onNext(UserLocationResponse.newBuilder()
                                    .build());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseObserver.onError(e);
            }
            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void getSkillRelation(SkillRelationRequest request, StreamObserver<SkillRelationResponse> responseObserver) {
        int userid = request.getUserid();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            // skillrelation_table, skilllist_table JOIN -> get skillname
            String query = "SELECT sr.skillid, sl.skillname, sr.skilllevel " +
                    "FROM skillrelation_table sr " +
                    "JOIN skilllist_table sl ON sr.skillid = sl.skillid " +
                    "WHERE sr.userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int skillid = resultSet.getInt("skillid");
                String skillname = resultSet.getString("skillname");
                int skilllevel = resultSet.getInt("skilllevel");

                SkillRelationResponse response = SkillRelationResponse.newBuilder()
                        .setUserid(userid)
                        .setSkillid(skillid)
                        .setSkillname(skillname)
                        .setSkilllevel(skilllevel)
                        .build();

                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(SkillRelationResponse.newBuilder()
                        .build());
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void saveSkillRelation(SkillRelationRequest request, StreamObserver<SkillRelationResponse> responseObserver) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String query = "REPLACE INTO skillrelation_table (userid, skillid, skilllevel) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, request.getUserid());
                statement.setInt(2, request.getSkillid());
                statement.setInt(3, request.getSkilllevel());
                statement.executeUpdate();

                responseObserver.onNext(SkillRelationResponse.newBuilder()
                        .build());
            }
            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void getItemRelation(ItemRelationRequest request, StreamObserver<ItemRelationResponse> responseObserver) {
        int userid = request.getUserid();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            // itemrelation_table, itemlist_table JOIN -> get itemname
            String query = "SELECT ir.itemid, il.itemname, ir.quantity " +
                    "FROM itemrelation_table ir " +
                    "JOIN itemlist_table il ON ir.itemid = il.itemid " +
                    "WHERE ir.userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String itemid = resultSet.getString("itemid");
                String itemname = resultSet.getString("itemname");
                int quantity = resultSet.getInt("quantity");

                ItemRelationResponse response = ItemRelationResponse.newBuilder()
                        .setUserid(userid)
                        .setItemid(itemid)
                        .setItemname(itemname)
                        .setQuantity(quantity)
                        .build();

                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(ItemRelationResponse.newBuilder()
                        .build());
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }


    @Override
    public void saveItemRelation(ItemRelationRequest request, StreamObserver<ItemRelationResponse> responseObserver) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String query = "REPLACE INTO itemrelation_table (userid, itemid, quantity) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, request.getUserid());
                statement.setString(2, request.getItemid());
                statement.setInt(3, request.getQuantity());
                statement.executeUpdate();

                responseObserver.onNext(ItemRelationResponse.newBuilder()
                        .build());
            }
            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

}
