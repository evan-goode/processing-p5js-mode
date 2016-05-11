/**
 *	This used to be part of Processing 2.0 beta and was
 *	moved out on 2013-02-25
 */

package processing.mode.p5js;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import processing.app.*;
import processing.app.ui.*;
import processing.mode.java.AutoFormat;
import processing.mode.p5js.ServingEditor;

import javax.swing.*;

public class p5jsEditor extends ServingEditor {
	final static String PROP_KEY_MODE = "mode";
	final static String PROP_VAL_MODE = "JavaScript";

  private p5jsMode jsMode;

//  private DirectivesEditor directivesEditor;

  // tapping into Java mode might not be wanted?
  // processing.mode.java.PdeKeyListener listener;

	/**
	 *	Constructor, overrides ServingEditor( .. )
	 *
	 *	@see processing.mode.p5js.ServingEditor
	 */
  protected p5jsEditor ( Base base, String path, EditorState state, Mode mode )
  	throws EditorException {

    super(base, path, state, mode);

    jsMode = (p5jsMode) mode;
  }

	// ----------------------------------------
	//  abstract Editor implementations
	//  and standard overrides
	// ----------------------------------------

	/**
	 *	Create and return the toolbar (tools above text area),
	 *	implements abstract Editor.createToolbar(),
	 *	called in Editor constructor to add the toolbar to the window.
	 *
	 *	@return an EditorToolbar, in our case a JavaScriptToolbar
	 *	@see processing.mode.p5js.p5jsToolbar
	 */
  public EditorToolbar createToolbar ()
  {
    return new p5jsToolbar(this);
  }

	/**
	 *	Create a formatter to prettify code,
	 *	implements abstract Editor.createFormatter(),
	 *	called by Editor.handleAutoFormat() to handle menu item or shortcut
	 *
	 *	@return the formatter to handle formatting of code.
	 */
  public Formatter createFormatter ()
  {
    return new AutoFormat();
  }

	/**
	 *	Build the "File" menu,
	 *	implements abstract Editor.buildFileMenu(),
	 *	called by Editor.buildMenuBar() to generate the app menu for the editor window
	 *
	 *	@return JMenu containing the menu items for "File" menu
	 */
  public JMenu buildFileMenu ()
  {
    JMenuItem exportItem = Toolkit.newJMenuItem("Export", 'E');
    exportItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handleExport( true );
      }
    });
    return buildFileMenu(new JMenuItem[] { exportItem });
  }

	/**
	 *	Build the "Sketch" menu,
	 *	implements abstract Editor.buildSketchMenu(),
	 *	called by Editor.buildMenuBar() to generate the app menu for the editor window
	 *
	 *	@return JMenu containing the menu items for "Sketch" menu
	 */
  public JMenu buildSketchMenu ()
  {
	JMenuItem startServerItem = Toolkit.newJMenuItem("Run in Browser", 'R');
    startServerItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          handleStartServer();
        }
      });

	JMenuItem openInBrowserItem = Toolkit.newJMenuItem("Reopen in Browser", 'B');
    openInBrowserItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          handleOpenInBrowser();
        }
      });

    JMenuItem stopServerItem = new JMenuItem("Stop");
    stopServerItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          handleStopServer();
        }
      });

    return buildSketchMenu(new JMenuItem[] {
		startServerItem,
		openInBrowserItem,
		stopServerItem
	});
  }

	/**
	 *	Build the mode menu,
	 *	overrides Editor.buildModeMenu(),
	 *	called by Editor.buildMenuBar() to generate the app menu for the editor window
	 *
	 *	@return JMenu containing the menu items for "JavaScript" menu
	 */
  public JMenu buildModeMenu() {
    JMenu menu = new JMenu("JavaScript");
    JMenuItem item;

    /*
	item = new JMenuItem("Playback Settings (Directives)");
	item.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	      handleShowDirectivesEditor();
		}
	});
	menu.add(item);
	*/

	JMenuItem copyServerAddressItem = new JMenuItem("Copy Server Address");
	copyServerAddressItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			handleCopyServerAddress();
		}
	});
	menu.add( copyServerAddressItem );

	JMenuItem setServerPortItem = new JMenuItem("Set Server Port");
	setServerPortItem.addActionListener(new ActionListener(){
		public void actionPerformed (ActionEvent e) {
			handleSetServerPort();
		}
	});
	menu.add( setServerPortItem );

    menu.addSeparator();

	item = new JMenuItem("Start Custom Template");
	item.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		  handleCreateCustomTemplate();
		}
	});
	menu.add(item);

	item = new JMenuItem("Show Custom Template");
	item.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		  handleOpenCustomTemplateFolder();
		}
	});
	menu.add(item);

    return menu;
  }

	/**
	 *	Build the "Help" menu,
	 *	implements abstract Editor.buildHelpMenu(),
	 *	called by Editor.buildMenuBar() to generate the app menu for the editor window
	 *
	 *	@return JMenu containing the menu items for "Help" menu
	 */
  public JMenu buildHelpMenu() {
    JMenu menu = new JMenu("Help");
    JMenuItem item;

    item = new JMenuItem("Getting Started");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Platform.openURL("http://p5js.org/get-started/#your-first-sketch");
      }
    });
    menu.add(item);

    item = new JMenuItem("Reference");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Platform.openURL("http://p5js.org/reference/");
      }
    });
    menu.add(item);

    item = Toolkit.newJMenuItemShift("Find in Reference", 'F');
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //handleFindReferenceImpl();
        handleFindReference();
      }
    });
    menu.add(item);

    menu.addSeparator();

    item = new JMenuItem("Visit p5js.org");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Platform.openURL("http://p5js.org/");
      }
    });
    menu.add(item);

    item = new JMenuItem("Visit the Forum");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Platform.openURL("https://forum.processing.org/");
      }
    });
    menu.add(item);

    item = new JMenuItem("View p5js on Github");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Platform.openURL("https://github.com/processing/p5.js");
      }
    });
    menu.add(item);

    return menu;
  }


	/**
	 * Returns the default commenting prefix for comment/uncomment command,
	 * called from Editor.handleCommentUncomment()
	 */
  @Override
  public String getCommentPrefix() {
    return "//";
  }


	/**
	 *	Stop the runner, in our case this is the server,
	 *	implements abstract Editor.internalCloseRunner(),
	 *	called from Editor.prepareRun()
	 *
	 *  Called when the window is going to be reused for another sketch.
	 */
  public void internalCloseRunner ()
  {
      handleStopServer();

      /*
	  if ( directivesEditor != null )
	  {
		directivesEditor.hide();
		directivesEditor = null;
	  }
	  */
  }

	/**
	 *	Implements abstract Editor.deactivateRun()
	 */
  public void deactivateRun ()
  {
      // not sure what to do here ..
  }

	// ----------------------------------------
	//  handlers ... mainly for menu items
	// ----------------------------------------

	/**
	 *	Menu item callback, let's users set the server port number
	 */
  private void handleSetServerPort ()
  {
	statusEmpty();

	boolean wasRunning = isServerRunning();
	if ( wasRunning )
	{
		statusNotice("Server was running, changing the port requires a restart.");
		stopServer();
	}

	setServerPort();
	saveSketchSettings();

	if ( wasRunning ) {
		startServer( getExportFolder() );
	}
  }

	/**
	 *	Menu item callback, copy basic template to sketch folder
	 */
  private void handleCreateCustomTemplate ()
  {
	Sketch sketch = getSketch();

	File ajs = sketch.getMode().
				getContentFile( p5jsBuild.TEMPLATE_FOLDER_NAME );

	File tjs = getCustomTemplateFolder();

	if ( !tjs.exists() )
	{
		try {
			Util.copyDir( ajs, tjs );
			statusNotice( "Default template copied." );
			Platform.openFolder( tjs );
		} catch ( java.io.IOException ioe ) {
			Messages.showWarning("Copy default template folder",
				"Something went wrong when copying the template folder.", ioe);
		}
	}
	else
		statusError( "You need to remove the current "+
				     "\""+p5jsBuild.TEMPLATE_FOLDER_NAME+"\" "+
					 "folder from the sketch." );
  }

	/**
	 *	Menu item callback, open custom template folder from inside sketch folder
	 */
  private void handleOpenCustomTemplateFolder ()
  {
  	File tjs = getCustomTemplateFolder();
	if ( tjs.exists() )
	{
		Platform.openFolder( tjs );
	}
	else
	{
		// TODO: promt to create one?
		statusNotice( "You have no custom template with this sketch. Create one from the menu!" );
	}
  }

	/**
	 *	Menu item callback, copy server address to clipboard
	 */
  private void handleCopyServerAddress ()
  {
	String address = getServerAddress();

	if ( address != null )
	{
		java.awt.datatransfer.StringSelection stringSelection =
			new java.awt.datatransfer.StringSelection( address );
	    java.awt.datatransfer.Clipboard clipboard =
			java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents( stringSelection, null );
	}
  }

	/**
	 *	Menu item callback, open the playback settings frontend
	 */
  /*
  private void handleShowDirectivesEditor ()
  {
	if ( directivesEditor == null )
	{
	  directivesEditor = new DirectivesEditor(this);
	}

	directivesEditor.show();
  }
  */


	/**
	 *	Menu item callback, replacement for RUN:
	 *  export to folder, start server, open in default browser.
	 */
	public void handleStartServer ()
	{
		statusEmpty();

		if ( !startServer( getExportFolder() ) )
		{
			if ( !handleExport( false ) ) return;
			toolbar.activateRun();
		}

		// waiting for server to call "serverStarted() below ..."
	}

	/**
	 *	Menu item callback, open running server address in a browser
	 */
	private void handleOpenInBrowser ()
	{
		openBrowserForServer();
	}

	/**
	 *  Menu item callback, replacement for STOP: stop server.
	 */
  public void handleStopServer ()
  {
	stopServer();

	toolbar.deactivateRun();
  }

	/**
	 *	Menu item callback, call the export method of the sketch
	 *	and handle the gui stuff
	 */
  public boolean handleExport ( boolean openFolder )
  {
    if ( !handleExportCheckModifiedMod() )
    {
		return false;
	}
	else
	{

      //toolbar.activate(JavaScriptToolbar.EXPORT);

      try
	  {
        boolean success = jsMode.handleExport(sketch);
        if ( success && openFolder )
		{
          File exportFolder = new File( sketch.getFolder(),
 										  p5jsBuild.EXPORTED_FOLDER_NAME );
          Platform.openFolder( exportFolder );

          statusNotice("Finished exporting.");
        } else if ( !success ) {
          // error message already displayed by handleExport
	      return false;
        }
      } catch (Exception e) {
        statusError(e);

	    //toolbar.deactivate(JavaScriptToolbar.EXPORT);

		return false;
      }

      //toolbar.deactivate(JavaScriptToolbar.EXPORT);
    }
	return true;
  }

	/**
	 *  Menu item callback, changed from Editor.java to automaticaly
	 *	export and handle the server when it's running.
	 *	Normal save ops otherwise.
	 *
	 *	@param immediately set to false to allow it to be run in a Swing optimized manner
	 */
  public boolean handleSave ( boolean immediately )
  {
    if (sketch.isUntitled())
	{
      return handleSaveAs();
    }
	else if (immediately)
	{
      handleSave();
	  statusEmpty();
		if ( isServerRunning() ) handleStartServer();
    }
	else
	{
      	SwingUtilities.invokeLater(new Runnable()
		{
          	public void run()
			{
            	handleSave();
				statusEmpty();
				if ( isServerRunning() ) handleStartServer();
			}
        });
    }
    return true;
  }

	/**
	 *	Called from handleSave( true/false )
	 */
  public void handleSave ()
  {
    // toolbar.activate(JavaScriptToolbar.SAVE);

    handleSaveImpl();

    // toolbar.deactivate(JavaScriptToolbar.SAVE);
  }

	/**
	 *	Called from handleExport()
	 */
  private boolean handleExportCheckModifiedMod ()
  {
    if (sketch.isModified()) {
      Object[] options = { "OK", "Cancel" };
      int result = JOptionPane.showOptionDialog(this,
                                                "Save changes before export?",
                                                "Save",
                                                JOptionPane.OK_CANCEL_OPTION,
                                                JOptionPane.QUESTION_MESSAGE,
                                                null,
                                                options,
                                                options[0]);

      if (result == JOptionPane.OK_OPTION) {
        handleSave(true);
      } else {
        statusNotice("Export canceled, changes must first be saved.");
        return false;
      }
    }
    return true;
  }

	/**
	 *	Menu item callback
	 */
  public boolean handleSaveAs ()
  {
    // toolbar.activate(JavaScriptToolbar.SAVE);

    boolean result = super.handleSaveAs();

    // toolbar.deactivate(JavaScriptToolbar.SAVE);

    return result;
  }

	/**
	 *	Menu item callback for Sketch -> Import Library -> XXX
	 *
	 *	Copied from JavaEditor.java
	 */
	public void handleImportLibrary ( String jarPath )
	{
		// Messages.showWarning("Processing.js doesn't support libraries",
		//                  "Libraries are not supported. Import statements are " +
		//                  "ignored, and code relying on them will break.",
		//                  null);

		// make sure the user didn't hide the sketch folder
		sketch.ensureExistence();

		// import statements into the main sketch file (code[0])
		// if the current code is a .java file, insert into current
		if (mode.isDefaultExtension(sketch.getCurrentCode()))
		{
			sketch.setCurrentCode(0);
		}

		// could also scan the text in the file to see if each import
		// statement is already in there, but if the user has the import
		// commented out, then this will be a problem.
		String[] list = Util.packageListFromClassPath(jarPath).array();
		StringBuffer buffer = new StringBuffer();
		for ( int i = 0; i < list.length; i++ )
		{
			buffer.append("import ");
			buffer.append(list[i]);
			buffer.append(".*;\n");
		}
		buffer.append('\n');
		buffer.append(getText());
		setText(buffer.toString());
		setSelection(0, 0);  // scroll to start
		sketch.setModified(true);
	}

	// ----------------------------------------
	//  implementation BasicServerListener
	// ----------------------------------------

	/**
	 *	BasicServerListener implementation,
	 *	called by server once it starts serving
	 */
  public void serverStarted ()
  {
  		super.serverStarted();

		if ( !handleExport( false ) ) return;

		toolbar.activateRun();
  }

	// ----------------------------------------
	//  other methods
	// ----------------------------------------

	/**
	 *	Return the current export folder in a sane way
	 *
	 *	@return the export folder as File
	 */
  private File getExportFolder ()
  {
  	return new File( getSketch().getFolder(),
	 				 p5jsBuild.EXPORTED_FOLDER_NAME );
  }

	/**
	 *	Return the custom template folder
	 *
	 *	@return the custom template folder as File
	 */
  private File getCustomTemplateFolder ()
  {
	return new File( getSketch().getFolder(),
					 p5jsBuild.TEMPLATE_FOLDER_NAME );
  }

	/**
	 *	Save current sketch settings, this adds the server port to them
	 */
  private void saveSketchSettings ()
  {
	statusEmpty();

	File sketchProps = getSketchPropertiesFile();
	Settings settings;

	try {
		settings = new Settings(sketchProps);
	} catch ( IOException ioe ) {
		ioe.printStackTrace();
		return;
	}
	if ( settings == null )
	{
		statusError( "Unable to create sketch properties file!" );
		return;
	}
	settings.set( PROP_KEY_MODE, PROP_VAL_MODE );

	int port = getServerPort();
	if ( port > 0 ) settings.set( PROP_KEY_SERVER_PORT, (port+"") );

	settings.save();
  }
}