package avicit.ui.runtime.core.node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDoubleClickListener;
import net.sf.json.JSONObject;

import avicit.ui.core.runtime.resource.IFileDelegate;
import avicit.ui.core.runtime.resource.IResourceDelegate;
import avicit.ui.runtime.core.action.OpenFormAction;

public class PresentationFormNode extends AbstractFileNode
{
	JSONObject internalObj;
	long lastModified = 0;
	
    public PresentationFormNode(IFileDelegate file)
    {
        super(file);
        setOrder(2);
    }

    public Object getModel()
    {
        return this.getFile();
    }

    @Override
	public String getDisplayName() {
		String name = this.getFile().getName();
		int dotIndex = name.indexOf(".");
		if(dotIndex>0)
			name = name.substring(0,dotIndex);
		return name;
	}

	public String getType()
    {
        return "form";
    }

    public Object getAdapter(Class adapter)
    {
        if(IResourceDelegate.class == adapter)
            return getResource();
        if(IProject.class == adapter)
        {
			try {
				return getResource().getProject().getAdapter(adapter);
			} catch (ResourceException e) {
				return null;
			}
        }
        if(IDoubleClickListener.class == adapter)
        {
        	return new OpenFormAction();
        }
		else
		{
            return super.getAdapter(adapter);
		}
    }
    

	public JSONObject getInternalObj() {
//		if( internalObj == null)
		{
			IFile f = (IFile) this.resource.getResource();
			if( f == null || f.getModificationStamp() > this.lastModified)
			{
				String str = getFileString(f.getLocation().toFile(), Charset.defaultCharset().name());
				try{
					internalObj = JSONObject.fromObject(str);
					this.lastModified = f.getModificationStamp();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return internalObj;
	}

	public void setInternalObj(JSONObject internalObj) {
		this.internalObj = internalObj;
	}

	public String getFileString(File file, String code) {
		Reader re1;
		try {
			re1 = new InputStreamReader(new FileInputStream(file.getPath()), code);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		Reader re = re1;
		StringBuilder sb = new StringBuilder();
		
		try {
			char[] buffer = new char[1024];
			while (true) {
				int count = re.read(buffer);
				if (count == -1) {
					break;
				}
				char[] da = new char[count];
		
				System.arraycopy(buffer, 0, da, 0, count);
				sb.append(da);
		
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (re != null) {
				try {
					re.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String str1 = sb.toString().trim();//sb.substring(0,sb.length()-1);
		
		/*
		 * �ڶ�ȡĳЩ�ļ���ʱ��,�����ַ���ͷ�����ɿո�.������utf-8���뷽ʽ��ȡͷ����"0xFFFE"�ֽ�(BOM)���ı��ļ�ʱ.
		 * �����������,��������Ϊutf-8��׼�в���Ҫbom,����ʵ�ϴ󲿷�utf8�ļ�����bom.���ƺ�java
		 * readerû�н����㹻���õļ��ݴ���. һ���ո�������ʲô������.����һЩxml
		 * parserȴ������prolog��ǰ���κ�����,�����ո�,�����׳��쳣.
		 * 
		 */
//		while (!str1.startsWith("{") && str1.length() > 0) {
//			str1 = str1.substring(1);
//		}
		int index = str1.indexOf("{");
		int lastindex = str1.lastIndexOf("}");
		if(index>=0)
			str1 = str1.substring(index, lastindex+1);
		String str = str1;
		return str;
	}
}