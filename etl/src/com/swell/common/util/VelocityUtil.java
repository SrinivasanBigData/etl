package com.swell.common.util;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

/**
 * Velocity模版辅助类
 * 
 * @author s.well.tt
 *
 */
public class VelocityUtil {
	private static ClasspathResourceLoader classLoader = new ClasspathResourceLoader();
	private static FileResourceLoader fileLoader = new FileResourceLoader();

	public static void main(String args[]) throws Exception {
		String s = "We are using $project $name to render this.";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Velocity");
		map.put("project", "Jakarta");

		System.out.println(" string : " + evlString(map, s));

		InputStream in = getClasspathResource("/com/swell/etl/vm/velocity.properties");
		String result = IOUtil.loadString(in);
		System.out.println(result);
		
//		String strTemplate ="true";
//		System.out.println(evaluate(map, strTemplate));
//		System.out.println(evlBoolean(map, strTemplate));

	}

	/**
	 * 加载classpath内的资源
	 * 
	 * @param classpathResourceName
	 * @return
	 */
	public static InputStream getClasspathResource(String classpathResourceName) {
		return classLoader.getResourceStream(classpathResourceName);
	}

	/**
	 * 加载文件系统资源
	 * 
	 * @param fileResourceName
	 * @return
	 */
	public static InputStream getFileResource(String fileResourceName) {
		return fileLoader.getResourceStream(fileResourceName);
	}

	public static StringWriter evaluate(Map<String, Object> map, Reader reader) {
		/* first, we init the runtime engine. Defaults are fine. */
		Velocity.init();

		/* lets make a Context and put data into it */
		VelocityContext context = new VelocityContext(map);

		/* lets render a template */
		StringWriter writer = new StringWriter();

		/* lets make our own string to render */
		Velocity.evaluate(context, writer, "VelocityUtil", reader);
		return writer;
	}

	public static StringWriter evaluate(Map<String, Object> map,
			String strTemplate) {
		/* first, we init the runtime engine. Defaults are fine. */
		Velocity.init();

		/* lets make a Context and put data into it */
		VelocityContext context = new VelocityContext(map);

		/* lets render a template */
		StringWriter writer = new StringWriter();

		/* lets make our own string to render */
		Velocity.evaluate(context, writer, "VelocityUtil", strTemplate);
		return writer;
	}

	public static String evlString(Map<String, Object> map, String strTemplate) {
		return evaluate(map, strTemplate).toString();
	}

	public static Integer evlInteger(Map<String, Object> map, String strTemplate) {
		return Integer.valueOf(evaluate(map, strTemplate).toString());
	}

	public static Double evlDouble(Map<String, Object> map, String strTemplate) {
		return Double.valueOf(evaluate(map, strTemplate).toString());
	}

	public static Float evlFloat(Map<String, Object> map, String strTemplate) {
		return Float.valueOf(evaluate(map, strTemplate).toString());
	}

	public static boolean evlBoolean(Map<String, Object> map, String strTemplate) {
		return Boolean.valueOf(evaluate(map, strTemplate).toString());
	}

	/**
	 * 支持ISO日期时间格式的转换
	 * 
	 * @param map
	 * @param strTemplate
	 * @return
	 * @throws ParseException
	 * 
	 * @see {@link DateFormatUtils}
	 */
	public static Date evlDate(Map<String, Object> map, String strTemplate)
			throws ParseException {
		String[] patterns = new String[] {
				DateFormatUtils.ISO_DATE_FORMAT.getPattern(),
				DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern(),
				DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(),
				DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),
				DateFormatUtils.ISO_TIME_FORMAT.getPattern(),
				DateFormatUtils.ISO_TIME_NO_T_FORMAT.getPattern(),
				DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern(),
				DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.getPattern() };
		return DateUtils.parseDate(evaluate(map, strTemplate).toString(),
				patterns);
	}
}
