package jbossconfigurator.preferences;

import jbossconfigurator.Activator;
import jbossconfigurator.preferences.constants.types.PreferenceTypeConstants;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceTypeConstants.P_JBOSS_DS_PATH, "c:/jboss-5.1.0.GA/jboss-5.1.0.GA/server/default/deploy/oracle-xa-ds.xml");
		store.setDefault(PreferenceTypeConstants.P_JBOSS_USER, "t079wp");
		store.setDefault(PreferenceTypeConstants.P_JBOSS_PASSWORD, "mims");
		store.setDefault(PreferenceTypeConstants.P_JBOSS_URL, "jdbc:oracle:thin:@devorahome:1522:v8000utf");
	}

}
