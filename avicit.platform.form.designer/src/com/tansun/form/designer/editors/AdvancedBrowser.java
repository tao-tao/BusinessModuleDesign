package com.tansun.form.designer.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
  
/**
 *   
 * @author lidong
 *
 */
public class AdvancedBrowser {  
     // The "at rest" text of the throbber  
     private static final String AT_REST = "Ready";  
     private Browser browser = null;
     /** 
      * Runs the application 
      *  
      * @param location the initial location to display 
      */  
     public void run(String location) {  
         Display display = new Display();  
         Shell shell = new Shell(display);  
         shell.setText("Advanced Browser");  
         createContents(shell, location);  
         shell.open();  
         while (!shell.isDisposed()) {  
             if (!display.readAndDispatch()) {  
                 display.sleep();  
             }  
         }  
         display.dispose();  
     }  
   
     /** 
      * Creates the main window's contents 
      *  
      * @param shell the main window 
      * @param location the initial location 
      */  
     public void createContents(Composite shell, String location) {  
         shell.setLayout(new org.eclipse.swt.layout.GridLayout());  
   
         
         browser = new Browser(shell, SWT.BORDER);
         browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
         
         Label throbber = new Label(shell, SWT.NONE);
         throbber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
         browser.addProgressListener(new AdvancedProgressListener(throbber));
         // Go to the initial URL  
         if (location != null) {
             browser.setUrl(location);
             browser.forward();
         }  
     }  
  
     /** 
      * This class implements a CloseWindowListener for AdvancedBrowser 
      */  
     class AdvancedCloseWindowListener implements CloseWindowListener {  
         /** 
          * Called when the parent window should be closed 
          */  
         public void close(WindowEvent event) {  
             // Close the parent window  
             ((Browser) event.widget).getShell().close();  
         }  
     }  
  
     /** 
      * This class implements a LocationListener for AdvancedBrowser 
      */  
     class AdvancedLocationListener implements LocationListener {  
         // The address text box to update  
         private Text location;  
         
         /** 
          * Constructs an AdvancedLocationListener 
          *  
          * @param text the address text box to update 
          */  
         public AdvancedLocationListener(Text text) {  
             // Store the address box for updates  
             location = text;  
         }  
   
         /** 
          * Called before the location changes 
          *  
          * @param event the event 
          */  
         public void changing(LocationEvent event) {  
             // Show the location that's loading  
             location.setText("Loading " + event.location + "...");  
         }  
   
         /** 
          * Called after the location changes 
          *  
          * @param event the event 
          */  
         public void changed(LocationEvent event) {  
             // Show the loaded location  
             location.setText(event.location);  
         }  
     }  
   
     /** 
      * This class implements a ProgressListener for AdvancedBrowser 
      */  
     class AdvancedProgressListener implements ProgressListener {  
         // The label on which to report progress  
         private Label progress;  
   
         /** 
          * Constructs an AdvancedProgressListener 
          *  
          * @param label the label on which to report progress 
          */  
         public AdvancedProgressListener(Label label) {  
             // Store the label on which to report updates  
             progress = label;  
         }  
   
         /** 
          * Called when progress is made 
          *  
          * @param event the event 
          */  
         public void changed(ProgressEvent event) {  
             // Avoid divide-by-zero  
             if (event.total != 0) {  
                 // Calculate a percentage and display it  
                 int percent = (int) (event.current / event.total);  
                 progress.setText(percent + "%");  
             } else {  
                // Since we can't calculate a percent, show confusion :-)  
                 progress.setText("\u5b8c\u6210");  
             }  
         }
   
         /** 
          * Called when load is complete 
          *  
          * @param event the event 
          */  
         public void completed(ProgressEvent event) {  
             // Reset to the "at rest" message  
             progress.setText(AT_REST);  
         }  
     }  
   
     /** 
      * This class implements a StatusTextListener for AdvancedBrowser 
      */  
     class AdvancedStatusTextListener implements StatusTextListener {  
         // The label on which to report status  
         private Label status;  
   
         /** 
          * Constructs an AdvancedStatusTextListener 
          *  
          * @param label the label on which to report status 
          */  
         public AdvancedStatusTextListener(Label label) {  
             // Store the label on which to report status  
             status = label;  
         }  
   
         /** 
          * Called when the status changes 
          *  
          * @param event the event 
          */  
         public void changed(StatusTextEvent event) {  
             // Report the status  
             status.setText(event.text);  
         }  
     }  
   
     /** 
      * The application entry point 
      *  
      * @param args the command line arguments 
      */  
     public static void main(String[] args) {  
         new AdvancedBrowser().run(args.length == 0 ? null : args[0]);  
     }

	public Browser getBrowser() {
		return browser;
	}  
 }  