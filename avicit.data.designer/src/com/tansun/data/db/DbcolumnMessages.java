package com.tansun.data.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.tansun.data.db.dialect.ColumnType;
import com.tansun.data.db.dialect.IColumnType;

public class DbcolumnMessages {


	public static IColumnType[] getColumnTypes(String typeDb) {
		Vector<ColumnType> v = new Vector<ColumnType>();
		
		try {
			InputStream in = DbcolumnMessages.class.getResourceAsStream(typeDb+"ColumnType.properties");
			InputStreamReader read = new InputStreamReader(in, "utf-8");
			BufferedReader reader = new BufferedReader(read);
			String line;
			while ((line = reader.readLine()) != null) {
					try {
					String[] stro = line.split("=");
					String pname=stro[0].trim();
					String[] str = stro[1].split("[|]");
					String lname=str[0].trim();
					boolean issu=Integer.valueOf(str[1].trim())==1?true:false;
					int minsize=Integer.valueOf(str[3].trim());
					int maxsize=Integer.valueOf(str[4].trim());
					int type=Integer.valueOf(str[2].trim());
					ColumnType content = new ColumnType(pname,lname,issu,3);
					content.setMinsize(minsize);
					content.setMaxsize(maxsize);
					if(pname.equals("NUMBER")){
						content.setDminsize(Integer.valueOf(str[5].trim()));
						content.setDmaxsize(Integer.valueOf(str[6].trim()));
					}
					v.add(content);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in.close();
			read.close();
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IColumnType[] c=new IColumnType[v.size()];
		for(int i=0;i<v.size();i++){
			c[i]=v.get(i);
		}
		return c;
	}
}
