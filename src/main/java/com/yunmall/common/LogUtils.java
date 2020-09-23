package com.yunmall.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LogUtils<T> {
    /**
     * 对象比较器 比较结果eg：1、字段名称loginName,旧值:liu,新值:gu;2、字段名称address,旧值:hunan,新值:neimenggu
     * 
     * @param oldBean
     * @param newBean
     * @return
     */

    public String compareObject(Object oldBean, Object newBean) {
        String str = "";

        // if (oldBean instanceof SysConfServer && newBean instanceof SysConfServer) {
        T pojo1 = (T) oldBean;
        T pojo2 = (T) newBean;
        try {
            Class clazz = pojo1.getClass().getSuperclass();
            System.out.println();
            Field[] fields1 = clazz.getDeclaredFields();
            List<Field> list = null;
            if (clazz.getName().equals("com.sinoservices.framework.core.query.BaseQueryItem")
                    || clazz.getName().equals("com.sinoservices.framework.core.model.BaseModel")) {
                list = new ArrayList();
            } else {
                list = new ArrayList(Arrays.asList(fields1));
            }
            list.addAll(Arrays.asList(pojo1.getClass().getDeclaredFields()));
            Object[] array = list.toArray();
            int i = 1;
            for (Object object : array) {
                Field field = (Field) object;
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                if ("recVer".equals(field.getName())) {
                    continue;
                }
                if ("createTime".equals(field.getName())) {
                    continue;
                }
                if ("modifyTime".equals(field.getName())) {
                    continue;
                }
                if ("timeZone".equals(field.getName())) {
                    continue;
                }

                PropertyDescriptor pd = null;
                boolean isContains = Arrays.asList(fields1).contains(field);
                if (isContains) {
                    pd = new PropertyDescriptor(field.getName(), clazz);
                } else {
                    pd = new PropertyDescriptor(field.getName(), pojo1.getClass());

                }
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = getMethod.invoke(pojo2);
                if (field.getName().equals("ebcdNameEns")) {
                    System.out.println("1");
                }
                if (o1 == null) {
                    if (!isEmpty(o2)) {
                        if (i != 1) {
                            str += "; ";
                        }
                        if (o2 instanceof Date) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            o2 = format.format((Date) o2);
                        }

                        str +=  i + "、字段名称：" + field.getName() + ",修改前:" + o1 + ",修改后:" + o2;
                        i++;
                    }
                    continue;
                }
                if (o2 == null) {
                    if (!isEmpty(o1)) {
                        if (i != 1) {
                            str += "; ";
                        }
                        if (o1 instanceof Date) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            o1 = format.format((Date) o1);
                        }
                        str +=  i + "、字段名称：" + field.getName() + ",修改前:" + o1 + ",修改后:" + o2;
                        i++;
                    }
                    continue;
                }
                if (o1 == null || o2 == null) {
                    continue;
                }
                // 判断是否是日期类型 //比较日期
                if (o1 instanceof Date || o2 instanceof Date) {
                    int compareTo = ((Date) o1).compareTo((Date) o2);
                    if (compareTo == 0) {
                        continue;
                    }
                    if (i != 1) {
                        str += "; ";
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    o1 = format.format((Date) o1);
                    o2 = format.format((Date) o2);
                    str +=  i + "、字段名称：" + field.getName() + ",修改前:" + o1 + ",修改后:" + o2;
                    i++;
                } else {

                    if (!o1.toString().equals(o2.toString())) {
                        if (i != 1) {
                            str += "; ";
                        }
                        str +=  i + "、字段名称：" + field.getName() + ",修改前:" + o1 + ",修改后:" + o2;
                        i++;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // }
        return str;
    }

    public static boolean isEmpty(Object obj) {
        if (obj != null && obj.equals("null"))
            return true;
        else if (obj == null)
            return true;
        else if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();
        else if (obj instanceof Map)
            return ((Map) obj).isEmpty();
        else if (obj.getClass().isArray())
            return Array.getLength(obj) == 0;
        else if (obj instanceof String)
            return ((String) obj).length() == 0;
        return false;
    }
}
