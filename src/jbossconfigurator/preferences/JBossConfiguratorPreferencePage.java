package jbossconfigurator.preferences;

import jbossconfigurator.Activator;
import jbossconfigurator.preferences.constants.strings.PreferenceStringConstants;
import jbossconfigurator.preferences.constants.types.PreferenceTypeConstants;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Adds a preferences page from which JBoss configurations can be made. This
 * extends <samp>FieldEditorPreferencePage</samp This page is used to modify
 * preferences only. They are stored in the preference store that belongs to the
 * main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */

public class JBossConfiguratorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	protected Button changeDataSource;

	public JBossConfiguratorPreferencePage() {
		super(GRID);
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
	}

	protected Control createContents(Composite parent) {
		Control returnControl = super.createContents(parent);
		changeDataSource = new Button(parent, SWT.PUSH);
		changeDataSource.setText(PreferenceStringConstants.CHANGE_DATA_SOURCE);
		changeDataSource.setEnabled(true);
		changeDataSource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

			}
		});
		return returnControl;
	}
}