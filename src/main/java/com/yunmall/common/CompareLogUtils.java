package com.yunmall.common;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.yunmall.bean.User;

public class CompareLogUtils {
	public static void main(String[] args) {
		User beforeUser = new User();
		beforeUser.setAge(18);
		beforeUser.setId(1);
		beforeUser.setName("张三");
		beforeUser.setPassword("123456");
		User afterUser = new User();
		afterUser.setAge(18);
		afterUser.setId(1);
		afterUser.setName("李四");
		afterUser.setPassword("654321");
		compare(beforeUser,afterUser);
		LogUtils l=new LogUtils();
		String compareObject = l.compareObject(beforeUser, afterUser);
		System.out.println(compareObject);
	}
	public static String compare(Object beforeObj,Object afterObj) {
		try {
			Class<? extends Object> beforeClass = beforeObj.getClass();
			Class<? extends Object> afterClass = afterObj.getClass();
			if(!afterClass.getName().equals(beforeClass.getName())) {
				return "类型不对";
			}
			PropertyDescriptor[] pds = Introspector.getBeanInfo(beforeClass,  
			        Object.class).getPropertyDescriptors();
			
			for (PropertyDescriptor propertyDescriptor: pds) {
				String name=propertyDescriptor.getName();
				Method readMethod = propertyDescriptor.getReadMethod();
				Object beforeInvoke = readMethod.invoke(beforeObj);
				Object afterInvoke = readMethod.invoke(afterObj);
				if(beforeInvoke.equals(afterInvoke)) {
					continue;
				}
				System.out.println("属性："+name+beforeInvoke+"-------------"+afterInvoke);
			}
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
