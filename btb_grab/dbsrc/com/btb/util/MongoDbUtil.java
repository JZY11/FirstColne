package com.btb.util;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.btb.entity.Market;
import com.mongodb.Block;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.*;
import com.mongodb.client.result.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MongoDbUtil {
	private static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	private static  MongoDatabase database = mongoClient.getDatabase("bxs");
    private static MongoCollection<Document> marketTab = database.getCollection("market");
    
    public static void main(String[] args) throws InterruptedException {
    	
    	final CountDownLatch dropLatch3 = new CountDownLatch(1);
		marketTab.updateOne(eq("_id_", "test"), set("moneytype", "宋荣洋"), new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(UpdateResult result, Throwable arg1) {
				long count = result.getMatchedCount();
				if (count==0) {
					Document document = new Document("_id", "test12");
					document.append("moneytype", "宋荣洋");
					marketTab.insertOne(document,new SingleResultCallback<Void>(){
						@Override
						public void onResult(Void arg0, Throwable arg1) {
							
						}
						
					});
				}
				System.out.println(count);
				dropLatch3.countDown();
			}
		});
		dropLatch3.await();
	}
    
    public static void insertOrUpdate(Market market) {
    	final CountDownLatch dropLatch3 = new CountDownLatch(1);
    	Bson bson = set("moneytype", "宋荣洋");
    	
		marketTab.updateOne(eq("_id", market.get_id()), set("moneytype", "宋荣洋"), new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(UpdateResult result, Throwable arg1) {
				long count = result.getMatchedCount();
				if (count==0) {
					Document document = new Document("_id", "test12");
					document.append("moneytype", "宋荣洋");
					marketTab.insertOne(document,new SingleResultCallback<Void>(){
						@Override
						public void onResult(Void arg0, Throwable arg1) {
							
						}
						
					});
				}
				System.out.println(count);
				dropLatch3.countDown();
			}
		});
		try {
			dropLatch3.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
     
    
    public static Map<String, Method> columns = getColumns();
	private static Map<String, Method> getColumns() {
		Map<String, Method> columns=new HashMap<>();
		Field[] fields = Market.class.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			try {
				if (!fieldName.equals("_id")) {
					columns.put(fieldName, Market.class.getMethod("get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1)));
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}
}
