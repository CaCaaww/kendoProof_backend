package com.backend.ttcust_api.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.backend.ttcust_api.sensitiveInfo;
import com.backend.ttcust_api.model.columnOrderObject;
import com.backend.ttcust_api.model.dataColumn;

public class columnOrderObjectDAO {
    sensitiveInfo info = new sensitiveInfo();
    ObjectMapper objectMapper = new ObjectMapper();
    private String matchingColumnID = "WHERE \"rpt-name\" IN (\'iauData\', \'custData\', \'seqData\')";
    String[] columnIds = {"iauData", "custData", "seqData"};
    
    private Connection con;

    public columnOrderObjectDAO(){
         try {
            Class.forName ( "com.ddtek.jdbc.openedge.OpenEdgeDriver");
            con = DriverManager.getConnection ( info.getUrl(), info.getUsername(), info.getPassword() );
            //con.setTransactionIsolation(1); // no locks
            System.out.println("NO ERRORS THROWN WHEN TRYING TO CONNECT");

        }  catch (Exception e) {
            System.out.println("ERROR:");
            e.printStackTrace();
        }
    }

   

    //public methods
    public columnOrderObject[] getAllColumnOrderObjects(){
          try{

            String query = "SELECT \"user-name\", \"rpt-name\", \"fill-string-1\" FROM pub.fpar " + matchingColumnID;
            //System.out.println(query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<columnOrderObject> colooArrayList = new ArrayList<>();
            while (resultSet.next()){
                String userId = resultSet.getString("user-name");
                String columnId = resultSet.getString("rpt-name");
                String dataColumns = resultSet.getString("fill-string-1");
                //System.out.println(dataColumns);
                List<dataColumn> dC = objectMapper.readValue(dataColumns, new TypeReference<List<dataColumn>>() {});
                System.out.println(dC);
                dataColumn[] dataColumns2 = new dataColumn[dC.size()];
                dC.toArray(dataColumns2);
                colooArrayList.add(new columnOrderObject(userId, columnId, dataColumns2 ));
            }
            columnOrderObject[] result = new columnOrderObject[colooArrayList.size()];
            colooArrayList.toArray(result);
            resultSet.close();
            statement.close();
            return result;
        } catch (Exception e){
            System.out.println(e);
        }
        return null;  
    }

    public columnOrderObject[] getColumnOrderByUserId(String userId){
        try{

            String query = "SELECT \"user-name\", \"rpt-name\", \"fill-string-1\" FROM pub.fpar " + matchingColumnID + " AND \"user-name\" = \'" + userId + "\'";
            //System.out.println(query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<columnOrderObject> colooArrayList = new ArrayList<>();
            while (resultSet.next()){
                String userId2 = resultSet.getString("user-name");
                String columnId = resultSet.getString("rpt-name");
                String dataColumns = resultSet.getString("fill-string-1");
                //System.out.println(dataColumns);
                List<dataColumn> dC = objectMapper.readValue(dataColumns, new TypeReference<List<dataColumn>>() {});
                System.out.println(dC);
                dataColumn[] dataColumns2 = new dataColumn[dC.size()];
                dC.toArray(dataColumns2);
                colooArrayList.add(new columnOrderObject(userId2, columnId, dataColumns2 ));
            }
            columnOrderObject[] result = new columnOrderObject[colooArrayList.size()];
            colooArrayList.toArray(result);
            resultSet.close();
            statement.close();
            return result;
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public columnOrderObject[] getColumnOrderByColumnId(String columnId){
        try{

            String query = "SELECT \"user-name\", \"rpt-name\", \"fill-string-1\" FROM pub.fpar " + matchingColumnID + " AND \"rpt-name\" = \'" + columnId + "\'";
            //System.out.println(query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<columnOrderObject> colooArrayList = new ArrayList<>();
            while (resultSet.next()){
                String userId = resultSet.getString("user-name");
                String columnId2 = resultSet.getString("rpt-name");
                String dataColumns = resultSet.getString("fill-string-1");
                //System.out.println(dataColumns);
                List<dataColumn> dC = objectMapper.readValue(dataColumns, new TypeReference<List<dataColumn>>() {});
                System.out.println(dC);
                dataColumn[] dataColumns2 = new dataColumn[dC.size()];
                dC.toArray(dataColumns2);
                colooArrayList.add(new columnOrderObject(userId, columnId2, dataColumns2 ));
            }
            columnOrderObject[] result = new columnOrderObject[colooArrayList.size()];
            colooArrayList.toArray(result);
            resultSet.close();
            statement.close();
            return result;
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public dataColumn[] getColumnOrderByUserAndColumnId(String identification){
        try{
            String userId = "";
            String columnId = "";
            for (String id : columnIds){
                int index = identification.indexOf(id);
                if (index >= 0){
                    columnId = id;
                    userId = identification.substring(0, index);
                    break;
                }
            }
            if (userId.equals("") || columnId.equals("")){
                return null;
            }

            String query = "SELECT \"user-name\", \"rpt-name\", \"fill-string-1\" FROM pub.fpar " + matchingColumnID + " AND \"user-name\" = \'" + userId + "\' AND \"rpt-name\" = \'" + columnId + "\'";
            //System.out.println(query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String dataColumns = resultSet.getString("fill-string-1");
                List<dataColumn> dC = objectMapper.readValue(dataColumns, new TypeReference<List<dataColumn>>() {});
                System.out.println(dC);
                dataColumn[] dataColumns2 = new dataColumn[dC.size()];
                dC.toArray(dataColumns2);
                resultSet.close();
                statement.close();
                return dataColumns2;
            }
            
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public columnOrderObject createColumnOrderObject(columnOrderObject col) throws IOException{
        try {
            String dataColumnString = "[";
            for (dataColumn c : col.getDataColumns()){
                dataColumnString += c.toString2() + ",";
            }
            dataColumnString = dataColumnString.substring(0, dataColumnString.length()-1);
            dataColumnString += "]";
            String query = "INSERT INTO pub.fpar (\"User-name\", \"rpt-name\", \"fill-string-1\") VALUES (\'" + col.getUserId() + "\', \'" + col.getColumnId() + "\', \' " + dataColumnString + "\')";
            //System.out.println("QUERY: " + query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            statement.executeUpdate(query);
            statement.close();
            
            
            return new columnOrderObject(col.getUserId(), col.getColumnId(), getColumnOrderByUserAndColumnId(col.getUserId() + col.getColumnId()));
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public columnOrderObject updateColumnOrderObject(columnOrderObject col) throws IOException{
        try {
            String dataColumnString = "[";
            for (dataColumn c : col.getDataColumns()){
                dataColumnString += c.toString2() + ",";
            }
            dataColumnString = dataColumnString.substring(0, dataColumnString.length()-1);
            dataColumnString += "]";
            String query = "UPDATE pub.fpar SET \"fill-string-1\" = \'" + dataColumnString + "\' WHERE \"user-name\" = \'" + col.getUserId() + "\' AND \"rpt-name\" = \'" + col.getColumnId() + "\'";
            //System.out.println("QUERY: " + query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            statement.executeUpdate(query);
            statement.close();
            
            
            return new columnOrderObject(col.getUserId(), col.getColumnId(), getColumnOrderByUserAndColumnId(col.getUserId() + col.getColumnId()));
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public columnOrderObject[] deleteByColumnId(String columnId) throws IOException{
         try {
            String query = "DELETE FROM pub.fpar WHERE \"rpt-name\" = \'" + columnId + "\'";
            //System.out.println("QUERY: " + query);
            Statement statement = con.createStatement();
            // execute the query and get the result set
            statement.executeUpdate(query);
            statement.close();
            
            
            return getAllColumnOrderObjects();
        } catch (Exception e){
            System.out.println(e);
        }
        return null;  
    }
}
