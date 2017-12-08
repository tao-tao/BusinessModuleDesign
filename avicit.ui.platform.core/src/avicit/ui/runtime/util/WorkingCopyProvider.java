package avicit.ui.runtime.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.IWorkingCopyManagerExtension;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class WorkingCopyProvider {

	ICompilationUnit fCU = null;

	IFileEditorInput fEditorIn = null;

	IFile fFile = null;

	IDocument document = null;

	boolean fdisconnected;

	public WorkingCopyProvider(IFile file) {
		fFile = file;
	}

	public ICompilationUnit getWorkingCopy(boolean forceReconcile) {
		if (forceReconcile || fCU == null) {
			synchronized (this) {
				if (fCU == null) {
					try {
						getDocumentProvider().connect(getEditor());
						// getDocumentProvider().getAnnotationModel(getEditor()).connect(getDocument());
						IWorkingCopyManager workCopy = (IWorkingCopyManager) JavaUI.getWorkingCopyManager();
						fCU = workCopy.getWorkingCopy(getEditor());
						if (workCopy != null && workCopy instanceof IWorkingCopyManagerExtension) {
							((IWorkingCopyManagerExtension) workCopy).setWorkingCopy(getEditor(), fCU);
						}
					} catch (Exception e) {
					}
				}
			}
			try {
				if (!fCU.isConsistent()) {
					fCU.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor());
				}
			} catch (JavaModelException e) {
				System.out.println(e);
			}
			return fCU;
		} else
			return fCU;
	}

	/**
	 * @return IFileEditorInput accociated with the file resource
	 */
	public synchronized IFileEditorInput getEditor() {
		if (fEditorIn != null)
			return fEditorIn;
		fEditorIn = new FileEditorInput(fFile);
		return fEditorIn;
	}

	/**
	 * @return IFile resource associated with this provider
	 */
	public IFile getFile() {
		return fFile;
	}

	protected IDocumentProvider getDocumentProvider() {
		return JavaUI.getDocumentProvider();
	}

	/**
	 * @return IDocument associated with "the" Java Model working copy
	 */
	public synchronized IDocument getDocument() {
		if (document == null)
			document = getDocumentProvider().getDocument(getEditor());
		return document;
	}

	/**
	 * Dispose this provider
	 */
	public synchronized void dispose() {

		if (fdisconnected)
			return;

		// IAnnotationModel model =
		// getDocumentProvider().getAnnotationModel(getEditor());
		// if(model != null)
		// model.disconnect(getDocument());

		IWorkingCopyManager workCopy = (IWorkingCopyManager) JavaUI.getWorkingCopyManager();

		try {
			if (workCopy != null && workCopy instanceof IWorkingCopyManagerExtension)
				((IWorkingCopyManagerExtension) workCopy).removeWorkingCopy(getEditor());
		} catch (Exception e) {
		}

		workCopy.disconnect(getEditor());
		try {
			fCU.discardWorkingCopy();
		} catch (JavaModelException e) {
		}
		fCU = null;
		fEditorIn = null;
		fdisconnected = true;
	}

	/**
	 * ReConnect to the Shared/Local providers.
	 * 
	 * @param new
	 *            (or same) input file.
	 */
	public synchronized void connect(IFile file) {

		if (file == null)
			throw new IllegalArgumentException();
		if (!fdisconnected) {
			if (file.equals(fFile))
				return;
		} else
			dispose();

		fFile = file;

		getEditor();
		fdisconnected = false;
	}

	public String toString() {
		return "WCP [" + fFile + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.codegen.util.IWorkingCopyProvider#getDocumentLock()
	 */
	public Object getDocumentLock() {
		return ((ISynchronizable) getDocument()).getLockObject();
	}

	public boolean isFdisconnected() {
		return fdisconnected;
	}

	public void setFdisconnected(boolean fdisconnected) {
		this.fdisconnected = fdisconnected;
	}
}
