package com.tansun.data.db.visual.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.tansun.data.db.util.IOUtils;
import com.tansun.data.db.visual.model.RootModel;
import com.tansun.platform.xstream.XStreamSerializer;


public class VisualDBSerializer {

	public static InputStream serialize(RootModel model) throws UnsupportedEncodingException {
		return XStreamSerializer.serializeStream(model, VisualDBSerializer.class.getClassLoader());
	}

	public static RootModel deserialize(InputStream in) throws UnsupportedEncodingException {
		String xml = IOUtils.loadStream(in, "UTF-8");

		// 1.0.2 -> 1.0.3
		xml = xml.replaceAll("com\\.tansun\\.data\\.db\\.view\\.dialect\\.ColumnType", "com.tansun.data.db.dialect.ColumnType");

		return (RootModel) XStreamSerializer.deserialize(new ByteArrayInputStream(xml.getBytes("UTF-8")), VisualDBSerializer.class.getClassLoader());
	}

}
