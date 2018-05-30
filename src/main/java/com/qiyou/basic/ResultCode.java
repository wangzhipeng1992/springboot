package com.qiyou.basic;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * 
 * base类code标识
 * @author wang
 * @since 2018-05-29 10:11
 *
 */
public class ResultCode {

	@Sub(name = "SUCCESS")
	public static String RESULT_SUCCESS = "0000";

	@Sub(name = "FAILED : ")
	public static String RESULT_FAILED = "0101";

	@Sub(name = "internal server error")
	public static String RESULT__ERROR_2 = "0102";

	@Retention(RUNTIME)
	@Target({ FIELD })
	public @interface Sub {
		public String name();
	}

	public static String getValueByKey(String key) {
		ResultCode mySub = new ResultCode();

		if (key == null || key.equals("")) {
			key = ResultCode.RESULT_SUCCESS;
		}

		String result = "Success";

		// getFields()只能获取public的字段，包括父类的。
		// getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
		Field[] fields = mySub.getClass().getDeclaredFields();
		for (Field field : fields) {

			// 返回该元素的指定类型的注释
			Annotation ano = field.getAnnotation(Sub.class);
			try {
				if (key.equals(field.get(mySub).toString()) && ano != null) {
					Sub sub = (Sub) ano;
					result = (String) sub.name();
					break;
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;

	}

}
