package gameservice;

import io.grpc.stub.StreamObserver;
import gameservice.ServiceProto.MapProgressRequest;
import gameservice.ServiceProto.MapProgressResponse;
import gameservice.ServiceProto.UserInfoRequest;
import gameservice.ServiceProto.UserInfoResponse;
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
            String query = "SELECT nkname, exp, savetime FROM userinfo_table WHERE userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nkname = resultSet.getString("nkname");
                int exp = resultSet.getInt("exp");
                String savetime = resultSet.getTimestamp("savetime").toString();

                UserInfoResponse response = UserInfoResponse.newBuilder()
                        .setUserid(userid)
                        .setNkname(nkname)
                        .setExp(exp)
                        .setSavetime(savetime)
                        .setCheckmessage("====== User info loaded successfully ======")
                        .build();

                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(UserInfoResponse.newBuilder()
                        .setCheckmessage("!!! User not found !!!")
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
                        query = "UPDATE userinfo_table SET nkname = ?, exp = ?, savetime = ? WHERE userid = ?";
                    } else {
                        query = "INSERT INTO userinfo_table (userid, nkname, exp, savetime) VALUES (?, ?, ?, ?)";
                    }

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        if (recordExists) {
                            // Set parameters for the UPDATE statement
                            statement.setString(1, request.getNkname());
                            statement.setInt(2, request.getExp());
                            statement.setString(3, request.getSavetime());
                            statement.setInt(4, request.getUserid());
                        } else {
                            // Set parameters for the INSERT statement
                            statement.setInt(1, request.getUserid());
                            statement.setString(2, request.getNkname());
                            statement.setInt(3, request.getExp());
                            statement.setString(4, request.getSavetime());
                        }

                        // Execute the appropriate statement
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            UserInfoResponse response = UserInfoResponse.newBuilder()
                                    .setCheckmessage(recordExists ? "====== User info updated successfully ======" : "====== User info saved successfully ======")
                                    .build();
                            responseObserver.onNext(response);
                        } else {
                            responseObserver.onNext(UserInfoResponse.newBuilder()
                                    .setCheckmessage("!!! Failed to save or update user info !!!")
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
    public void getMapProgress(MapProgressRequest request, StreamObserver<MapProgressResponse> responseObserver) {
        int userid = request.getUserid();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String query = "SELECT mapprogress, xloc, yloc, zloc FROM mapprogress_table WHERE userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int mapprogress = resultSet.getInt("mapprogress");
                double xloc = resultSet.getDouble("xloc");
                double yloc = resultSet.getDouble("yloc");
                double zloc = resultSet.getDouble("zloc");

                MapProgressResponse response = MapProgressResponse.newBuilder()
                        .setUserid(userid)
                        .setMapprogress(mapprogress)
                        .setXloc(xloc)
                        .setYloc(yloc)
                        .setZloc(zloc)
                        .setCheckmessage("====== Map progress loaded successfully ======")
                        .build();

                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(MapProgressResponse.newBuilder()
                        .setCheckmessage("!!! Failed to load map progress !!!")
                        .build());
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void saveMapProgress(MapProgressRequest request, StreamObserver<MapProgressResponse> responseObserver) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String checkQuery = "SELECT COUNT(*) FROM mapprogress_table WHERE userid = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, request.getUserid());

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    resultSet.next();
                    boolean recordExists = resultSet.getInt(1) > 0;
                    String query;

                    if (recordExists) {
                        query = "UPDATE mapprogress_table SET mapprogress = ?, xloc = ?, yloc = ?, zloc = ? WHERE userid = ?";
                    } else {
                        query = "INSERT INTO mapprogress_table (userid, mapprogress, xloc, yloc, zloc) VALUES (?, ?, ?, ?, ?)";
                    }

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        if (recordExists) {
                            // Set parameters for the UPDATE statement
                            statement.setInt( 1, request.getMapprogress());
                            statement.setDouble(2, request.getXloc());
                            statement.setDouble(3, request.getYloc());
                            statement.setDouble(4, request.getZloc());
                            statement.setInt(5, request.getUserid());
                        } else {
                            statement.setInt(1, request.getUserid());
                            statement.setInt( 2, request.getMapprogress());
                            statement.setDouble(3, request.getXloc());
                            statement.setDouble(4, request.getYloc());
                            statement.setDouble(5, request.getZloc());
                        }

                        // Execute the appropriate statement
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            MapProgressResponse response = MapProgressResponse.newBuilder()
                                    .setCheckmessage(recordExists ? "====== Map progress updated successfully ======" : "====== Map progress saved successfully ======")
                                    .build();
                            responseObserver.onNext(response);
                        } else {
                            responseObserver.onNext(MapProgressResponse.newBuilder()
                                    .setCheckmessage("!!! Failed to save or update map progress !!!")
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
    public void getItemRelation(ItemRelationRequest request, StreamObserver<ItemRelationResponse> responseObserver) {
        int userid = request.getUserid();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String query = "SELECT itemid, itemname, quantity FROM itemrelation_table WHERE userid = ?";
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
                        .setCheckmessage("====== Mapprogress loaded ======")
                        .build();

                responseObserver.onNext(response);
            } else {
                responseObserver.onNext(ItemRelationResponse.newBuilder()
                        .setCheckmessage("====== Mapprogress not loaded ======")
                        .build());
            }

            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    public void saveItemRelation(ItemRelationRequest request, StreamObserver<ItemRelationResponse> responseObserver) {
        DatabaseHelper dbHelper = new DatabaseHelper();

        try (Connection connection = dbHelper.connect()) {
            String getItemNameQuery = "SELECT itemname FROM itemlist_table WHERE itemid = ?";
            String query = "REPLACE INTO itemrelation_table (userid, itemid, itemname, quantity) VALUES (?, ?, ?, ?)";

            try (PreparedStatement getItemNameStmt = connection.prepareStatement(getItemNameQuery);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                getItemNameStmt.setString(1, request.getItemid());
                ResultSet rs = getItemNameStmt.executeQuery();

                if (rs.next()) {
                    String itemname = rs.getString("itemname");

                    statement.setInt(1, request.getUserid());
                    statement.setString(2, request.getItemid());
                    statement.setString(3, itemname);
                    statement.setInt(4, request.getQuantity());
                    statement.executeUpdate();

                    responseObserver.onNext(ItemRelationResponse.newBuilder()
                            .setCheckmessage("====== Item relation saved successfully ======")
                            .build());
                } else {
                    responseObserver.onNext(ItemRelationResponse.newBuilder()
                            .setCheckmessage("!!! Item not found !!!")
                            .build());
                }
            }
            responseObserver.onCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }
}