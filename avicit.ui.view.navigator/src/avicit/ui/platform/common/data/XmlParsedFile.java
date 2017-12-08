package avicit.ui.platform.common.data;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;

public class XmlParsedFile {

	protected IFile iFile;
	protected FileEditorInput input;
	protected IDocumentProvider provider;
	protected IDocument iDoc;
	protected Element ele;
	protected IStructuredModel model;
	protected IDOMDocument iDomDoc;

	public XmlParsedFile(IFile iFile)
	{
		init(iFile);
	}
	
	public void init(IFile ifile)
	{
		this.iFile = ifile;
		try {
			iFile.refreshLocal(1, null);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		input = new FileEditorInput(iFile);
		provider = DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		try {
			provider.connect(input);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		iDoc = provider.getDocument(input);
		if (iDoc instanceof IStructuredDocument) 
			model = StructuredModelManager.getModelManager().getModelForEdit((IStructuredDocument) iDoc);
		
		iDomDoc = ((IDOMModel)model).getDocument();
		ele = iDomDoc.getDocumentElement();	
	}

	public void save() {
		try {
			if(provider != null)
				provider.saveDocument(null, input, iDoc, true);
			/*
			try {
				SAXReader reader = new SAXReader(DocumentFactory.getInstance());
				reader.setValidation(false);
				Document document = null;
				String filePath = this.input.getPath().toOSString();
				InputStream in = new FileInputStream(filePath);
				try {
					document = reader.read(in);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(in);
				}
				Writer pw = new PrintWriter(filePath,"UTF-8");
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setIndent(true);
				format.setTrimText(true);
				format.setNewlines(true);
				format.setEncoding("UTF-8");
				XMLWriter writer = new XMLWriter(pw, format);
				writer.write(document);
				writer.flush();
				writer.close();
				pw.close();
				this.init(this.iFile);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			*/
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		if(provider != null)
		{
			//try {
			//	provider.resetDocument(input);
			//} catch (CoreException e) {
			//	e.printStackTrace();
			//}
			provider.disconnect(input);
		}
		if(model != null)
		{
			try{
				model.releaseFromEdit();
			}catch(Exception e){}
		}
	}

	public Element getEle() {
		return ele;
	}

	public IDocumentProvider getProvider() {
		return provider;
	}

	public void setProvider(IDocumentProvider provider) {
		this.provider = provider;
	}

	public IDOMDocument getIDomDoc() {
		return iDomDoc;
	}
	
	public IFile getFile()
	{
		return this.iFile;
	}

}