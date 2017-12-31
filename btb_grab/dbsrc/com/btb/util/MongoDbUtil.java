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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class MongoDbUtil {
	private static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	private static MongoDatabase database = mongoClient.getDatabase("bxs");
    private static MongoCollection<Document> marketTab = database.getCollection("market");
    private static MongoCollection<Document> buySellDiskTab = database.getCollection("buySellDisk");
    
    public static void insertOrUpdateBuySellDiskTab(final Map<String, Object> map) {
    	Document document=new Document();
    	for (String key : map.keySet()) {
			if (!key.equals("_id")) {
				document.append(key, map.get(key));
			}
		}
    	//不存在就添加
    	UpdateOptions updateOptions = new UpdateOptions();
    	updateOptions.upsert(true);
		buySellDiskTab.updateOne(eq("_id", map.get("_id")), new Document("$set", document),updateOptions, new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(UpdateResult result, Throwable ex) {
				if (ex!=null) {
					System.out.println("mongoDb更新报错了:");
					ex.printStackTrace();
				}
			}
		});
    }
    
    public static void insertOrUpdate(final Market market) {
    	Document document=new Document();
    	for(String columName:columns.keySet()){
			Method method = columns.get(columName);
			try {
				Object object = method.invoke(market, null);
				if (object != null) {
					document.append(columName,object);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	//不存在就添加
    	UpdateOptions updateOptions = new UpdateOptions();
    	updateOptions.upsert(true);
		marketTab.updateOne(eq("_id", market.get_id()), new Document("$set", document),updateOptions, new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(UpdateResult result, Throwable ex) {
				if (ex!=null) {
					System.out.println("mongoDb更新报错了:");
					ex.printStackTrace();
				}
			}
		});
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
