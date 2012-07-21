package jbossconfigurator.preferences;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jbossconfigurator.Activator;
import jbossconfigurator.preferences.constants.strings.PreferenceStringConstants;
import jbossconfigurator.preferences.constants.types.PreferenceTypeConstants;

import org.apache.xerces.parsers.DOMParser;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Adds a preferences page from which JBoss configurations can be made. This
 * extends <samp>FieldEditorPreferencePage</samp This page is used to modify
 * preferences only. They are stored in the preference store that belongs to the
 * main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */

public class JBossConfiguratorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	protected Button changeDataSource;
	protected XPathFactory factory;
	protected XPath xpath;
	protected NamespaceContext context;
	protected DOMParser parser;

	public JBossConfiguratorPreferencePage() {
		super(GRID);
		noDefaultAndApplyButton();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(PreferenceStringConstants.PREFRENCES_DESCRIPTION);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new FileFieldEditor(PreferenceTypeConstants.P_JBOSS_DS_PATH, PreferenceStringConstants.JBOSS_DS_PATH, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceTypeConstants.P_JBOSS_USER, PreferenceStringConstants.JBOSS_USER, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceTypeConstants.P_JBOSS_PASSWORD, PreferenceStringConstants.JBOSS_PASSWORD, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceTypeConstants.P_JBOSS_URL, PreferenceStringConstants.JBOSS_URL, getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		parser = new DOMParser();
	}

	protected Control createContents(Composite parent) {
		Control returnControl = super.createContents(parent);
		changeDataSource = new Button(parent, SWT.PUSH);
		changeDataSource.setText(PreferenceStringConstants.CHANGE_DATA_SOURCE);
		changeDataSource.setEnabled(true);
		changeDataSource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				File dataSourceFile = getDataSourceFile();
				if (dataSourceFile == null) {
					System.out.println("Halting update of DataSource properties - file could not be located");
					return;
				}
				updateDataSource(dataSourceFile);
			}

		});
		return returnControl;
	}

	private Node getAttributeWithName(Document root, String name) {
		try {
			XPathExpression expression = xpath.compile("//*[@name='" + name + "']");
			NodeList items = (NodeList) expression.evaluate(root, XPathConstants.NODESET);
			if (items != null && items.getLength() > 0) {
				return items.item(0);
			}
		} catch (XPathExpressionException e) {
			System.err.println("Issue running XPath");
			e.printStackTrace();
		}
		return null;
	}

	private void updateNode(Document doc, String name, String pref) {
		System.out.println("Attempting to update " + name);
		Node node = getAttributeWithName(doc, name);
		if (node == null) {
			System.err.println("Error retrieving " + name);
			return;
		}
		updateNode(node, pref);
	}

	private void updateDataSource(File dataSource) {
		System.out.println("Updating data source file.");
		try {
			parser.parse(dataSource.getAbsolutePath());
			Document root = parser.getDocument();
			updateNode(root, PreferenceStringConstants.JBOSS_USER_ATTRIBUTE, PreferenceTypeConstants.P_JBOSS_USER);
			updateNode(root, PreferenceStringConstants.JBOSS_PASSWORD_ATTRIBUTE, PreferenceTypeConstants.P_JBOSS_PASSWORD);
			updateNode(root, PreferenceStringConstants.JBOSS_URL_ATTRIBUTE, PreferenceTypeConstants.P_JBOSS_URL);
			saveFile(dataSource, root);
		} catch (SAXException e) {
			System.err.println("Issue parsing data source file.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Issue opening file.");
			e.printStackTrace();
		}
		System.out.println("Finished updating data source file.");
	}
	private void saveFile(File file, Document doc) {
		// Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		} catch (Exception e) {
			System.err.println("Error saving file.");
			e.printStackTrace();
		}

	}

	private void updateNode(Node node, String preference) {
		IPreferenceStore preferencesStore = getPreferenceStore();
		String value = preferencesStore.getString(preference);
		System.out.println("Updating with value: '" + value + "'");
		node.getFirstChild().setNodeValue(value);
	}

	private File getDataSourceFile() {
		System.out.println("Attempting to retrieve JBoss Data Source file.");
		IPreferenceStore preferencesStore = getPreferenceStore();
		String path = preferencesStore.getString(PreferenceTypeConstants.P_JBOSS_DS_PATH);
		File dataSourceFile = new File(path);
		if (!dataSourceFile.exists()) {
			System.err.println("Could not find JBoss Data Source file" + path);
			return null;
		}
		System.out.println("Found JBoss Data Source file" + path);
		return dataSourceFile;
	}
}