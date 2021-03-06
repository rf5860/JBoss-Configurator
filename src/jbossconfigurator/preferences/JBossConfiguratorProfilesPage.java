package jbossconfigurator.preferences;

import jbossconfigurator.Activator;
import jbossconfigurator.preferences.constants.strings.PreferenceStringConstants;
import jbossconfigurator.preferences.constants.types.PreferenceTypeConstants;
import jbossconfigurator.preferences.widgets.ConfigurableComboFieldEditor;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class provides a means of adding/deleting/editing profiles for use with
 * JBoss.
 */

public class JBossConfiguratorProfilesPage extends AbstractJBossConfiguratorPreferencesPage implements IWorkbenchPreferencePage {

	protected Button save;
	protected Button delete;
	protected StringFieldEditor profileName;
	protected StringFieldEditor userName;
	protected StringFieldEditor password;
	protected StringFieldEditor url;
	protected ConfigurableComboFieldEditor profiles;

	public JBossConfiguratorProfilesPage() {
		super(GRID);
		noDefaultAndApplyButton();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(PreferenceStringConstants.PROFILES_DESCRIPTION);
	}

	public void createFieldEditors() {
		profiles = constructComboBox();
		profiles.addListener(listener);
		profileName = new StringFieldEditor(PreferenceTypeConstants.P_PROFILE_DISPLAY, PreferenceStringConstants.PROFILE, getFieldEditorParent());
		userName = new StringFieldEditor(PreferenceTypeConstants.P_JBOSS_USER, PreferenceStringConstants.JBOSS_USER, getFieldEditorParent());
		password = new StringFieldEditor(PreferenceTypeConstants.P_JBOSS_PASSWORD, PreferenceStringConstants.JBOSS_PASSWORD, getFieldEditorParent());
		url = new StringFieldEditor(PreferenceTypeConstants.P_JBOSS_URL, PreferenceStringConstants.JBOSS_URL, getFieldEditorParent());
		addField(profiles);
		addField(profileName);
		addField(userName);
		addField(password);
		addField(url);
	}

	public void init(IWorkbench workbench) {
		listener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				String newValue = (String) event.getNewValue();
				if (newValue != null && newValue.trim() != "") {
					String[] values = newValue.split(",");
					if (values != null) {
						if (values.length == 4) {
							profileName.setStringValue(values[0]);
							userName.setStringValue(values[1]);
							password.setStringValue(values[2]);
							url.setStringValue(values[3]);
						} else {
							profileName.setStringValue("");
							userName.setStringValue("");
							password.setStringValue("");
							url.setStringValue("");
						}
					}
				}
			}

		};
	}

	protected Button createButton(Composite parent, String text) {
		Button b = new Button(parent, SWT.PUSH);
		b.setText(text);
		b.setEnabled(true);
		return b;
	}

	private String[] getProfileNameValue() {
		String profileNameValue = profileName.getStringValue();
		return new String[]{profileNameValue, getProfileValue()};
	}

	private String getProfileValue() {
	    String userNameValue = userName.getStringValue();
	    String passwordValue = password.getStringValue();
	    String urlValue = url.getStringValue();
		return userNameValue + "," + passwordValue + "," + urlValue;
	}

	private void updateProfileOption(String profileName, String profileValue) {
		profiles.updateNameValue(getProfileNameValue());
	}

	private void deleteProfileOption() {
		profiles.deleteCurrentProfile();
	}

	protected Control createContents(Composite parent) {
		Control returnControl = super.createContents(parent);
		save = createButton(parent, PreferenceStringConstants.SAVE_PROFILE);
		delete = createButton(parent, PreferenceStringConstants.DELETE_PROFILE);

		save.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				updateProfileOption(profileName.getStringValue(), getProfileValue());
			}
		});

		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				deleteProfileOption();
			}
		});
		return returnControl;
	}
}
